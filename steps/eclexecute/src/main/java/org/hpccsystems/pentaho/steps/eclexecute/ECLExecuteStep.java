package org.hpccsystems.pentaho.steps.eclexecute;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Column;
import org.hpccsystems.ecldirect.Dataset;
import org.hpccsystems.ecldirect.ECLSoap;
import org.hpccsystems.ecldirect.EclDirect;
import org.hpccsystems.eclguifeatures.AutoPopulateSteps;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLExecuteStep extends BaseStep implements StepInterface {

    private ECLExecuteStepData data;
    private ECLExecuteStepMeta meta;

    public ECLExecuteStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (ECLExecuteStepMeta) smi;
        data = (ECLExecuteStepData) sdi;
<<<<<<< HEAD
        /*
        AutoPopulateSteps ap2 = new AutoPopulateSteps();
        System.out.println("______________________________________________________==");
        try{
        //Object[] jec = this.jobMeta.getJobCopies().toArray();
        	//this.getTrans().getSteps();
        	//this.getTrans().getTransMeta().getSteps()
        	
           System.out.println("SERVER_IP: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_ip"));
           System.out.println("SERVER_PORT: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_port"));

           System.out.println("CLUSTER: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"cluster"));
           System.out.println("jobName: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"jobName"));

           System.out.println("ECLCCINSTALLDIR: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"eclccInstallDir"));
           System.out.println("mlPath: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"mlPath"));
           System.out.println("includeML: " + ap2.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"includeML"));
           
            
           

        }catch (Exception e){
            System.out.println("Error Parsing existing Global Variables ");
            System.out.println(e.toString());
            e.printStackTrace();

        }
        System.out.println("==______________________________________________________==");
        */
=======
        
        
        Object[] row = getRow(); 
        String input = "";
        if (row == null) 
        {
        } else {
            logBasic("Found Row = " + row[row.length-1]);
            input = row[row.length-1].toString() + "\r\n";
        }


        String eclCode = input;
        
>>>>>>> master
        //Result result = prevResult;
        if(false){//result.isStopped()){
            logBasic("{Output Job is Stopped}");

           if(!ECLExecuteStepMeta.isReady){
             //  result.setResult(false);
           } 
        }else{
        
        ErrorNotices en = new ErrorNotices();
        
        
        //en.openDialog("Test Error, Click OK to continue");
        en.openValidateCodeDialog();
        boolean validate = en.isValidateCode();

       // JobMeta jobMeta = super.parentJob.getJobMeta();
                
        String serverHost = "";
        String serverPort = "";
        String cluster = "";
        String jobName = "";
        String eclccInstallDir = "";
        String mlPath = "";
        String includeML = "";
        
        AutoPopulateSteps ap = new AutoPopulateSteps();
        try{
        //Object[] jec = this.jobMeta.getJobCopies().toArray();
        	//this.getTrans().getSteps();
        	//this.getTrans().getTransMeta().getSteps()
            serverHost = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_ip");
            serverPort = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"server_port");

            cluster = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"cluster");
            jobName = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"jobName");

            eclccInstallDir = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"eclccInstallDir");
            mlPath = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"mlPath");
            includeML = ap.getGlobalVariable(this.getTrans().getTransMeta().getSteps(),"includeML");
            
            System.out.println("@@@@@@@@@@@@@@@@@@@" + includeML + "@@@@@@");

        }catch (Exception e){
            System.out.println("Error Parsing existing Global Variables ");
            System.out.println(e.toString());
            e.printStackTrace();

        }
        
        System.out.println("Output -- Finished setting up Global Variables");
        meta.setServerAddress(serverHost);
        meta.setServerPort(serverPort);
        
        
        
            ECLExecuteStepMeta.isReady=true;
            logBasic("not waiting: " + ECLExecuteStepMeta.isReady);
            //Output op = new Output();
            //op.setDefinitionName(getAttributeName());

            //String opResults = op.ecl();

            //logBasic("{Output Job} Execute = " + opResults);

            //logBasic("{Output Job} Previous =" + result.getLogText());

            //result.setResult(true);

           // RowMetaAndData data = new RowMetaAndData();
            //data.addValue("ecl", Value.VALUE_TYPE_STRING, opResults);


           // List list = result.getRows();
          //  list.add(data);
<<<<<<< HEAD
            String eclCode = "";
=======
            //String eclCode = "";
