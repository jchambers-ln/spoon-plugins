package org.hpccsystems.pentaho.steps.ecldataset;


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

public class ECLDatasetStepMeta extends BaseStepMeta implements StepMetaInterface {
	 private String stepName;
	 private String outputField;
	 
	private String logicalFileName = "";
    private String datasetName = "";
    private String recordName = "";
    private String recordDef = "";
    private String recordSet = "";
    private String fileType = "";
    
    private RecordList recordList = new RecordList();

    public RecordList getRecordList() {
        return recordList;
    }

    public void setRecordList(RecordList recordList) {
        this.recordList = recordList;
    }
    
    
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

    public String getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(String recordSet) {
        this.recordSet = recordSet;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String fileName) {
        this.logicalFileName = fileName;
    }

    public String getRecordDef() {
        return recordDef;
    }

    public void setRecordDef(String recordDef) {
        this.recordDef = recordDef;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    

    public String saveRecordList(){
        String out = "";
        ArrayList list = recordList.getRecords();
        Iterator<RecordBO> itr = list.iterator();
        boolean isFirst = true;
        while(itr.hasNext()){
            if(!isFirst){out+="|";}
            
            out += itr.next().toCSV();
            isFirst = false;
        }
        return out;
    }
    
    public void openRecordList(String in){
        String[] strLine = in.split("[|]");
        
        int len = strLine.length;
        if(len>0){
            recordList = new RecordList();
            System.out.println("Open Record List");
            for(int i =0; i<len; i++){
                System.out.println("++++++++++++" + strLine[i]);
                //this.recordDef.addRecord(new RecordBO(strLine[i]));
                RecordBO rb = new RecordBO(strLine[i]);
                System.out.println(rb.getColumnName());
                recordList.addRecordBO(rb);
            }
        }
    }
    public String resultListToString(){
        String out = "";
        
        if(recordList != null){
            if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
                    System.out.println("Size: "+recordList.getRecords().size());
                    for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO record = (RecordBO) iterator.next();
                            int rLen = record.getColumnWidth();
                            if(rLen != 0){
                                if(record.getColumnName() != null && !record.getColumnName().equals("")){
                                    out += record.getColumnType()+rLen + " " + record.getColumnName();
                                    if(record.getDefaultValue() != ""){
                                        out += " := " + record.getDefaultValue();
                                    }
                                    out += ";\r\n";
                                 }
                            }else{
                                if(record.getColumnName() != null && !record.getColumnName().equals("")){
                                    out += record.getColumnType() + " " + record.getColumnName();
                                    if(record.getDefaultValue() != ""){
                                        out += " := " + record.getDefaultValue();
                                    }
                                    out += ";\r\n";
                                }
                            }
                            
                            
                    }
            }
        }
        
        return out;
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
    	
        retval += "		<logical_file_name>" + logicalFileName + "</logical_file_name>" + Const.CR;
        retval += "		<record_name isDef=\"true\">" + recordName + "</record_name>" + Const.CR;
        retval += "		<dataset_name isDef=\"true\">" + datasetName + "</dataset_name>" + Const.CR;
        retval += "		<record_def>" + recordDef + "</record_def>" + Const.CR;
        retval += "		<recordSet>" + recordSet + "</recordSet>" + Const.CR;
        retval += "		<recordList>" + this.saveRecordList() + "</recordList>" + Const.CR;
        retval += "		<fileType>" + fileType + "</fileType>" + Const.CR;
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			 setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
       		  setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")) != null)
                 setLogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")) != null)
                 setRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                 setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_def")) != null)
                 setRecordDef(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_def")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordSet")) != null)
                 setRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordSet")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")) != null)
                 openRecordList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fileTYpe")) != null)
                 setFileType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fileType")));

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
        return new ECLDatasetStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLDatasetStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLDatasetStepData(outputField);
    	//return null;
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "logicalFileName") != null)
                logicalFileName = rep.getStepAttributeString(id_step, "logicalFileName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_step, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "recordName") != null)
                recordName = rep.getStepAttributeString(id_step, "recordName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "recordDef") != null)
                recordDef = rep.getStepAttributeString(id_step, "recordDef"); //$NON-NLS-1$
            
             if(rep.getStepAttributeString(id_step, "recordSet") != null)
                recordSet = rep.getStepAttributeString(id_step, "recordSet"); //$NON-NLS-1$
             
             if(rep.getStepAttributeString(id_step, "recordList") != null)
                this.openRecordList(rep.getStepAttributeString(id_step, "recordList")); //$NON-NLS-1$
             
             if(rep.getStepAttributeString(id_step, "fileType") != null)
                fileType = rep.getStepAttributeString(id_step, "fileType"); //$NON-NLS-1$
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "recordName", recordName); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "recordDef", recordDef); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "recordSet", recordSet); //$NON-NLS-1$
            
            rep.saveStepAttribute(id_step, getObjectId(), "recordList", this.saveRecordList()); //$NON-NLS-1$
            rep.saveStepAttribute(id_step, getObjectId(), "fileType", fileType); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
