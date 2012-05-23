package org.hpccsystems.pentaho.steps.eclgroup;


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

public class ECLGroupStepMeta extends BaseStepMeta implements StepMetaInterface {
	private String stepName;
	private String outputField;
	private String recordsetName;
	private String recordset;
	private String breakCriteria;
	private Boolean isAll = false;
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
	public String getRecordSetName() {
		return recordsetName;
	}

	public void setRecordSetName(String recordsetName) {
		this.recordsetName = recordsetName;
	}

	public String getRecordSet() {
		return recordset;
	}

	public void setRecordSet(String recordset) {
		this.recordset = recordset;
	}

	public String getBreakCriteria() {
		return breakCriteria;
	}

	public void setBreakCriteria(String breakCriteria) {
		this.breakCriteria = breakCriteria;
	}

	public boolean getIsAll() {
		return isAll;
	}

	public String getIsAllString() {
		if (this.isAll)
			return "true";
		return "false";
	}

	public void setIsAll(boolean isAll) {
		this.isAll = isAll;
	}

	public void setIsAllString(String isAll) {
		if (isAll.equals("true"))
			this.isAll = true;
		else
			this.isAll = false;
	}

	public boolean getIsRunLocal() {
		return runLocal;
	}

	public String getIsRunLocalString() {
		if (this.runLocal)
			return "true";
		return "false";
	}

	public void setRunLocal(boolean runLocal) {
		this.runLocal = runLocal;
	}

	public void setRunLocalString(String runLocal) {
		if (runLocal.equals("true"))
			this.runLocal = true;
		else
			this.runLocal = false;
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
    	retval += "             <recordset_name>"+recordsetName+"</recordset_name>"+Const.CR;
        retval += "             <recordset>"+recordset+"</recordset>"+Const.CR;
        retval += "             <breakCriteria>"+breakCriteria+"</breakCriteria>"+Const.CR;
        retval += "             <isAll>"+getIsAllString()+"</isAll>"+Const.CR;
        retval += "             <runLocal>"+getIsRunLocalString()+"</runLocal>"+Const.CR;
        
        return retval;
    }

    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset_name")) != null)
    			setRecordSetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset_name")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset")) != null)
    			setRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "breakCriteria")) != null)
    			setBreakCriteria(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"breakCriteria")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "isAll")) != null)
    			setIsAllString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isAll")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "runLocal")) != null)
    			setRunLocalString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"runLocal")));
    		 
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
        return new ECLGroupStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLGroupStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLGroupStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		if(rep.getStepAttributeString(id_step, "recordset_name") != null)
    			recordsetName = rep.getStepAttributeString(id_step, "recordset_name");
    		if(rep.getStepAttributeString(id_step, "recordset") != null)
    			recordset = rep.getStepAttributeString(id_step, "recordset");
    		if(rep.getStepAttributeString(id_step, "breakCriteria") != null)
    			breakCriteria = rep.getStepAttributeString(id_step, "breakCriteria");
    		if(rep.getStepAttributeString(id_step, "isAll") != null)
    			this.setIsAllString(rep.getStepAttributeString(id_step, "isAll"));
    		if(rep.getStepAttributeString(id_step, "runLocal") != null)
    			this.setRunLocalString(rep.getStepAttributeString(id_step, "runLocal"));
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "recordset_name", recordsetName);
            rep.saveStepAttribute(id_transformation, id_step, "recordset", recordset);
            rep.saveStepAttribute(id_transformation, id_step, "breakCriteria", breakCriteria);
            rep.saveStepAttribute(id_transformation, id_step, "isAll", this.getIsAllString());
            rep.saveStepAttribute(id_transformation, id_step, "runLocal", this.getIsRunLocalString());
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
