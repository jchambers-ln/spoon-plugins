package org.hpccsystems.pentaho.steps.ecldedup;


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

public class ECLDedupStepMeta extends BaseStepMeta implements StepMetaInterface {
	private String stepName;
	private String outputField;

	private String RecordsetName = "";
    private String recordset = "";//Comma separated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private String condition = "";
    private Boolean isAll = false;
    private Boolean isHash = false;
    private String keep = "";
    private String keeper = "";
    private Boolean runLocal = false;
  
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
        return RecordsetName;
    }

    public void setRecordsetName(String recordsetName) {
        this.RecordsetName = recordsetName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getIsAll() {
        return isAll;
    }

    public void setIsAll(Boolean isAll) {
        this.isAll = isAll;
    }

    public Boolean getIsHash() {
        return isHash;
    }

    public void setIsHash(Boolean isHash) {
        this.isHash = isHash;
    }

    public String getKeep() {
        return keep;
    }

    public void setKeep(String keep) {
        this.keep = keep;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }

    public String getRecordset() {
        return recordset;
    }

    public void setRecordset(String recordset) {
        this.recordset = recordset;
    }

    public Boolean getRunLocal() {
        return runLocal;
    }

    public void setRunLocal(Boolean runLocal) {
        this.runLocal = runLocal;
    }
    
public void setRunLocalString(String runLocal) {
        
        if(runLocal.equals("true")){
            this.runLocal = true;
        }else{
            this.runLocal = false;
        }
        
    }
    
    public String getRunLocalString() {
        if(this.runLocal != null && this.runLocal){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setIsHashString(String isHash) {
        
        if(isHash.equals("true")){
            this.isHash = true;
        }else{
            this.isHash = false;
        }
        
    }
    
    public String getIsHashString() {
        if(this.isHash != null && this.isHash){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setIsAllString(String isall) {
        
        if(isall.equals("true")){
            this.isAll = true;
        }else{
            this.isAll = false;
        }
        
    }
    
    public String getIsAllString() {
        if(this.isAll != null && this.isAll){
            return "true";
        }else{
            return "false";
        }
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
        retval += "             <recordset_name>"+this.RecordsetName+"</recordset_name>"+Const.CR;
        retval += "             <recordset>"+this.recordset+"</recordset>"+Const.CR;
        retval += "             <condition>"+this.condition+"</condition>"+Const.CR;
        retval += "             <runLocal>"+this.getRunLocalString()+"</runLocal>"+Const.CR;
        retval += "             <isAll>"+this.getIsAllString()+"</isAll>"+Const.CR;
        retval += "             <isHash>"+this.getIsHashString()+"</isHash>"+Const.CR;
        retval += "             <keep>"+this.getKeep()+"</keep>"+Const.CR;
        retval += "             <keeper>"+this.getKeeper()+"</keeper>"+Const.CR;
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset_name")) != null)
                 this.setRecordsetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")) != null)
                 this.setRecordset(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"condition")) != null)
                 this.setCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"condition")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isAll")) != null)
                 this.setIsAllString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isAll")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isHash")) != null)
                 this.setIsHashString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isHash")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keep")) != null)
                 this.setKeep(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keep")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keeper")) != null)
                 this.setKeeper(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keeper")));
             
             this.setRunLocalString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"runLocal")));

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
        return new ECLDedupStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLDedupStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLDedupStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		if(rep.getStepAttributeString(id_step, "recordset_name") != null)
                RecordsetName = rep.getStepAttributeString(id_step, "recordset_name");
            if(rep.getStepAttributeString(id_step, "recordset") != null)
                recordset = rep.getStepAttributeString(id_step, "recordset");
            if(rep.getStepAttributeString(id_step, "condition") != null)
                condition = rep.getStepAttributeString(id_step, "condition");
            if(rep.getStepAttributeString(id_step, "runLocal") != null)
            	this.setRunLocalString(rep.getStepAttributeString(id_step, "runLocal"));
            if(rep.getStepAttributeString(id_step, "isAll") != null)
            	this.setIsAllString(rep.getStepAttributeString(id_step, "isAll"));
            if(rep.getStepAttributeString(id_step, "isHash") != null)
            	this.setIsHashString(rep.getStepAttributeString(id_step, "isHash"));
            if(rep.getStepAttributeString(id_step, "keep") != null)
                keep = rep.getStepAttributeString(id_step, "keep");
            if(rep.getStepAttributeString(id_step, "keeper") != null)
                keeper = rep.getStepAttributeString(id_step, "keeper");
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
            
    		rep.saveStepAttribute(id_transformation, id_step, "recordset_name", RecordsetName);
            rep.saveStepAttribute(id_transformation, id_step, "recordset", recordset);
            rep.saveStepAttribute(id_transformation, id_step, "condition", condition);
            rep.saveStepAttribute(id_transformation, id_step, "runLocal", this.getRunLocalString());
            rep.saveStepAttribute(id_transformation, id_step, "isAll", this.getIsAllString());
            rep.saveStepAttribute(id_transformation, id_step, "isHash", this.getIsHashString());
            rep.saveStepAttribute(id_transformation, id_step, "keep", keep);
            rep.saveStepAttribute(id_transformation, id_step, "keeper", keeper);
    		
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
