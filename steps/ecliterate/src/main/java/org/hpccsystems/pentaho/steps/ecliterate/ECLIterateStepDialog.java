package org.hpccsystems.pentaho.steps.ecliterate;

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
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

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

public class ECLIterateStepDialog extends BaseStepDialog implements StepDialogInterface {

	private ECLIterateStepMeta input;
    private HashMap controls = new HashMap();
    
    private Text stepnameField;
    private Text transform;
    private Text transformName;
    private Text recordset;//Comma separated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private Combo runLocal;
   
    private Text record;
    private Text recordName;
    private Text recordsetName;
    private Text recordsetNameIterate;
   
    private Text transformCall;
    private Text returnType;
   
    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
   
    public ECLIterateStepDialog(Shell parent, Object in, TransMeta transMeta, String stepName) {
        super(parent, (BaseStepMeta) in, transMeta, stepName);
        input = (ECLIterateStepMeta) in;
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
        shell.setText("Define an ECL Iterate");

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
        Group transformGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(transformGroup);
        transformGroup.setText("Transform Details");
        transformGroup.setLayout(groupLayout);
        FormData transformGroupFormat = new FormData();
        transformGroupFormat.top = new FormAttachment(generalGroup, margin);
        transformGroupFormat.width = 400;
        transformGroupFormat.height = 175;
        transformGroupFormat.left = new FormAttachment(middle, 0);
        transformGroup.setLayoutData(transformGroupFormat);
        
        transformName = buildText("Transform Name", null, lsMod, middle, margin, transformGroup);
        transform = buildMultiText("Transform", transformName, lsMod, middle, margin, transformGroup);


        Group recordGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(recordGroup);
        recordGroup.setText("Record Details");
        recordGroup.setLayout(groupLayout);
        FormData recordGroupFormat = new FormData();
        recordGroupFormat.top = new FormAttachment(transformGroup, margin);
        recordGroupFormat.width = 400;
        recordGroupFormat.height = 300;
        recordGroupFormat.left = new FormAttachment(middle, 0);
        recordGroup.setLayoutData(recordGroupFormat);
        
        //name = buildText("Distribute Name", null, lsMod, middle, margin, distributeGroup);


        recordName = buildText("Record Name", null, lsMod, middle, margin, recordGroup);
        record = buildMultiText("Record", recordName, lsMod, middle, margin, recordGroup);

        recordsetName = buildText("Recordset Name", record, lsMod, middle, margin, recordGroup);
        recordset = buildMultiText("Recordset", recordsetName, lsMod, middle, margin, recordGroup);
        
        Group iterateGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(iterateGroup);
        iterateGroup.setText("Iterate Details");
        iterateGroup.setLayout(groupLayout);
        FormData iterateGroupFormat = new FormData();
        iterateGroupFormat.top = new FormAttachment(recordGroup, margin);
        iterateGroupFormat.width = 400;
        iterateGroupFormat.height = 95;
        iterateGroupFormat.left = new FormAttachment(middle, 0);
        iterateGroup.setLayoutData(iterateGroupFormat);
        
        //returnType = buildText("Return Type", null, lsMod, middle, margin, iterateGroup);
        recordsetNameIterate = buildText("Resulting Recordset", null, lsMod, middle, margin, iterateGroup);
        transformCall = buildText("Transform Call", recordsetNameIterate, lsMod, middle, margin, iterateGroup);
        runLocal = buildCombo("RUNLOCAL", transformCall, lsMod, middle, margin, iterateGroup,new String[]{"false", "true"});
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, iterateGroup);

        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
            	//updatePaths();
            	//boolean isReady = verifySettings();
            	//if(isReady){
            		ok();
            	//}else{
            		
            	//}
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
        
        if (input.getTransformName() != null) {
            transformName.setText(input.getTransformName());
        }
        if (input.getTransform() != null) {
            transform.setText(input.getTransform());
        }
        if (input.getRecordset() != null) {
            recordset.setText(input.getRecordset());
        }
        if (input.getRecordsetNameIterate() != null) {
            recordsetNameIterate.setText(input.getRecordsetNameIterate());
        }
        if (input.getRecordsetName() != null) {
            recordsetName.setText(input.getRecordsetName());
        }
        if (input.getRecord() != null) {
            record.setText(input.getRecord());
        }
        if (input.getRecordName() != null) {
            recordName.setText(input.getRecordName());
        }
        
        if (input.getReturnType() != null) {
            //returnType.setText(jobEntry.getReturnType());
        }
        
       // if (jobEntry.getRunLocalString() != null) {
            runLocal.setText(input.getRunLocalString());
        //}
        if (input.getTransformCall() != null) {
            transformCall.setText(input.getTransformCall());
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
        stepname = null;
        input.setChanged(changed);
        dispose();
    }

    // let the plugin know about the entered data
    private void ok() {
    	//input.setName(jobEntryName.getText());
    	input.setStepName(stepnameField.getText());
    	super.stepname = stepnameField.getText();
    	//add other here
    	input.setRecordsetNameIterate(recordsetNameIterate.getText());
    	input.setTransformName(transformName.getText());
    	input.setTransform(transform.getText());
    	input.setRecordset(recordset.getText());
    	input.setRecordsetName(recordsetName.getText());
    	input.setRecord(record.getText());
    	input.setRecordName(recordName.getText());
    	input.setRunLocalString(runLocal.getText());
    	input.setTransformCall(transformCall.getText());
    	
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
}
