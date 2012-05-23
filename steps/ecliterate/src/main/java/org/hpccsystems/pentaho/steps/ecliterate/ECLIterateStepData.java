package org.hpccsystems.pentaho.steps.ecliterate;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class ECLIterateStepData extends BaseStepData implements StepDataInterface {

    public String output;
    public RowMetaInterface outputRowMeta;

    public ECLIterateStepData(String output) {
        super();
        this.output = output;
    }

    public String toString() {
        return output;
    }
}