package org.hpccsystems.pentaho.steps.ecljoin;


import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hpccsystems.eclguifeatures.CreateTable;
import org.hpccsystems.eclguifeatures.RecordBO;
import org.hpccsystems.eclguifeatures.RecordList;

public class ECLJoinStepMeta extends BaseStepMeta implements StepMetaInterface {
	
	private String stepName;
	private String outputField;
	
	private String joinName = "";
	private String joinCondition = "";
	private String leftJoinCondition = "";
	private String rightJoinCondition = "";
	private String joinType = "";
	private String leftRecordSet = "";
	private String rightRecordSet = "";
	private String joinRecordSet = "";
	 
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    
    public String getOutputField() {
        return outputField;
    }

    public void setOutputField(String outputField) {
        this.outputField = outputField;
    }
    
    public String getJoinName() {
        return joinName;
    }

    public void setJoinName(String joinName) {
        this.joinName = joinName;
    }

    public String getLeftJoinCondition() {
        return leftJoinCondition;
    }

    public void setLeftJoinCondition(String leftJoinCondition) {
        this.leftJoinCondition = leftJoinCondition;
    }

    public String getRightJoinCondition() {
        return rightJoinCondition;
    }

    public void setRightJoinCondition(String rightJoinCondition) {
        this.rightJoinCondition = rightJoinCondition;
    }

    public String getName() {
        return this.joinName;
    }

    public void setName(String joinName) {
        this.joinName = joinName;
    }

    public String getJoinCondition() {
        return this.joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }
    
    public String getJoinType() {
        return this.joinType;
    }
    
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getLeftRecordSet() {
        return this.leftRecordSet;
    }

    public void setLeftRecordSet(String leftRecordSet) {
        this.leftRecordSet = leftRecordSet;
    }

    public String getRightRecordSet() {
        return this.rightRecordSet;
    }

    public void setRightRecordSet(String rightRecordSet) {
        this.rightRecordSet = rightRecordSet;
    }
    
    public String getJoinRecordSet() {
        return this.joinRecordSet;
    }

    public void setJoinRecordSet(String joinRecordSet) {
        this.joinRecordSet = joinRecordSet;
    }

    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {
		// append the outputField to the output
		ValueMetaInterface v = new ValueMeta();
		v.setName("outputfield");
		v.setType(ValueMeta.TYPE_STRING);
		v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		v.setOrigin(origin);

		r.addValueMeta(v);
        
    }

    public String getXML() throws KettleValueException {
		String retval = "";

		retval += "		<stepName>" + stepName + "</stepName>" + Const.CR;
		retval += "		<outputfield>" + outputField + "</outputfield>" + Const.CR;
		retval += "		<join_name>" + this.joinName + "</join_name>" + Const.CR;
        retval += "		<join_condition>" + this.joinCondition + "</join_condition>" + Const.CR;
        retval += "		<left_join_condition>" + this.leftJoinCondition + "</left_join_condition>" + Const.CR;
        retval += "		<right_join_condition>" + this.rightJoinCondition + "</right_join_condition>" + Const.CR;
        retval += "		<join_type>" + this.joinType + "</join_type>" + Const.CR;
        retval += "		<left_recordset>" + this.leftRecordSet + "</left_recordset>" + Const.CR;
        retval += "		<right_recordset>" + this.rightRecordSet + "</right_recordset>" + Const.CR;
        retval += "		<record_name>" + this.joinRecordSet + "</record_name>" + Const.CR;

		return retval;
    }

    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
    		  	setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_name")) != null)
                 setName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_condition")) != null)
                 setJoinCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_condition")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "left_join_condition")) != null)
                 setLeftJoinCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "left_join_condition")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "right_join_condition")) != null)
                 setRightJoinCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "right_join_condition")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_type")) != null)
                 setJoinType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_type")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "left_recordset")) != null)
                 setLeftRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "left_recordset")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "right_recordset")) != null)
                 setRightRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "right_recordset")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")) != null)
                 setJoinRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")));

         } catch (Exception e) {
             throw new KettleXMLException("ECL Join Step Plugin Unable to read step info from XML node", e);
         }

    }
    
    public Object clone() {
		Object retval = super.clone();
		return retval;
    }
    
    public void setDefault() {
       outputField = "template_outfield";
    }

    public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
        CheckResult cr;

        // See if we have input streams leading to this step!
        if (input.length > 0) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
            remarks.add(cr);
        }

    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new ECLJoinStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLJoinStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLJoinStepData(outputField);
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
		try {
			if (rep.getStepAttributeString(id_step, "stepName") != null)
				stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
			if (rep.getStepAttributeString(id_step, "outputField") != null)
				outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
			if(rep.getStepAttributeString(id_step, "joinName") != null)
                joinName = rep.getStepAttributeString(id_step, "joinName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "joinCondition") != null)
                joinCondition = rep.getStepAttributeString(id_step, "joinCondition"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "leftJoinCondition") != null)
                leftJoinCondition = rep.getStepAttributeString(id_step, "leftJoinCondition"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "rightJoinCondition") != null)
                rightJoinCondition = rep.getStepAttributeString(id_step, "rightJoinCondition"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "joinType") != null)
                joinType = rep.getStepAttributeString(id_step, "joinType"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "leftRecordSet") != null)
                leftRecordSet = rep.getStepAttributeString(id_step, "leftRecordSet"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "rightRecordSet") != null)
                rightRecordSet = rep.getStepAttributeString(id_step, "rightRecordSet"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "record_name") != null)
                joinRecordSet = rep.getStepAttributeString(id_step, "record_name"); //$NON-NLS-1$

		} catch (Exception e) {
			throw new KettleException("Unexpected Exception", e);
		}
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "joinName", joinName); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "joinCondition", joinCondition); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "leftJoinCondition", leftJoinCondition); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "rightJoinCondition", rightJoinCondition); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "joinType", joinType); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "leftRecordSet", leftRecordSet); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "rightRecordSet", rightRecordSet); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "record_name", joinRecordSet); //$NON-NLS-1$

		} catch (Exception e) {
			throw new KettleException("Unable to save info into repository" + id_step, e);
		}
    }


}
