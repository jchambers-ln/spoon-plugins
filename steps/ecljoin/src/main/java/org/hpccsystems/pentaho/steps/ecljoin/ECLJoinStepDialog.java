package org.hpccsystems.pentaho.steps.ecljoin;

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
import org.hpccsystems.eclguifeatures.AutoPopulateSteps;

public class ECLJoinStepDialog extends BaseStepDialog implements StepDialogInterface {

	private ECLJoinStepMeta input;
    private HashMap controls = new HashMap();
    
    private Text stepnameField;
    
    private Text joinCondition;
    private Combo joinType;
    private Combo leftRecordSet;
    private Combo rightRecordSet;
    private Text joinRecordSet;
    
    private Combo leftJoinCondition;
    private Combo rightJoinCondition;
    
    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
    
    public ECLJoinStepDialog(Shell parent, Object in, TransMeta transMeta, String stepName) {
    	
        super(parent, (BaseStepMeta) in, transMeta, stepName);
        input = (ECLJoinStepMeta) in;
        if(stepName != null && !stepName.equals("")){
    		input.setStepName(stepName);
    	}
    }

    public String open() {

    	Shell parentShell = getParent();
        Display display = parentShell.getDisplay();
        
        String datasets[] = null;
        AutoPopulateSteps ap = new AutoPopulateSteps();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            datasets = ap.parseDatasets(this.transMeta.getSteps()); //this.transMeta.getSteps()
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }
        
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
        shell.setText("Define an ECL Join");

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
        
        stepnameField = buildText("ECL JOIN", null, lsMod, middle, margin, generalGroup);

        //All other contols
        //Join Declaration
        Group joinGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(joinGroup);
        joinGroup.setText("Join Details");
        joinGroup.setLayout(groupLayout);
        FormData joinGroupFormat = new FormData();
        joinGroupFormat.top = new FormAttachment(generalGroup, margin);
        joinGroupFormat.width = 400;
        joinGroupFormat.height = 180;
        joinGroupFormat.left = new FormAttachment(middle, 0);
        joinGroup.setLayoutData(joinGroupFormat);

        this.leftRecordSet = buildCombo("Left Recordset Name", null, lsMod, middle, margin, joinGroup,datasets);
        this.rightRecordSet = buildCombo("Right Recordset Name", this.leftRecordSet, lsMod, middle, margin, joinGroup,datasets);
       // this.joinCondition = buildText("Join Condition", this.rightRecordSet, lsMod, middle, margin, joinGroup);
        
        this.leftJoinCondition = buildCombo("Left Join Condition", this.rightRecordSet, lsMod, middle, margin, joinGroup, new String[]{""});
        this.rightJoinCondition = buildCombo("Right Join Condition", this.leftJoinCondition, lsMod, middle, margin, joinGroup, new String[]{""});
        
        this.joinType = buildCombo("Join Type", this.rightJoinCondition, lsMod, middle, margin, joinGroup, new String[]{"INNER","LEFT OUTER", "RIGHT OUTER", "FULL OUTER", "LEFT ONLY", "RIGHT ONLY","FULL ONLY"});
        
        this.joinRecordSet = buildText("Resulting Recordset", this.joinType, lsMod, middle, margin, joinGroup);
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");
        
        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, joinGroup);
        
        leftRecordSet.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e){
                System.out.println("left RS changed");
                AutoPopulateSteps ap = new AutoPopulateSteps();
                try{
                    System.out.println("Load items for select");
                    String[] items = ap.fieldsByDataset( leftRecordSet.getText(), transMeta.getSteps());
                    System.out.println("++++++"+items.length+"+++++");
                    leftJoinCondition.setItems(items);
                    System.out.println("itemsSet");
                    leftJoinCondition.redraw();
                    System.out.println("new Join condition vals loaded");
                }catch (Exception ex){
                    System.out.println("failed to load record definitions");
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
               // leftJoinCondition.setItems(items);
            }
        });
        
        rightRecordSet.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e){
                System.out.println("left RS changed");
                AutoPopulateSteps ap = new AutoPopulateSteps();
                try{
                    System.out.println("Load items for select");
                    String[] items = ap.fieldsByDataset( rightRecordSet.getText(), transMeta.getSteps());
                    System.out.println("++++++"+items.length+"+++++");
                    rightJoinCondition.setItems(items);
                    System.out.println("itemsSet");
                    rightJoinCondition.redraw();
                    System.out.println("new Join condition vals loaded");
                }catch (Exception ex){
                    System.out.println("failed to load record definitions");
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
               // leftJoinCondition.setItems(items);
            }
        });
        
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
        if (input.getJoinType() != null) {
            this.joinType.setText(input.getJoinType());
        }
        if (input.getLeftRecordSet() != null) {
            this.leftRecordSet.setText(input.getLeftRecordSet());
        }
        if (input.getRightRecordSet() != null) {
            this.rightRecordSet.setText(input.getRightRecordSet());
        }
        if (input.getJoinRecordSet() != null) {
            this.joinRecordSet.setText(input.getJoinRecordSet());
        }
        
        if (input.getLeftJoinCondition() != null) {
            this.leftJoinCondition.setText(input.getLeftJoinCondition());
        }
        if (input.getRightJoinCondition() != null) {
            this.rightJoinCondition.setText(input.getRightJoinCondition());
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
    	
    	super.stepname = stepnameField.getText();
    	//input.setName(jobEntryName.getText());
    	input.setStepName(stepnameField.getText());
    	
    	//add other here
    	input.setLeftJoinCondition(this.leftJoinCondition.getText());
    	input.setRightJoinCondition(this.rightJoinCondition.getText());
    	input.setJoinType(this.joinType.getText());
    	input.setLeftRecordSet(this.leftRecordSet.getText());
    	input.setRightRecordSet(this.rightRecordSet.getText());
    	input.setJoinRecordSet(this.joinRecordSet.getText());
        
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
