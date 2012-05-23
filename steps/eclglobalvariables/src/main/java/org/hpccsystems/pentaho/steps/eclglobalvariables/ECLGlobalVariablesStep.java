package org.hpccsystems.pentaho.steps.eclglobalvariables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Dataset;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLGlobalVariablesStep extends BaseStep implements StepInterface {

    private ECLGlobalVariablesStepData data;
    private ECLGlobalVariablesStepMeta meta;

    public ECLGlobalVariablesStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (ECLGlobalVariablesStepMeta) smi;
        data = (ECLGlobalVariablesStepData) sdi; 
        //thid one doesn't do anything
        return false;
    }

    
    
    
    
    
    
    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLGlobalVariablesStepMeta) smi;
        data = (ECLGlobalVariablesStepData) sdi;
        super.setStepname(meta.getStepName());
        
        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLGlobalVariablesStepMeta) smi;
        data = (ECLGlobalVariablesStepData) sdi;

        super.dispose(smi, sdi);
    }

}
