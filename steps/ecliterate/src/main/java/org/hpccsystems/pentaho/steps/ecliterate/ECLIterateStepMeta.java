package org.hpccsystems.pentaho.steps.ecliterate;


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

public class ECLIterateStepMeta extends BaseStepMeta implements StepMetaInterface {
	private String stepName;
	private String outputField;

	private String transform = "";
    private String transformName = "";
    private String recordset = "";//Comma separated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private Boolean runLocal = false;
    
    private String record = "";
    private String recordName = "";
    private String recordsetName = "";
    
    private String returnType = "";
    
    private String recordsetNameIterate = "";
    
    private String transformCall = "";
    
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
    
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public String getTransformCall() {
        return transformCall;
    }

    public String getRecordsetNameIterate() {
        return recordsetNameIterate;
    }

    public void setRecordsetNameIterate(String recordsetNameIterate) {
        this.recordsetNameIterate = recordsetNameIterate;
    }

    public void setTransformCall(String transformCall) {
        this.transformCall = transformCall;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordsetName() {
        return recordsetName;
    }

    public void setRecordsetName(String recordsetName) {
        this.recordsetName = recordsetName;
    }


    public String getTransformName() {
        return transformName;
    }

    public void setTransformName(String transformName) {
        this.transformName = transformName;
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
        if(runLocal){
            return "true";
        }else{
            return "false";
        }
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
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
    	retval += "             <record>"+this.record+"</record>"+Const.CR;
        retval += "             <recordName>"+this.recordName+"</recordName>"+Const.CR;
        retval += "             <recordsetName>"+this.recordsetName+"</recordsetName>"+Const.CR;
        retval += "             <recordset_name_iterate>"+this.recordsetNameIterate+"</recordset_name_iterate>"+Const.CR;
        retval += "             <transformName>"+this.transformName+"</transformName>"+Const.CR;
        retval += "             <transform>"+this.transform+"</transform>"+Const.CR;
        retval += "             <transformCall>"+this.transformCall+"</transformCall>"+Const.CR;
        retval += "             <recordset>"+this.recordset+"</recordset>"+Const.CR;
        retval += "             <runLocal>"+this.getRunLocalString()+"</runLocal>"+Const.CR;
        retval += "             <returnType>"+this.getReturnType()+"</returnType>"+Const.CR;
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformName")) != null)
                 this.setTransformName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformName")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transform")) != null)
                 this.setTransform(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transform")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"record")) != null)
                 this.setRecord(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"record")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordName")) != null)
                 this.setRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordName")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordsetName")) != null)
                 this.setRecordsetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordsetName")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset_name_iterate")) != null)
                 this.setRecordsetNameIterate(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset_name_iterate")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformCall")) != null)
                 this.setTransformCall(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformCall")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"returnType")) != null)
                 this.setReturnType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"returnType")));
             
             this.setRecordset(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")));
             this.setRunLocalString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"runLocal")));
    		 
         } catch (Exception e) {
             throw new KettleXMLException("ECL Iterate Job Plugin Unable to read step info from XML node", e);
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
        return new ECLIterateStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLIterateStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLIterateStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		if(rep.getStepAttributeString(id_step, "record") != null)
                record = rep.getStepAttributeString(id_step, "record");
            if(rep.getStepAttributeString(id_step, "recordName") != null)
                recordName = rep.getStepAttributeString(id_step, "recordName");
            if(rep.getStepAttributeString(id_step, "recordsetName") != null)
                recordsetName = rep.getStepAttributeString(id_step, "recordsetName");
            if(rep.getStepAttributeString(id_step, "recordset_name_iterate") != null)
                recordsetNameIterate = rep.getStepAttributeString(id_step, "recordset_name_iterate");
            if(rep.getStepAttributeString(id_step, "transformName") != null)
                transformName = rep.getStepAttributeString(id_step, "transformName");
            if(rep.getStepAttributeString(id_step, "transform") != null)
                transform = rep.getStepAttributeString(id_step, "transform");
            if(rep.getStepAttributeString(id_step, "transformCall") != null)
                transformCall = rep.getStepAttributeString(id_step, "transformCall");
            if(rep.getStepAttributeString(id_step, "recordset") != null)
                recordset = rep.getStepAttributeString(id_step, "recordset");
            if(rep.getStepAttributeString(id_step, "runLocal") != null)
                this.setRunLocalString(rep.getStepAttributeString(id_step, "runLocal"));
            if(rep.getStepAttributeString(id_step, "returnType") != null)
                this.setReturnType(rep.getStepAttributeString(id_step, "returnType"));
    		
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "record", record);
            rep.saveStepAttribute(id_transformation, id_step, "recordName", recordName);
            rep.saveStepAttribute(id_transformation, id_step, "recordsetName", recordsetName);
            rep.saveStepAttribute(id_transformation, id_step, "recordset_name_iterate", recordsetNameIterate);
            rep.saveStepAttribute(id_transformation, id_step, "transformName", transformName);
            rep.saveStepAttribute(id_transformation, id_step, "transform", transform);
            rep.saveStepAttribute(id_transformation, id_step, "transformCall", transformCall);
            rep.saveStepAttribute(id_transformation, id_step, "recordset", recordset);
            rep.saveStepAttribute(id_transformation, id_step, "runLocal", this.getRunLocalString());
            rep.saveStepAttribute(id_transformation, id_step, "returnType", this.getReturnType());
    		
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
