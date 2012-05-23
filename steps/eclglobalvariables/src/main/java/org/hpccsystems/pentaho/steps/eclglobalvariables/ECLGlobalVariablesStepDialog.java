package org.hpccsystems.pentaho.steps.eclglobalvariables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import org.hpccsystems.eclguifeatures.CreateTable;
import org.hpccsystems.eclguifeatures.RecordBO;
import org.hpccsystems.eclguifeatures.RecordList;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Color;

public class ECLGlobalVariablesStepDialog extends BaseStepDialog implements StepDialogInterface {

	private ECLGlobalVariablesStepMeta input;

    
    private Text stepnameField;
    
    private Text jobEntryName;

    private Text serverIP;
    private Text serverPort;
    private Text landingZone;
    
    private Text cluster;
    private Text jobName;
    private Text eclccInstallDir;
    private Text mlPath;
    private Combo includeML;
   
    public ECLGlobalVariablesStepDialog(Shell parent, Object in, TransMeta transMeta, String stepName) {
        super(parent, (BaseStepMeta) in, transMeta, stepName);
        input = (ECLGlobalVariablesStepMeta) in;
        if(stepName != null && !stepName.equals("")){
        	input.setStepName(stepName);
        }
    }

    public String open() {

    	Shell parentShell = getParent();
        Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        props.setLook(shell);
        setShellImage(shell, input);

        
      
                

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                input.setChanged();
            }
        };

        backupChanged = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;


        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Define an ECL Dataset");

        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        // Stepname line
        Group generalGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroup.setLayoutData(generalGroupFormat);
        
        stepnameField = buildText("Step Name", null, lsMod, middle, margin, generalGroup);

        
        
        

        //All other contols
        //Distribute Declaration
        Group varGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(varGroup);
        varGroup.setText("Server Details");
        varGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 220;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        varGroup.setLayoutData(datasetGroupFormat);

        //name = buildText("Distribute Name", null, lsMod, middle, margin, distributeGroup);

        
        serverIP = buildText("Server Host", null, lsMod, middle, margin, varGroup);
        serverPort = buildText("Server Port", serverIP, lsMod, middle, margin, varGroup);
        landingZone = buildText("Landing Zone Dir", serverPort, lsMod, middle, margin, varGroup);
        
        //move thes to Job Information
        cluster = buildText("Cluster", landingZone, lsMod, middle, margin, varGroup);
        jobName = buildText("Job Name", cluster, lsMod, middle, margin, varGroup);
        
        //move these to Library(s)
        
        eclccInstallDir = buildText("eclcc Install Dir", jobName, lsMod, middle, margin, varGroup);
        mlPath = buildText("Path to ML Library", eclccInstallDir, lsMod, middle, margin, varGroup);
        includeML = buildCombo("Include ML Library?", mlPath, lsMod, middle, margin, varGroup, new String[]{"true", "false"});
        
        
        
        
        
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, varGroup);

        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
            	updatePaths();
            	boolean isReady = verifySettings();
            	if(isReady){
            		ok();
            	}else{
            		
            	}
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };

        stepnameField.addSelectionListener(lsDef);
        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        if (input.getStepName() != null && !input.getStepName().equals("")) {
        	stepnameField.setText(input.getStepName());
        }else{
        	stepnameField.setText("Global Variables");
        }
        //add other set functions here
        
        if (input.getServerIP() != null) {
            serverIP.setText(input.getServerIP());
        }
        if (input.getServerPort() != null) {
            serverPort.setText(input.getServerPort());
        }
        
        if (input.getLandingZone() != null) {
            landingZone.setText(input.getLandingZone());
        }

        
        if (input.getCluster() != null) {
            cluster.setText(input.getCluster());
        }
        
        if (input.getJobName() != null) {
            jobName.setText(input.getJobName());
        }
        if (input.getEclccInstallDir() != null) {
            eclccInstallDir.setText(input.getEclccInstallDir());
        }
        if (input.getMlPath() != null) {
            mlPath.setText(input.getMlPath());
        }
        if (input.getIncludeML() != null) {
            includeML.setText(input.getIncludeML());
        }
        
        

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return stepname;
    }

    public void getData() {
        wStepname.selectAll();
    }

    private void cancel() {
        stepnameField = null;
        input.setChanged(changed);
        dispose();
    }

    // let the plugin know about the entered data
    private void ok() {
    	//input.setName(jobEntryName.getText());
    	input.setStepName(stepnameField.getText());
    	super.stepname = stepnameField.getText();
    	//add other here
    	input.setServerIP(serverIP.getText());
    	input.setServerPort(serverPort.getText());
    	input.setLandingZone(landingZone.getText());
        
    	input.setCluster(cluster.getText());
    	input.setJobName(jobName.getText());
        
    	input.setEclccInstallDir(eclccInstallDir.getText());
    	input.setMlPath(mlPath.getText());
    	input.setIncludeML(includeML.getText());
        dispose();
    	
    }

    private Text buildText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Group groupBox) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // text field
        Text text = new Text(groupBox, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        text.setLayoutData(fieldFormat);

        return text;
    }

    private Text buildMultiText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Group groupBox) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // text field
        Text text = new Text(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        fieldFormat.height = 50;
        text.setLayoutData(fieldFormat);

        return text;
    }
    
    private Combo buildCombo(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox, String[] items) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // combo field
        Combo combo = new Combo(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER);
        props.setLook(combo);
        combo.setItems(items);
        combo.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        fieldFormat.height = 50;
        combo.setLayoutData(fieldFormat);

        return combo;
    }
    
    
    private void updatePaths(){
    	String eclPath = eclccInstallDir.getText();
    	boolean eclLast = false;
    	if(eclPath.lastIndexOf("\\") == (eclPath.length()-1)){
    		//has last \
    		eclLast = true;
    	}
    	if(!eclLast && eclPath.lastIndexOf("/") == (eclPath.length()-1)){
    		eclLast = true;
    	}
    	if(!eclLast){
    		eclccInstallDir.setText(eclccInstallDir.getText() + "\\");
    	}
    	
    	if(includeML.getText().equals("true")){
	    	String mlP = mlPath.getText();
	    	boolean mlLast = false;
	    	if(mlP.lastIndexOf("\\") == (mlP.length()-1)){
	    		//has last \
	    		mlLast = true;
	    	}
	    	if(!mlLast && mlP.lastIndexOf("/") == (mlP.length()-1)){
	    		mlLast = true;
	    	}
	    	if(mlLast){	    		
	    		String noSlash = (mlPath.getText()).substring(0,(mlPath.getText()).length()-1);
	    		mlPath.setText(noSlash);
	    	}
    	}
    }
    private boolean verifySettings(){
    	boolean isReady = false;
    	
    	boolean eclccExists = true;
    	boolean mlExists = true;
    	
    	String errorTxt = "Some Fields Were Not Correct:\r\n";
    	
    	eclccExists = (new File(eclccInstallDir.getText())).exists();
    	if(!eclccExists){
    		//warn
    		System.out.println("no eclcc install found");
    		errorTxt += "The \"eclcc Install Dir\" could not be located\r\n";
    	}
    	if(includeML.getText().equals("true")){
    		mlExists = (new File(mlPath.getText())).exists();
    		if(!mlExists){
    			//warn
    			errorTxt += "The \"Path to ML Library\" could not be located\r\n";
    			System.out.println("No ML Library found");
    		}
    	}
    	if(eclccExists && mlExists){
    		isReady = true;
    		System.out.println("paths validated");
    	}else{
    		Shell parentShell = getParent();
            //Display display = parentShell.getDisplay();
    		//final Shell dialog = new Shell (display, SWT.DIALOG_TRIM);
    		final Shell dialog = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

    		Label label = new Label (dialog, SWT.NONE);
    		label.setText (errorTxt);
    		Button okButton = new Button (dialog, SWT.PUSH);
    		okButton.setText ("&OK");
   
	        Listener cancelListener = new Listener() {

	            public void handleEvent(Event e) {
	                dialog.close();
	            }
	        };
	        
	        okButton.addListener(SWT.Selection, cancelListener);
	        
	        FormLayout form = new FormLayout ();
	    	form.marginWidth = form.marginHeight = 8;
	    	dialog.setLayout (form);
	    	FormData okData = new FormData ();
	    	okData.top = new FormAttachment (label, 8);
	    	okButton.setLayoutData (okData);
	    	
	        
	        dialog.setDefaultButton (okButton);
	    	dialog.pack ();
	    	dialog.open ();
    	}
    	return isReady;
    }
}
