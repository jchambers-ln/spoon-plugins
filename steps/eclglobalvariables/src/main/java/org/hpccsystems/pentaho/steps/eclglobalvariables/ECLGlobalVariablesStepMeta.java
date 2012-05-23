package org.hpccsystems.pentaho.steps.eclglobalvariables;


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

public class ECLGlobalVariablesStepMeta extends BaseStepMeta implements StepMetaInterface {
	private String outputField;
	private String stepName; 
	private String serverIP = "";
	private String serverPort = "";	
	private String landingZone = "";
	private String mlPath = "";
	private String eclccInstallDir = "C:\\Program Files\\HPCC Systems\\HPCC\\bin\\ver_3_0\\";
	private String jobName = "Spoon-job";
	private String cluster = "hthor";
	private boolean includeML = false;
    
  
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

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getLandingZone() {
        return landingZone;
    }

    public void setLandingZone(String landingZone) {
        this.landingZone = landingZone;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getEclccInstallDir() {
        return eclccInstallDir;
    }

    public void setEclccInstallDir(String eclccInstallDir) {
        this.eclccInstallDir = eclccInstallDir;
    }

    public boolean isIncludeML() {
        return includeML;
    }
    public String getIncludeML() {
        if(includeML){
            return "true";
        }else{
            return "false";
        }
    }
    public void setIncludeML(boolean includeML) {
        this.includeML = includeML;
    }
    
    public void setIncludeML(String includeML) {
        if(includeML.equals("true")){
            this.includeML = true;
        }else{
            this.includeML = false;
        }
    }


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMlPath() {
        return mlPath;
    }

    public void setMlPath(String mlPath) {
        this.mlPath = mlPath;
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
        
    	retval += "				<stepName>" + stepName + "</stepName>" + Const.CR;
    	retval += "				<outputfield>" + outputField + "</outputfield>" + Const.CR;
    	retval += "             <server_ip>"+this.serverIP+"</server_ip>"+Const.CR;
        
        retval += "             <server_port>"+this.serverPort+"</server_port>"+Const.CR;
        
        retval += "             <landing_zone>"+this.landingZone+"</landing_zone>"+Const.CR;
        
        retval += "             <mlPath>"+this.getMlPath()+"</mlPath>"+Const.CR;
        
        retval += "             <eclccInstallDir>"+this.getEclccInstallDir()+"</eclccInstallDir>"+Const.CR;
       
        retval += "             <jobName>"+this.getJobName()+"</jobName>"+Const.CR;
       
        retval += "             <cluster>"+this.getCluster()+"</cluster>"+Const.CR;
        
        retval += "             <includeML>"+this.getIncludeML() + "</includeML>"+Const.CR;
      
        
        
        return retval;
    }



    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			 setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			 setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_ip")) != null)
                 this.setServerIP(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_ip")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_port")) != null)
                 this.setServerPort(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_port")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"landing_zone")) != null)
                 this.setLandingZone(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"landing_zone")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"mlPath")) != null)
                 this.setMlPath(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"mlPath")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"eclccInstallDir")) != null)
                 this.setEclccInstallDir(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"eclccInstallDir")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"jobName")) != null)
                 this.setJobName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"jobName")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"cluster")) != null)
                 this.setCluster(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"cluster")));
             
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"includeML")) != null)
                 this.setIncludeML(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"includeML")));

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
        return new ECLGlobalVariablesStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLGlobalVariablesStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLGlobalVariablesStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		 	outputField = rep.getStepAttributeString(id_step, "otuputField"); //$NON-NLS-1$
    		 
    			serverIP = rep.getStepAttributeString(id_step, "server_ip");
            
	            serverPort = rep.getStepAttributeString(id_step, "server_port");
	            landingZone = rep.getStepAttributeString(id_step, "landing_zone");
	            
	            this.setMlPath( rep.getStepAttributeString(id_step, "mlPath"));
	            this.setEclccInstallDir( rep.getStepAttributeString(id_step, "eclccInstallDir"));
	            this.setJobName( rep.getStepAttributeString(id_step, "jobName"));
	            this.setCluster( rep.getStepAttributeString(id_step, "cluster"));
	            this.setIncludeML( rep.getStepAttributeString(id_step, "includeML"));
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		
    		rep.saveStepAttribute(id_step, getObjectId(), "server_ip", serverIP);
            rep.saveStepAttribute(id_step, getObjectId(), "server_port", serverPort);
            rep.saveStepAttribute(id_step, getObjectId(), "landing_zone", landingZone);
            
            rep.saveStepAttribute(id_step, getObjectId(), "mlPath", this.getMlPath());
            rep.saveStepAttribute(id_step, getObjectId(), "eclccInstallDir", this.getEclccInstallDir());
            rep.saveStepAttribute(id_step, getObjectId(), "jobName", this.getJobName());
            rep.saveStepAttribute(id_step, getObjectId(), "cluster", this.getCluster());
            rep.saveStepAttribute(id_step, getObjectId(), "includeML", this.getIncludeML());
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
