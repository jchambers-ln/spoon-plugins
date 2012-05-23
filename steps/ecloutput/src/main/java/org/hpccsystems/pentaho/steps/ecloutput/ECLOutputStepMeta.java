package org.hpccsystems.pentaho.steps.ecloutput;


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

public class ECLOutputStepMeta extends BaseStepMeta implements StepMetaInterface {
	 private String stepName;
	 private String outputField;
	 
	 private String attributeName = "";
	  private String isDef = ""; //true set output into attr using job entry name
	    private String outputType = ""; //recordset,expression
	    private String includeFormat = ""; //yes/no
	    private String inputType = ""; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
	    private String outputFormat = "";
	    private String expression = "";
	    private String file = "";
	    private String typeOptions = ""; // used for CSV, XML, Pipe
	    private String fileOptions = ""; // used for CSV, XML
	    private String named = ""; //used for named
	    private String extend = ""; // availiable for Named
	    private String returnAll = ""; // availiable for Named
	    private String thor = ""; // used in 
	    private String cluster = "";
	    private String encrypt = "";
	    private String compressed = "";
	    private String overwrite = "";
	    private String expire = "";
	    private String repeat = "";// piped
	    private String pipeType = "";

	    public void setStepName(String stepName) {
	        this.stepName = stepName;
	    }
		public String getStepName() {
		        return stepName;
		}
		public String getOutputField() {
	        return outputField;
	    }

	    public void setOutputField(String outputField) {
	        this.outputField = outputField;
	    }
	    

	    public String getAttributeName() {
	        return attributeName;
	    }
	    public void setAttributeName(String attributeName) {
	        this.attributeName = attributeName;
	    }
	    public String getCluster() {
	        return cluster;
	    }

	    public void setCluster(String cluster) {
	        this.cluster = cluster;
	    }

	    public String getCompressed() {
	        return compressed;
	    }

	    public void setCompressed(String compressed) {
	        this.compressed = compressed;
	    }

	    public String getEncrypt() {
	        return encrypt;
	    }

	    public void setEncrypt(String encrypt) {
	        this.encrypt = encrypt;
	    }

	    public String getExpire() {
	        return expire;
	    }

	    public void setExpire(String expire) {
	        this.expire = expire;
	    }

	    public String getExpression() {
	        return expression;
	    }

	    public void setExpression(String expression) {
	        this.expression = expression;
	    }

	    public String getExtend() {
	        return extend;
	    }

	    public void setExtend(String extend) {
	        this.extend = extend;
	    }

	    public String getFile() {
	        return file;
	    }

	    public void setFile(String file) {
	        this.file = file;
	    }

	    public String getFileOptions() {
	        return fileOptions;
	    }

	    public void setFileOptions(String fileOptions) {
	        this.fileOptions = fileOptions;
	    }

	    public String getOutputFormat() {
	        return outputFormat;
	    }

	    public void setOutputFormat(String format) {
	        this.outputFormat = format;
	    }

	    public String getIncludeFormat() {
	        return includeFormat;
	    }

	    public void setIncludeFormat(String includeFormat) {
	        this.includeFormat = includeFormat;
	    }

	    public String getIsDef() {
	        System.out.println("getIsDef() = " + isDef);
	        return isDef;
	    }

	    public void setIsDef(String isDef) {
	        this.isDef = isDef;
	    }

	    public String getTypeOptions() {
	        return typeOptions;
	    }

	    public void setTypeOptions(String options) {
	        this.typeOptions = options;
	    }

	    public String getOutputType() {
	        return outputType;
	    }

	    public void setOutputType(String outputType) {
	        this.outputType = outputType;
	    }

	    public String getOverwrite() {
	        return overwrite;
	    }

	    public void setOverwrite(String overwrite) {
	        this.overwrite = overwrite;
	    }

	    public String getPipeType() {
	        return pipeType;
	    }

	    public void setPipeType(String pipeType) {
	        this.pipeType = pipeType;
	    }