>>>>>>> master
           // if (list == null) {
            List list = new ArrayList();
          /*  } else {

                for (int i = 0; i < list.size(); i++) {
                    RowMetaAndData rowData = (RowMetaAndData) list.get(i);
                    String code = rowData.getString("ecl", null);
                    if (code != null) {
                        eclCode += code;
                    }
                }
                logBasic("{Output Job} Output Code =" + eclCode);
            }
          */
            
            
            EclDirect eclDirect = new EclDirect(meta.getServerAddress(), cluster, meta.getServerPort());
            eclDirect.setEclccInstallDir(eclccInstallDir);
            eclDirect.setIncludeML(includeML);
            eclDirect.setJobName(jobName);
            eclDirect.setMlPath(mlPath);
            eclDirect.setOutputName(meta.getName());
            ArrayList dsList = null;
            String outStr = "";
            //System.out.println("Output -- Finished setting up ECLDirect");
            try{
                String includes = "";
                includes += "IMPORT Std;\n";
                if(includeML.equals("true")){
                    includes += "IMPORT * FROM ML;\r\n\r\n";
                    includes += "IMPORT * FROM ML.Cluster;\r\n\r\n";
                    includes += "IMPORT * FROM ML.Types;\r\n\r\n";
                }
                 System.out.println("Output -- Finished Imports");
                eclCode = includes + eclCode;
                String error = "";
                boolean isValid = true;
                if(validate){
                   // System.out.println("Output -- Start Validate");
                    ECLSoap es = new ECLSoap();
                    es.setEclccInstallDir(eclccInstallDir);
                    es.setCluster(cluster);
                    es.setHostname(meta.getServerAddress());
                    es.setJobName(jobName);
                    es.setOutputName(meta.getName());
                    System.out.println("@@@@@@@@@@@@@@@@@@@2" + includeML + "@@@@@@");
                    if(includeML.equals("true")){
                        es.setIncludeML(true);
                        System.out.println("includML");
                    }else{
                        es.setIncludeML(false);
                        System.out.println("Dont includML");
                    }
                    es.setMlPath(mlPath);
                    es.setPort(Integer.parseInt(serverPort));
                    // System.out.println("+++++");
                    error = (es.syntaxCheck(eclCode)).trim();
                    //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    //System.out.println("|"+error+"|");
                    if(!error.equals("")){
                        System.out.println("Throw Exception");
                        logError("Syntax Check Failed:\n\r"+error);
                        this.logRowlevel("Syntax Check Failed:\n\r"+error);
                        en.openDialog("Syntax Check Failed:", error,eclCode);
                        isValid = false;
                        //result.setResult(false);
                        throw new KettleException("ECL failed Validation, please correct and re-run." + error);
                    }else{
                    	System.out.println("!!!!!!!!!!1 USE execute_noResults");
                        //isValid = eclDirect.execute_noResults(eclCode);
                    	
                    	
                        
                        isValid = es.executeECL(eclCode);
                        eclDirect.setWuid(es.getWuid());
                        
                    }
                   // System.out.println("Output -- Finished Validating");
                }else{
                	System.out.println("!!!!!!!!!!2 USE execute_noResults");
                    //isValid = eclDirect.execute_noResults(eclCode);
                	ECLSoap es = new ECLSoap();
                    es.setEclccInstallDir(eclccInstallDir);
                    es.setCluster(cluster);
                    es.setHostname(meta.getServerAddress());
                    es.setJobName(jobName);
                    es.setOutputName(meta.getName());
                    System.out.println("@@@@@@@@@@@@@@@@@@@2" + includeML + "@@@@@@");
                    if(includeML.equals("true")){
                        es.setIncludeML(true);
                        System.out.println("includML");
                    }else{
                        es.setIncludeML(false);
                        System.out.println("Dont includML");
                    }
                    es.setMlPath(mlPath);
                    es.setPort(Integer.parseInt(serverPort));
                    isValid = es.executeECL(eclCode);
                    eclDirect.setWuid(es.getWuid());
                }
                //logBasic("Start Log Results");
               
                    if(isValid){// && dsList != null){
                        ArrayList al = eclDirect.resultList();
                        
                        int alSize = al.size();
                        // System.out.println(al.toString());
                         //rows
                        for(int i = 0; i < alSize ; i++){
                            //System.out.println("-");
                            ArrayList al2 = (ArrayList)al.get(i);
                            int al2Size = al2.size();
                            //columns
                            int counter = 0;
                            for(int j = 0; j < al2Size ; j++){
                               // System.out.println("--");

                                ArrayList al3 = (ArrayList)al2.get(j);
                                int al3Size = al3.size();
                                
                                for(int r = 0; r < al3Size ; r++){

                                    if(((Column)al3.get(r)).getName().equals("Name")){
                                        //System.out.println("---");
                                        String resName = ((Column)al3.get(r)).getValue();
                                        //System.out.println( v2);
                                        ECLSoap es = new ECLSoap();
                                        es.setEclccInstallDir(eclccInstallDir);
                                        es.setCluster(cluster);
                                        es.setHostname(meta.getServerAddress());
                                        es.setJobName(jobName);
                                        es.setOutputName(meta.getName());
                                        System.out.println("@@@@@@@@@@@@@@@@@@@3" + includeML + "@@@@@@");
                                        if(includeML.equals("true")){
                                            es.setIncludeML(true);
                                            System.out.println("includML");
                                        }else{
                                            es.setIncludeML(false);
                                            System.out.println("Dont includML");
                                        }
                                        
                                        InputStream is = es.ResultsSoapCall(eclDirect.getWuid(), resName);
                                        ArrayList results = es.parseResults(is);
                                        //System.out.println(this.fileName + "\\" + resName + ".csv");
                                        resName = resName.replace(" ", "_");
                                        createOutputFile(results,meta.getFileName() + "\\" + resName + ".csv",counter);
                                        counter++;
                                    }
                                }
                                
                            }
                            //set counter to memory
                                
                                //System.out.println(counter);
                                System.setProperty("fileCount", counter+"" );
                        }
                      //  this.createOutputFile(dsList,fileName);
                      //  result.setRows(list);
                    }else{
                    	logError("Failed to compile code please verify your settings");
                    	//result.setResult(false);
                    
                    }
             }catch (Exception e){
                logError("Failed to execute code on Cluster." + e);
                //result.setResult(false);
                 e.printStackTrace();
            }
               
            
            
            //write the executed ecl code to file
            try {              
                System.getProperties().setProperty("eclCodeFile",meta.getFileName());
                
                BufferedWriter out = new BufferedWriter(new FileWriter(meta.getFileName() + "\\eclcode.txt"));
                out.write(eclCode);
                out.close();
            } catch (IOException e) {
                 e.printStackTrace();
            }   
             
        
       }
         return false;
    }

    
    
    
    
    public void createOutputFile(ArrayList dsList,String fileName, int count){
        String outStr = "";
        String header = "";
        if(dsList != null){
        String newline = System.getProperty("line.separator");
        
                       for (int iList = 0; iList < dsList.size(); iList++) {
                           //logBasic("----------Outer-------------");
                           ArrayList rowList = (ArrayList) dsList.get(iList);

                           for (int jRow = 0; jRow < rowList.size(); jRow++) {
                               //logBasic("----------Row-------------");
                               ArrayList columnList = (ArrayList) rowList.get(jRow);

                               for (int lCol = 0; lCol < columnList.size(); lCol++) {
                                //   logBasic("----------Column-------------");
                                   Column column = (Column) columnList.get(lCol);
                                   logBasic(column.getName() + "=" + column.getValue() + "|");
                                   outStr += column.getValue();
                                   if(lCol< (columnList.size()-1)){
                                       outStr += ",";
                                   }
                                   if(jRow == 0){
                                       header += column.getName();
                                       if(lCol< (columnList.size()-1)){
                                           header += ",";
                                       }else{
                                           header += newline;
                                       }
                                   }
                               }
                               logBasic("newline");
                               outStr += newline;
                           }
                       }
            try {
               
               BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
               System.getProperties().getProperty("fileName");
               System.setProperty("fileName"+count, fileName);
               
               out.write(header+outStr);
               out.close();
          
           } catch (IOException e) {
              logError("Failed to write file: " + fileName); 
              //result.setResult(false);
               e.printStackTrace();
           }  
        }
   }
    
    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLExecuteStepMeta) smi;
        data = (ECLExecuteStepData) sdi;
<<<<<<< HEAD

=======
        super.setStepname(meta.getStepName()); 
        
>>>>>>> master
        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLExecuteStepMeta) smi;
        data = (ECLExecuteStepData) sdi;

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
