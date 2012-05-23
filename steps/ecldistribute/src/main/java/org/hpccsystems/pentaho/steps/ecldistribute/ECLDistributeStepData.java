package org.hpccsystems.pentaho.steps.ecldistribute;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class ECLDistributeStepData extends BaseStepData implements StepDataInterface {

    public String output;
    public RowMetaInterface outputRowMeta;

    public ECLDistributeStepData(String output) {
        super();
        this.output = output;
    }

    public String toString() {
        return output;
    }
}