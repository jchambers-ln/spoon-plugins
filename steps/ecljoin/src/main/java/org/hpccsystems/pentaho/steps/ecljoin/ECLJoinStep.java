package org.hpccsystems.pentaho.steps.ecljoin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Join;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLJoinStep extends BaseStep implements StepInterface {

    private ECLJoinStepData data;
    private ECLJoinStepMeta meta;

    public ECLJoinStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (ECLJoinStepMeta) smi;
		data = (ECLJoinStepData) sdi;

		Object[] r = getRow();
		String input = "";
		if (r == null) {
		} else {
			logBasic("Found Row = " + r[r.length - 1]);
			input = r[r.length - 1].toString() + "\r\n";
		}

		Object[] newRow = new Object[1];

		/*
		 * Dataset dataset = new Dataset();
		 * dataset.setLogicalFileName(meta.getLogicalFileName());
		 * dataset.setName(meta.getDatasetName()); //
		 * dataset.setRecordFormatString(getRecordDef());
		 * dataset.setRecordFormatString(meta.resultListToString());
		 * dataset.setRecordName(meta.getRecordName());
		 * dataset.setFileType(meta.getFileType());
		 * dataset.setRecordSet(meta.getRecordSet());
		 * 
		 * newRow[0] = input + dataset.ecl();
		 */
		
		Join join = new Join();
        join.setName(meta.getJoinRecordSet());
        join.setJoinCondition("left."+meta.getLeftJoinCondition()+" = "+"right."+meta.getRightJoinCondition());
        join.setJoinType(meta.getJoinType());
        join.setLeftRecordSet(meta.getLeftRecordSet());
        join.setRightRecordSet(meta.getRightRecordSet());

        newRow[0] = input + join.ecl();

		putRow(data.outputRowMeta, newRow);

		logBasic("{Dataset Step} Output = " + newRow[0]);

		return false;
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    	super.setStepname(meta.getStepName());
        meta = (ECLJoinStepMeta) smi;
        data = (ECLJoinStepData) sdi;

        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLJoinStepMeta) smi;
        data = (ECLJoinStepData) sdi;

        super.dispose(smi, sdi);
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic("Starting to run...");
        try {
            while (processRow(meta, data) && !isStopped())
				;
        } catch (Exception e) {
            logError("Unexpected error : " + e.toString());
            logError(Const.getStackTracker(e));
            setErrors(1);
            stopAll();
        } finally {
            dispose(meta, data);
            logBasic("Finished, processing " + getLinesRead() + " rows");
            markStop();
        }
    }
}
