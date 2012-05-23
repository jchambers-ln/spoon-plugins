package org.hpccsystems.pentaho.steps.ecloutput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Dataset;
import org.hpccsystems.ecldirect.Output;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLOutputStep extends BaseStep implements StepInterface {

    private ECLOutputStepData data;
    private ECLOutputStepMeta meta;

    public ECLOutputStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (ECLOutputStepMeta) smi;
        data = (ECLOutputStepData) sdi;
        
        
        Object[] r = getRow(); 
        String input = "";
        if (r == null) 
        {
        } else {
            logBasic("Found Row = " + r[r.length-1]);
            input = r[r.length-1].toString() + "\r\n";
        }
        
        Object[] newRow = new Object[1]; 
        
        Output op = new Output();
        op.setAttributeName(meta.getAttributeName());
        op.setIsDef(meta.getIsDef());
        op.setOutputType(meta.getOutputType());
        op.setIncludeFormat(meta.getIncludeFormat());
        op.setInputType(meta.getInputType());
        op.setOutputFormat(meta.getOutputFormat());
        op.setExpression(meta.getExpression());
        op.setFile(meta.getFile());
        op.setTypeOptions(meta.getTypeOptions());
        op.setFileOptions(meta.getFileOptions());
        op.setNamed(meta.getNamed());
        op.setExtend(meta.getExtend());
        op.setReturnAll(meta.getReturnAll());
        op.setThor(meta.getThor());
        op.setCluster(meta.getCluster());
        op.setEncrypt(meta.getEncrypt());
        op.setCompressed(meta.getCompressed());
        op.setOverwrite(meta.getOverwrite());
        op.setExpire(meta.getExpire());
        op.setRepeat(meta.getRepeat());
        op.setPipeType(meta.getPipeType());
        op.setName(meta.getName());
        newRow[0] = input + op.ecl();
        
        putRow(data.outputRowMeta, newRow);
        
        logBasic("{Dataset Step} Output = " + newRow[0]);
        
        return false;
    }

    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLOutputStepMeta) smi;
        data = (ECLOutputStepData) sdi;
        super.setStepname(meta.getStepName());
        
        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLOutputStepMeta) smi;
        data = (ECLOutputStepData) sdi;

        super.dispose(smi, sdi);
    }

   
}