	    public String getRepeat() {
	        return repeat;
	    }

	    public void setRepeat(String repeat) {
	        this.repeat = repeat;
	    }

	    public String getReturnAll() {
	        return returnAll;
	    }

	    public void setReturnAll(String returnAll) {
	        this.returnAll = returnAll;
	    }

	    public String getThor() {
	        return thor;
	    }

	    public void setThor(String thor) {
	        this.thor = thor;
	    }

	    public String getInputType() {
	        return inputType;
	    }

	    public void setInputType(String inputType) {
	        this.inputType = inputType;
	    }

	    public String getNamed() {
	        System.out.println("getNamed() - " + named + "}");
	        return named;
	    }

	    public void setNamed(String named) {
	        this.named = named;
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
    
    public String loadXMLElement(Node node,String key){
        String rStr = "";
        if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key)) != null){
                rStr = XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key));
                //System.out.println("load XML Element " + key + " = " + XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key)) + "|");
        }
        return rStr;
    }

    public String getXMLElement(String key, String value){
        return "        <" + key + ">"+value+"</"+key+">"+ Const.CR;
    }
    
    public String getXML() throws KettleValueException {
    	String retval = "";
    
    	retval += "		<stepName>" + stepName + "</stepName>" + Const.CR;
    	retval += "		<outputfield>" + outputField + "</outputfield>" + Const.CR;
        retval += "		<attributeName>" + attributeName + "</attributeName>" + Const.CR;
            //private String isDef; //true set output into attr using job entry name
    
        retval += getXMLElement("isDef", this.getIsDef());
            //private String outputType; //recordset,expression
         retval += getXMLElement("outputType", this.getOutputType());
            //private String includeFormat; //yes/no
         retval += getXMLElement("includeFormat", this.getIncludeFormat());
            //private String type; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
         retval += getXMLElement("inputType", this.getInputType());
            //private String format;
         retval += getXMLElement("outputFormat", this.getOutputFormat());
            //private String expression;
         retval += getXMLElement("expression", this.getExpression());
            //private String file;
         retval += getXMLElement("file", this.getFile());
            //private String options; // used for CSV, XML, Pipe
         retval += getXMLElement("typeOptions", this.getTypeOptions());
            //private String fileOptions; // used for CSV, XML
         retval += getXMLElement("fileOptions", this.getFileOptions());
            //private String name; //used for named
         retval += getXMLElement("named", this.getNamed());
            //private String extend; // availiable for Named
         retval += getXMLElement("extend", this.getExtend());
            //private String returnAll; // availiable for Named
         retval += getXMLElement("returnAll", this.getReturnAll());
            //private String thor; // used in 
         retval += getXMLElement("thor", this.getThor());
            //private String cluster;
         retval += getXMLElement("cluster", this.getCluster());
            //private String encrypt;
         retval += getXMLElement("encrypt", this.getEncrypt());
            //private String compressed;
         retval += getXMLElement("compressed", this.getCompressed());
            //private String overwrite;
         retval += getXMLElement("overwrite", this.getOverwrite());
            //private String expire;
         retval += getXMLElement("expire", this.getExpire());
            //private String repeat;// piped
         retval += getXMLElement("repeat", this.getRepeat());
            //private String pipeType;
        retval += getXMLElement("pipeType", this.pipeType);

        return retval;

    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	try {
            //System.out.println(" ------------ loadXML ------------- ");
           //super.loadXML(node, list, list1);
    		if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
   			 setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
    		 setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));

           if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "attributeName")) != null)
               setAttributeName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "attributeName")));
           
               //private String isDef; //true set output into attr using job entry name
           this.setIsDef(loadXMLElement(node,"isDef"));

           //private String outputType; //recordset,expression
           this.setOutputType(loadXMLElement(node,"outputType"));
               //private String includeFormat; //yes/no
           this.setIncludeFormat(loadXMLElement(node,"includeFormat"));
               //private String type; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
           this.setInputType(loadXMLElement(node,"inputType"));
               //private String format;
           this.setOutputFormat(loadXMLElement(node,"outputFormat"));
               //private String expression;
           this.setExpression(loadXMLElement(node,"expression"));
               //private String file;
           this.setFile(loadXMLElement(node,"file"));
               //private String options; // used for CSV, XML, Pipe
           this.setTypeOptions(loadXMLElement(node,"typeOptions"));
               //private String fileOptions; // used for CSV, XML
           this.setFileOptions(loadXMLElement(node,"fileOptions"));
               //private String name; //used for named
           this.setNamed(loadXMLElement(node,"named"));
               //private String extend; // availiable for Named
           this.setExtend(loadXMLElement(node,"extend"));
               //private String returnAll; // availiable for Named
           this.setReturnAll(loadXMLElement(node,"returnAll"));
               //private String thor; // used in 
           this.setThor(loadXMLElement(node,"thor"));
               //private String cluster;
           this.setCluster(loadXMLElement(node,"cluster"));
               //private String encrypt;
           this.setEncrypt(loadXMLElement(node,"encrypt"));
               //private String compressed;
           this.setCompressed(loadXMLElement(node,"compressed"));
               //private String overwrite;
           this.setOverwrite(loadXMLElement(node,"overwrite"));
               //private String expire;
           this.setExpire(loadXMLElement(node,"expire"));
               //private String repeat;// piped
           this.setRepeat(loadXMLElement(node,"repeat"));
               //private String pipeType;
           this.setPipeType(loadXMLElement(node,"pipeType"));
       
           //if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "server_address")) != null)
               //setServerAddress(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "server_address")));
          // if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_name")) != null)
          //     setFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_name")));


       } catch (Exception e) {
           e.printStackTrace();
           throw new KettleXMLException("ECL Output Job Plugin Unable to read step info from XML node", e);
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
        return new ECLOutputStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLOutputStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLOutputStepData(outputField);
    	
    }
    public String getRepElement(Repository rep, ObjectId id, String key) throws KettleException{
        String rStr = "";
        try{
        if(rep.getStepAttributeString(id, key) != null)
                rStr = rep.getStepAttributeString(id, key); //$NON-NLS-1$
        }catch (Exception e){
             e.printStackTrace();
            throw new KettleException("Unexpected Exception", e);
        }
        return rStr;
    }
    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$
    		
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
    			outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_step, "attributeName") != null)
                attributeName = rep.getStepAttributeString(id_step, "attributeName"); //$NON-NLS-1$
            
        } catch (Exception e) {
             e.printStackTrace();
            throw new KettleException("Unexpected Exception", e);
            
        }
        
        //private String isDef; //true set output into attr using job entry name
        this.setIsDef(getRepElement(rep, id_step, "isDef"));
            //private String outputType; //recordset,expression
        this.setOutputType(getRepElement(rep, id_step, "outputType"));
            //private String includeFormat; //yes/no
        this.setIncludeFormat (getRepElement(rep, id_step, "includeFormat"));
            //private String type; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
        this.setInputType(getRepElement(rep, id_step, "inputType"));
            //private String format;
        this.setOutputFormat(getRepElement(rep, id_step, "outputFormat"));
            //private String expression;
        this.setExpression(getRepElement(rep, id_step, "expression"));
            //private String file;
        this.setFile(getRepElement(rep, id_step, "file"));
            //private String options; // used for CSV, XML, Pipe
        this.setTypeOptions(getRepElement(rep, id_step, "typeOptions"));
            //private String fileOptions; // used for CSV, XML
        this.setFileOptions(getRepElement(rep, id_step, "fileOptions"));
            //private String name; //used for named
        this.setNamed(getRepElement(rep, id_step, "named"));
            //private String extend; // availiable for Named
        this.setExtend(getRepElement(rep, id_step, "extend"));
            //private String returnAll; // availiable for Named
        this.setReturnAll(getRepElement(rep, id_step, "returnAll"));
            //private String thor; // used in 
        this.setThor(getRepElement(rep, id_step, "thor"));
            //private String cluster;
        this.setCluster(getRepElement(rep, id_step, "cluster"));
            //private String encrypt;
        this.setEncrypt(getRepElement(rep, id_step, "encrypt"));
            //private String compressed;
        this.setCompressed(getRepElement(rep, id_step, "compressed"));
            //private String overwrite;
        this.setOverwrite(getRepElement(rep, id_step, "overwrite"));
            //private String expire;
        this.setExpire(getRepElement(rep, id_step, "expire"));
            //private String repeat;// piped
        this.setRepeat(getRepElement(rep, id_step, "repeat"));
            //private String pipeType;
        this.setPipeType(getRepElement(rep, id_step, "pipeType"));
    }

    public void saveRepElement(Repository rep, ObjectId id_job,String key,String value) throws KettleException{
        try{
        	
            rep.saveStepAttribute(id_job, getObjectId(), key, value); //$NON-NLS-1$
        }catch (Exception e){
             e.printStackTrace();
             throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }
    
    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	 try {
             
             ObjectId[] allIDs = rep.getPartitionSchemaIDs(true);
             for(int i = 0; i<allIDs.length; i++){
                 logBasic("ObjectID["+i+"] = " + allIDs[i]);
               //  System.out.println("ObjectID["+i+"] = " + allIDs[i]);
             }
             rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
             rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
             rep.saveStepAttribute(id_step, getObjectId(), "attributeName", attributeName); //$NON-NLS-1$
             
             //private String isDef; //true set output into attr using job entry name
             saveRepElement(rep,id_step,"isDef",this.getIsDef());
             //private String outputType; //recordset,expression
             saveRepElement(rep,id_step,"outputType",this.getOutputType());
             //private String includeFormat; //yes/no
             saveRepElement(rep,id_step,"includeFormat",this.getIncludeFormat());
             //private String type; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
             saveRepElement(rep,id_step,"inputType",this.getInputType());
             //private String format;
             saveRepElement(rep,id_step,"outputFormat",this.getOutputFormat());
             //private String expression;
             saveRepElement(rep,id_step,"expression",this.getExpression());
             //private String file;
             saveRepElement(rep,id_step,"file",this.getFile());
             //private String options; // used for CSV, XML, Pipe
             saveRepElement(rep,id_step,"typeOptions",this.getTypeOptions());
             //private String fileOptions; // used for CSV, XML
             saveRepElement(rep,id_step,"fileOptions",this.getFileOptions());
             //private String name; //used for named
             saveRepElement(rep,id_step,"named",this.getNamed());
             //private String extend; // availiable for Named
             saveRepElement(rep,id_step,"extend",this.getExtend());
             //private String returnAll; // availiable for Named
             saveRepElement(rep,id_step,"returnALl",this.getReturnAll());
             //private String thor; // used in 
             saveRepElement(rep,id_step,"thor",this.getThor());
             //private String cluster;
             saveRepElement(rep,id_step,"cluster",this.getCluster());
             //private String encrypt;
             saveRepElement(rep,id_step,"encrypt",this.getEncrypt());
             //private String compressed;
             saveRepElement(rep,id_step,"compressed",this.getCompressed());
             //private String overwrite;
             saveRepElement(rep,id_step,"overwrite",this.getOverwrite());
             //private String expire;
             saveRepElement(rep,id_step,"expire",this.getExpire());
             //private String repeat;// piped
             saveRepElement(rep,id_step,"repeat",this.getRepeat());
             //private String pipeType;
             saveRepElement(rep,id_step,"pipeType",this.getPipeType());
             
            
         } catch (Exception e) {
              e.printStackTrace();
             throw new KettleException("Unable to save info into repository" + id_step, e);
         }
    }


}
