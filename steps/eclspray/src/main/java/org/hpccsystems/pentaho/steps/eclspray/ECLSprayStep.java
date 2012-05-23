package org.hpccsystems.pentaho.steps.eclspray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Dataset;
import org.hpccsystems.ecldirect.EclDirect;
import org.hpccsystems.ecldirect.Output;
import org.hpccsystems.ecldirect.Spray;
import org.hpccsystems.eclguifeatures.AutoPopulateSteps;
import org.hpccsystems.pentaho.steps.eclspray.ECLSprayStepData;
import org.hpccsystems.pentaho.steps.eclspray.ECLSprayStepMeta;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLSprayStep extends BaseStep implements StepInterface {

    private ECLSprayStepData data;
    private ECLSprayStepMeta meta;

    public ECLSprayStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (ECLSprayStepMeta) smi;
        data = (ECLSprayStepData) sdi;
        
        String serverHost = "";
        String serverPort = "";
        String landingZone = "";
        
        String cluster = "";
        String jobName = "";
        String eclccInstallDir = "";
        String mlPath = "";
        String includeML = "";
        //this.getTrans().getTransMeta().getSteps()
        
        
        Object[] r = getRow(); 
        String input = "";
        if (r == null) 
        {
        } else {
            logBasic("Found Row = " + r[r.length-1]);
            input = r[r.length-1].toString() + "\r\n";
        }
        
        Object[] newRow = new Object[1]; 
        
        //call the related direct library and fetch the ecl code
        
        
        AutoPopulateSteps ap = new AutoPopulateSteps();
        try{
        //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            serverHost = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_ip");
            serverPort = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_port");
            landingZone = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"landing_zone");
            
            cluster = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"cluster");
            jobName = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"jobName");
            
            eclccInstallDir = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"eclccInstallDir");
            mlPath = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"mlPath");
            includeML = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"includeML");
            
        }catch (Exception e){
            System.out.println("Error Parsing existing Global Variables ");
            System.out.println(e.toString());
            
        }
        

	    
	    Spray spray = new Spray();
	    spray.setClusterName("mythor");
	    spray.setFilePath(landingZone + meta.getFilePath());
	    spray.setIpAddress(serverHost);
	    spray.setLogicalFileName(meta.getLogicalFileName());
	    spray.setFileType(meta.getFileType());
	    spray.setCsvQuote(meta.getCsvQuote());
	    spray.setCsvSeparator(meta.getCsvSeparator());
	    spray.setCsvTerminator(meta.getCsvTerminator());
	    spray.setRecordSize(meta.getFixedRecordSize());
	
	     
	    //logBasic(spray.ecl());
	    logBasic("{Spray Job} Execute = " + spray.ecl());
	    

	
	    logBasic("{Spray Job} Called IP = " + (String)spray.getIpAddress());
	    
	    EclDirect eclDirect = new EclDirect(((String)spray.getIpAddress()).trim(), cluster, serverPort);
	    eclDirect.setEclccInstallDir(eclccInstallDir);
	    eclDirect.setIncludeML(includeML);
	    eclDirect.setJobName(jobName);
	    eclDirect.setMlPath(mlPath);
	    eclDirect.setOutputName(meta.getName());
	    ArrayList dsList = eclDirect.execute(spray.ecl());
	
        //Create a line like htis where op is the direct library that referecnes the direct 
        newRow[0] = input;//No need to add ecl to the steps its executed here + spray.ecl();
        
        putRow(data.outputRowMeta, newRow);
        
        logBasic("{Dataset Step} Output = " + newRow[0]);
        
        return false;
    }

    
    
    
    
    
    
    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLSprayStepMeta) smi;
        data = (ECLSprayStepData) sdi;

        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLSprayStepMeta) smi;
        data = (ECLSprayStepData) sdi;

        super.dispose(smi, sdi);
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic("Starting to run...");
        try {
            while (processRow(meta, data) && !isStopped())
				;
        } catch (Exception e) {
            logError("Unexpected error : " + e.toString());
            logError(Const.getStackTracker(e));
            setErrors(1);
            stopAll();
        } finally {
            dispose(meta, data);
            logBasic("Finished, processing " + getLinesRead() + " rows");
            markStop();
        }
    }
}
