package org.hpccsystems.pentaho.steps.ecldedup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Dedup;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLDedupStep extends BaseStep implements StepInterface {

    private ECLDedupStepData data;
    private ECLDedupStepMeta meta;

    public ECLDedupStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (ECLDedupStepMeta) smi;
        data = (ECLDedupStepData) sdi;
        
        
        Object[] r = getRow(); 
        String input = "";
        if (r == null) 
        {
        } else {
            logBasic("Found Row = " + r[r.length-1]);
            input = r[r.length-1].toString() + "\r\n";
        }
        
        Object[] newRow = new Object[1]; 
        
        //call the related direct library and fetch the ecl code
        
        //Create a line like htis where op is the direct library that referecnes the direct 
        //newRow[0] = input + op.ecl();
        
        Dedup dedup = new Dedup();
        dedup.setName(meta.getRecordsetName());
        dedup.setRecordset(meta.getRecordset());
        dedup.setRunLocal(meta.getRunLocal());
        dedup.setCondition(meta.getCondition());
        dedup.setIsAll(meta.getIsAll());
        dedup.setIsHash(meta.getIsHash());
        dedup.setKeep(meta.getKeep());
        dedup.setKeeper(meta.getKeeper());
        
        newRow[0] = input + dedup.ecl();
        
        putRow(data.outputRowMeta, newRow);
        
        logBasic("{Dataset Step} Output = " + newRow[0]);
        
        return false;
    }

    
    
    
    
    
    
    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLDedupStepMeta) smi;
        data = (ECLDedupStepData) sdi;
        super.setStepname(meta.getStepName());
        
        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLDedupStepMeta) smi;
        data = (ECLDedupStepData) sdi;

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
