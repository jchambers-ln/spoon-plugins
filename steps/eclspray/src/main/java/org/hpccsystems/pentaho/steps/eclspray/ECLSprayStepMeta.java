package org.hpccsystems.pentaho.steps.eclspray;


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

public class ECLSprayStepMeta extends BaseStepMeta implements StepMetaInterface {
	 private String stepName;
	 private String outputField;
	 
	 private String filePath = "";
	 private String logicalFileName = "";
	 private String fileType = "";
	 private String csvSeparator = "";
	 private String csvTerminator = "";
	 private String csvQuote = "";
	 private String fixedRecordSize = "";

    
  
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
    

    public String getCsvQuote() {
        return csvQuote;
    }

    public void setCsvQuote(String csvQuote) {
        this.csvQuote = csvQuote;
    }

    public String getCsvSeparator() {
        return csvSeparator;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    public String getCsvTerminator() {
        return csvTerminator;
    }

    public void setCsvTerminator(String csvTerminator) {
        this.csvTerminator = csvTerminator;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String logicalFileName) {
        this.logicalFileName = logicalFileName;
    }
    
    public String getFixedRecordSize() {
        return fixedRecordSize;
    }

    public void setFixedRecordSize(String fixedRecordSize) {
        this.fixedRecordSize = fixedRecordSize;
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
        
    	retval += "		<file_path>" + filePath + "</file_path>" + Const.CR;
        retval += "		<file_type>" + fileType + "</file_type>" + Const.CR;
        retval += "		<csv_separator>" + csvSeparator + "</csv_separator>" + Const.CR;
        retval += "		<csv_terminator>" + csvTerminator + "</csv_terminator>" + Const.CR;
        retval += "		<csv_quote>" + csvQuote + "</csv_quote>" + Const.CR;
        retval += "		<csv_quote>" + csvQuote + "</csv_quote>" + Const.CR;
        retval += "		<fixed_record_size>" + fixedRecordSize + "</fixed_record_size>" + Const.CR;
        retval += "		<logical_file_name>" + logicalFileName + "</logical_file_name>" + Const.CR;
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_path")) != null)
                 setFilePath(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_path")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")) != null)
                 setLogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_type")) != null)
                 setFileType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_type")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_separator")) != null)
                 setCsvSeparator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_separator")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_terminator")) != null)
                 setCsvTerminator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_terminator")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_quote")) != null)
                 setCsvQuote(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_quote")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fixed_record_size")) != null)
                 setFixedRecordSize(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fixed_record_size")));  
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
        return new ECLSprayStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLSprayStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLSprayStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		
    		if(rep.getStepAttributeString(id_step, "filePath") != null)
                filePath = rep.getStepAttributeString(id_step, "filePath"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "fileType") != null)
                fileType = rep.getStepAttributeString(id_step, "fileType"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "csvSeparator") != null)
                csvSeparator = rep.getStepAttributeString(id_step, "csvSeparator"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "csvTerminator") != null)
                csvTerminator = rep.getStepAttributeString(id_step, "csvTerminator"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "csvQuote") != null)
                csvQuote = rep.getStepAttributeString(id_step, "csvQuote"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "fixedRecordSize") != null)
                fixedRecordSize = rep.getStepAttributeString(id_step, "fixedRecordSize"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "logicalFileName") != null)
                logicalFileName = rep.getStepAttributeString(id_step, "logicalFileName"); //$NON-NLS-1$
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		rep.saveStepAttribute(id_step, getObjectId(), "filePath", filePath); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "fileType", fileType); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "csvSeparator", csvSeparator); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "csvTerminator", csvTerminator); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "csvQuote", csvQuote); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "fixedRecordSize", fixedRecordSize); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
