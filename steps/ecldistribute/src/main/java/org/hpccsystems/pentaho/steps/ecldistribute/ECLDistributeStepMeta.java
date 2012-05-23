package org.hpccsystems.pentaho.steps.ecldistribute;


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

public class ECLDistributeStepMeta extends BaseStepMeta implements StepMetaInterface {
	
	private String stepName;
	private String outputField;

    private String recordsetName = "";
    private String datasetName = "";
    private String expression = "";
    private String index = "";
    private String joinCondition = "";
    private String skew = "";
  
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
    
    public String getRecordsetName() {
        return recordsetName;
    }

    public void setRecordsetName(String recordsetName) {
        this.recordsetName = recordsetName;
    }
    
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    
    public String getExpression(){
        return expression;
    }
    public void setExpression(String expression){
        this.expression = expression;
    }
    
    public String getIndex(){
        return index;
    }
    public void setIndex(String index){
        this.index=index;
    }
    
    public String getJoinCondition(){
        return this.joinCondition;
    }
    
    public void setJoinCondition(String jc){
        this.joinCondition = jc;
    }
    
    public String getSkew(){
        return this.skew;
    }
    public void setSkew(String skew){
        this.skew = skew;
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
        retval += "		<recordset_name>" + recordsetName + "</recordset_name>" + Const.CR;
        retval += "		<dataset_name>" + datasetName + "</dataset_name>" + Const.CR;
        retval += "		<expression>" + expression + "</expression>" + Const.CR;
        retval += "		<index>" + index + "</index>" + Const.CR;
        retval += "		<join_condition>" + joinCondition + "</join_condition>" + Const.CR;
        retval += "		<skew>" + skew + "</skew>" + Const.CR;
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset_name")) != null)
                 setRecordsetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                 setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "expression")) != null)
                 setExpression(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "expression")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "index")) != null)
                 setIndex(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "index")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_condition")) != null)
                 setJoinCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_condition")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "skew")) != null)
                 setSkew(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "skew")));

         } catch (Exception e) {
             throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
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
        return new ECLDistributeStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLDistributeStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLDistributeStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		if(rep.getStepAttributeString(id_step, "recordsetName") != null)
                recordsetName = rep.getStepAttributeString(id_step, "recordsetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_step, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "expression") != null)
                expression = rep.getStepAttributeString(id_step, "expression"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "index") != null)
                index = rep.getStepAttributeString(id_step, "index"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "joinCondition") != null)
                joinCondition = rep.getStepAttributeString(id_step, "joinCondition"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "skew") != null)
                skew = rep.getStepAttributeString(id_step, "skew"); //$NON-NLS-1$
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "recordsetName", recordsetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "expression", expression); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "index", index); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "joinCondition", joinCondition); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "skew", skew); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
