/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package org.hpccsystems.pentaho.job.eclmlclassify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import org.eclipse.swt.graphics.Color;

/**
*
* @author ChalaAX
*/
public class ECLML_ClassifyDialog extends JobEntryDialog implements JobEntryDialogInterface {

    private ECLML_Classify jobEntry;
    
    private Text jobEntryName;

    private Text recordsetName;
    
    private Text ridge;
    private Text epsilon;
    private Text maxIter;
    
    private Text passes;
    private Text alpha;
    
    
    private Text model; // 2
    private Text independentVar; // 1
    private Combo classifyType;
    private Combo dataType;


    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;

    public ECLML_ClassifyDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLML_Classify) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Naive Bayes");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;


       

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Define Classification - Classify");

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
        generalGroupFormat.width = 600;
        generalGroupFormat.height = 125;
        generalGroupFormat.left = new FormAttachment(0, 0);
        generalGroup.setLayoutData(generalGroupFormat);
        
        jobEntryName = buildText("Job Entry Name", null, lsMod, middle, margin, generalGroup);
        classifyType = buildCombo("Classify Type", jobEntryName, lsMod, middle, margin, generalGroup,new String[]{"", "NaiveBayes", "Logistic Regression", "Perceptron"});
        
        Group constGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(constGroup);
        constGroup.setText("Constructor Details");
        constGroup.setLayout(groupLayout);
        FormData constGroupFormat = new FormData();
        constGroupFormat.top = new FormAttachment(generalGroup, margin);
        constGroupFormat.width = 600;
        constGroupFormat.height = 200;
        constGroupFormat.left = new FormAttachment(0, 0);
        constGroup.setLayoutData(constGroupFormat);
        

        ridge = buildText("Ridge", null, lsMod, middle, margin, constGroup);
        epsilon = buildText("Epsilon", ridge, lsMod, middle, margin, constGroup);
        maxIter = buildText("Max Iterations", epsilon, lsMod, middle, margin, constGroup);
        
        passes = buildText("Passes", maxIter, lsMod, middle, margin, constGroup);
        alpha = buildText("Learning Rate", passes, lsMod, middle, margin, constGroup);

        Group recordGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(recordGroup);
        recordGroup.setText("Record Details");
        recordGroup.setLayout(groupLayout);
        FormData recordGroupFormat = new FormData();
        recordGroupFormat.top = new FormAttachment(constGroup, margin);
        recordGroupFormat.width = 600;
        recordGroupFormat.height = 170;
        recordGroupFormat.left = new FormAttachment(0, 0);
        recordGroup.setLayoutData(recordGroupFormat);
        
        //name = buildText("Distribute Name", null, lsMod, middle, margin, distributeGroup);

        dataType = buildCombo("Classify Data Type", null, lsMod, middle, margin, recordGroup,new String[]{"", "ClassifyC", "ClassifyD"});
       
        independentVar = buildText("Independent Variable", dataType, lsMod, middle, margin, recordGroup);
        
        model = buildText("Model", independentVar, lsMod, middle, margin, recordGroup);
        
        
        Group iterateGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(iterateGroup);
        iterateGroup.setText("Output");
        iterateGroup.setLayout(groupLayout);
        FormData iterateGroupFormat = new FormData();
        iterateGroupFormat.top = new FormAttachment(recordGroup, margin);
        iterateGroupFormat.width = 600;
        iterateGroupFormat.height = 100;
        iterateGroupFormat.left = new FormAttachment(0, 0);
        iterateGroup.setLayoutData(iterateGroupFormat);
        
        
        recordsetName = buildText("Output Recordset", null, lsMod, middle, margin, iterateGroup);
        
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
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };
        
        this.classifyType.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
             String cType = classifyType.getText();
             disableConstFields();
                if(cType.equals("NaiveBayes")){
                 disableConstFields();
                }else if(cType.equals("Logistic Regression")){
                 disableConstFields();
                 enableLogisticConstFields();
                }else if(cType.equals("Perceptron")){
                 disableConstFields();
                 enablePerceptronConstFields();
                }

            }
        });


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });
        


        //if (jobEntry.getJobName() != null) {
        // jobEntryName.setText(jobEntry.getJobName());
        //}
        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }
        

        

        if (jobEntry.getRecordsetName() != null) {
            recordsetName.setText(jobEntry.getRecordsetName());
        }
       

       
        
       
        if (jobEntry.getModel() != null) {
            model.setText(jobEntry.getModel());
        }
        
        if (jobEntry.getIndependentVar() != null) {
            independentVar.setText(jobEntry.getIndependentVar());
        }
        if (jobEntry.getClassifyType() != null) {
            classifyType.setText(jobEntry.getClassifyType());
            disableConstFields();
            String cType = classifyType.getText();
            if(cType.equals("NaiveBayes")){
             disableConstFields();
            }else if(cType.equals("Logistic Regression")){
             disableConstFields();
             enableLogisticConstFields();
            }else if(cType.equals("Perceptron")){
             disableConstFields();
             enablePerceptronConstFields();
            }
        }
        if (jobEntry.getDataType() != null) {
            dataType.setText(jobEntry.getDataType());
        }
        
        if (jobEntry.getRidge() != null) {
            ridge.setText(jobEntry.getRidge());
        }
        if (jobEntry.getEpsilon() != null) {
            epsilon.setText(jobEntry.getEpsilon());
        }
        if (jobEntry.getMaxIter() != null) {
            maxIter.setText(jobEntry.getMaxIter());
        }
        
        if (jobEntry.getPasses() != null) {
            passes.setText(jobEntry.getPasses());
        }
        if (jobEntry.getAlpha() != null) {
            alpha.setText(jobEntry.getAlpha());
        }




        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }
    
    private final void disableConstFields(){
  Color white = new Color(null,255,255,255);
        Color grey = new Color(null,245,245,245);
  ridge.setBackground(grey);
        epsilon.setBackground(grey);
        maxIter.setBackground(grey);
        ridge.setEnabled(false);
        epsilon.setEnabled(false);
        maxIter.setEnabled(false);
        
        passes.setBackground(grey);
        alpha.setBackground(grey);
        passes.setEnabled(false);
        alpha.setEnabled(false);
    }
    
    private final void enableLogisticConstFields(){
  Color white = new Color(null,255,255,255);
        Color grey = new Color(null,245,245,245);
  ridge.setBackground(white);
        epsilon.setBackground(white);
        maxIter.setBackground(white);
        ridge.setEnabled(true);
        epsilon.setEnabled(true);
        maxIter.setEnabled(true);
    }
    
    private final void enablePerceptronConstFields(){
  Color white = new Color(null,255,255,255);
        Color grey = new Color(null,245,245,245);
  passes.setBackground(white);
        alpha.setBackground(white);
        passes.setEnabled(true);
        alpha.setEnabled(true);
    }


    private Text buildText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
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
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
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
        Text text = new Text(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.V_SCROLL);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        fieldFormat.height = 100;
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

    private void ok() {
           
        jobEntry.setName(jobEntryName.getText());
        jobEntry.setRecordsetName(recordsetName.getText());
        //jobEntry.setRecordName(recordName.getText());
                    //private Text algType; //NaiveBayes, Logistic
    //private Text dependentVar; // 1
    //private Text independentVar; // 2
        
        
        jobEntry.setModel(model.getText());
        jobEntry.setIndependentVar(independentVar.getText());
        jobEntry.setClassifyType(classifyType.getText());
        jobEntry.setDataType(dataType.getText());
        
        jobEntry.setRidge(ridge.getText());
        jobEntry.setEpsilon(epsilon.getText());
        jobEntry.setMaxIter(maxIter.getText());
        
        jobEntry.setPasses(passes.getText());
        jobEntry.setAlpha(alpha.getText());
        

        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }

    public void dispose() {
        WindowProperty winprop = new WindowProperty(shell);
        props.setScreen(winprop);
        shell.dispose();
    }
}