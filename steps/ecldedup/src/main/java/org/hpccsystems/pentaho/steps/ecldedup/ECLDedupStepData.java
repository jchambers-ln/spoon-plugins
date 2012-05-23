package org.hpccsystems.pentaho.steps.ecldedup;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class ECLDedupStepData extends BaseStepData implements StepDataInterface {

    public String output;
    public RowMetaInterface outputRowMeta;

    public ECLDedupStepData(String output) {
        super();
        this.output = output;
    }

    public String toString() {
        return output;
    }
}