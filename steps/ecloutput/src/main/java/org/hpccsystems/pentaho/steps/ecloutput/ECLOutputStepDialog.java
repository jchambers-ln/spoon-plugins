package org.hpccsystems.pentaho.steps.ecloutput;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import org.hpccsystems.eclguifeatures.AutoPopulateSteps;
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
import org.eclipse.swt.graphics.Point;
import org.pentaho.di.trans.step.StepMeta;
import java.util.List;

public class ECLOutputStepDialog extends BaseStepDialog implements StepDialogInterface {
	private ECLOutputStepMeta input;

    private Text stepnameField;

    private Button wOK, wCancel, fileOpenButton;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
   
    private Combo attributeName;
    
    

    private Combo isDef; //true set output into attr using job entry name
    private Combo outputType; //recordset,expression
    //private Combo includeFormat; //yes/no
    
    private Combo inputType; //thor file options, CSV, XML, pipe, Named, File Owned by workunit
    private Text outputFormat;
    //private Text recordset;
    
    private Text expression;
    
    
    private Text file;
    private Text typeOptions; // used for CSV, XML, Pipe
    private Text fileOptions; // used for CSV, XML
    
    private Text named; //used for named
    private Combo extend; // availiable for Named
    private Combo returnAll; // availiable for Named
    
    private Combo thor; // used in 
    
    //used in different file types
    private Text cluster;
    private Text encrypt;
    private Combo compressed;
    private Combo overwrite;
    private Text expire;
    private Combo repeat;// piped

    private Combo pipeType;
    
    
    private Group expressionGroup;
    private Group outputGroup;
    private Group generalGroup;
    private Group recordsetGroup;
    private Group recordsetDetailsGroup;
    private Group fileTypeGroup;
    private Composite c1;
    private FormLayout groupLayout;
    
    private String datasets[] = null;
   
    //private Text serverAddress;
    
    
    private HashMap controls = new HashMap();

    public ECLOutputStepDialog(Shell parent, Object in, TransMeta transMeta, String stepName) {
        super(parent, (BaseStepMeta) in, transMeta, stepName);
        input = (ECLOutputStepMeta) in;
        if(stepName != null && !stepName.equals("")){
        	input.setStepName(stepName);
        }else{
        	input.setStepName("Output");
        }
    }

    public String open() {
    	
        List<StepMeta> steps = this.transMeta.getSteps();
       // this.stepMeta.getCopies();
         Object[] jec = steps.toArray();
        StepMeta sm = (StepMeta)jec[0];
        try {
			System.out.println(sm.getXML());
		} catch (KettleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	Shell parentShell = getParent();
        Display display = parentShell.getDisplay();
        

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        shell.setLayout(new FillLayout(SWT.VERTICAL));
    
        props.setLook(shell);
        setShellImage(shell, input);

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                input.setChanged();
            }
        };

        backupChanged = input.hasChanged();
        
        createContents(shell, lsMod);
        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });
        
                shell.pack();
        shell.open();
        c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        c1.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return stepname;
    }
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////
    
    private void createContents(Composite shell, final ModifyListener lsMod) {
        /*
        final String datasets[] = new String[]{"", "a", "b"};
        Display display = new Display();
        Color red = display.getSystemColor(SWT.COLOR_RED);
        Color blue = display.getSystemColor(SWT.COLOR_BLUE);
        Shell shell = new Shell(display);
        */
        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;
        
        
        AutoPopulateSteps ap = new AutoPopulateSteps();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            datasets = ap.parseDatasets(this.transMeta.getSteps());
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }
        
        
        
        shell.setBackground(new Color(shell.getDisplay(),255,255,255));
        GridLayout fl = new GridLayout(2,false);
        
        shell.setLayout(fl);
        GridData gd = new GridData();
        gd.horizontalSpan = 2;



        groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;
        

        final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER | SWT.SHADOW_OUT);
        
        sc1.setBackground(new Color(sc1.getDisplay(),255,255,255));
        sc1.setLayoutData(gd);
        sc1.setAlwaysShowScrollBars(true);
        
        c1 = new Composite(sc1, SWT.NONE);
        sc1.setContent(c1);
        //c1.setBackground(red);
        GridLayout layout = new GridLayout();
        layout.marginRight = 15;
        layout.numColumns = 1;
        
        c1.setLayout(layout);
        c1.setBackground(new Color(c1.getDisplay(),255,255,255));

        c1.setSize(595, 400);
        
        this.createGeneralGroup(c1, groupLayout, lsMod);
        
        this.loadGeneralGroup(lsMod);

        c1.setSize(new Point(560,400));
 
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        
        GridData okGrid = new GridData();
       
        okGrid.widthHint = 55;
        okGrid.heightHint = 24;
        okGrid.horizontalIndent = 250;
        wOK.setLayoutData(okGrid);
        
        
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");
        
         GridData cancelGrid = new GridData();
        cancelGrid.widthHint = 55;
        cancelGrid.heightHint = 24;
        wCancel.setLayoutData(cancelGrid);

       //BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, shell);

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

        shell.setSize(600, 500);
         
        
        


        
    }
    
    
    
    
    
    private void loadGeneralGroup(final ModifyListener lsMod){
       if(input.getInputType().equals("Recordset")){
            this.createOutputGroup(c1, groupLayout, lsMod);
       }
        if (input.getStepName() != null) {
        	stepnameField.setText(input.getStepName());
        }
         if (input.getIsDef() != null) {
            
            this.isDef.setText(input.getIsDef());
            
        }
        if(input.getInputType() != null){
            String t = input.getInputType();
            this.inputType.setText(t);
            
            disposeExpressionGroup();
            disposeRecordsetGroup();
            disposeFileTypeGroup();       
            disposeRecordsetDetailsGroup();

            if(inputType.getText().equals("Recordset")) {
                createRecordsetGroup(c1, groupLayout, lsMod);
            } else if(inputType.getText().equals("Expression")){

                createExpressionGroup(c1, groupLayout, lsMod);
            }
            c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            c1.layout();
        }
        //if(input.getIncludeFormat() != null){
        //    this.includeFormat.setText(input.getIncludeFormat());
        //}
        
        
        this.inputType.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                            
                            disposeExpressionGroup();
                            disposeRecordsetGroup();
                            disposeFileTypeGroup();       
                            disposeRecordsetDetailsGroup();
                            disposeOutputGroup();
                            if(inputType.getText().equals("Recordset")) {
                                createOutputGroup(c1, groupLayout, lsMod);
                                createRecordsetGroup(c1, groupLayout, lsMod);
                            } else if(inputType.getText().equals("Expression")){
                                
                                createExpressionGroup(c1, groupLayout, lsMod);
                            }
                        c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                        c1.layout();
                    };
            });
    }
    
    private void createGeneralGroup(Composite parent, final FormLayout groupLayout, final ModifyListener lsMod) {
        int middle = 50;
        final int margin = 10;
        generalGroup = new Group(parent, SWT.SHADOW_NONE);
        generalGroup.setBackground(new Color(generalGroup.getDisplay(),255,255,255));
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        groupFormat.minimumHeight = 150;
        groupFormat.heightHint = 150;
        groupFormat.widthHint = 530;
        generalGroup.setLayoutData(groupFormat);
        generalGroup.setSize(generalGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        generalGroup.layout();

        stepnameField = buildText("Step Name", null, lsMod, middle, margin, generalGroup);
        this.isDef = buildCombo("Is definition", stepnameField, lsMod, middle, margin, generalGroup, new String[]{"", "Yes", "No"});
        this.inputType = buildCombo("Input Type", isDef, lsMod, middle, margin, generalGroup, new String[]{"", "Recordset", "Expression"});
       // this.includeFormat = buildCombo("Include Format", inputType, lsMod, middle, margin, generalGroup, new String[]{"", "Yes", "No"});
        
       
        

    }
    
    
    private void disposeOutputGroup(){
         if(this.attributeName != null)this.attributeName.dispose();
         if(this.outputGroup != null){
            this.outputGroup.dispose();
        }
    }
    private void createOutputGroup(Composite parent, FormLayout groupLayout, ModifyListener lsMod) {
        int middle = 50;
        final int margin = 10;
        outputGroup = new Group(parent, SWT.SHADOW_NONE);
        outputGroup.setBackground(new Color(outputGroup.getDisplay(),255,255,255));
        outputGroup.setText("Output Details");
        outputGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        groupFormat.minimumHeight = 65;
        groupFormat.heightHint = 55;
        groupFormat.widthHint = 530;
        outputGroup.setLayoutData(groupFormat);
        outputGroup.setSize(outputGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        outputGroup.layout();



        this.attributeName = buildCombo("Recordset Name", null, lsMod, middle, margin, outputGroup, datasets);
        
        if (input.getAttributeName() != null) {
            
            this.attributeName.setText(input.getAttributeName());
        }
        

    }

    private void disposeExpressionGroup(){
        if(this.expression != null)this.expression.dispose();
        if(this.named != null)this.named.dispose();
        if(this.expressionGroup != null){
            this.expressionGroup.dispose();
        }
    }
    private void createExpressionGroup(Composite parent, FormLayout groupLayout, ModifyListener lsMod) {
        int middle = 45;
        final int margin = 20;
        this.expressionGroup = new Group(parent, SWT.SHADOW_NONE);
        expressionGroup.setBackground(new Color(expressionGroup.getDisplay(),255,255,255));
        expressionGroup.setText("Expression Details");
        expressionGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        groupFormat.minimumHeight = 265;
        groupFormat.heightHint = 265;
        groupFormat.widthHint = 530;
        expressionGroup.setLayoutData(groupFormat);
        expressionGroup.setSize(expressionGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        expressionGroup.layout();



        expression = buildMultiText("Expression", null, lsMod, middle, margin, this.expressionGroup);
        named = buildText("Name", expression, lsMod, middle, margin, this.expressionGroup);
        
        if(input.getExpression() != null && !this.expression.isDisposed()){
            this.expression.setText(input.getExpression());
        }
        //System.out.println("no if read anmed from file:" + input.getNamed() + "|");
        if(input.getNamed() != null && !this.named.isDisposed()){
           // System.out.println("read anmed from file:" + input.getNamed() + "|");
            this.named.setText(input.getNamed());
        }

    }
    private void disposeRecordsetGroup(){
        if(this.thor != null)this.thor.dispose();
        if(this.recordsetGroup != null)this.recordsetGroup.dispose();
    }
     private void createRecordsetGroup(Composite parent, final FormLayout groupLayout, final ModifyListener lsMod) {
        int middle = 45;
        final int margin = 20;
        recordsetGroup = new Group(parent, SWT.SHADOW_NONE);
        recordsetGroup.setBackground(new Color(recordsetGroup.getDisplay(),255,255,255));
        recordsetGroup.setText("Recordset Details");
        recordsetGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        groupFormat.minimumHeight = 65;
        groupFormat.heightHint = 65;
        groupFormat.widthHint = 530;
        recordsetGroup.setLayoutData(groupFormat);
        recordsetGroup.setSize(recordsetGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        recordsetGroup.layout();
        
        this.thor = buildCombo("File on disk, \"owned\" by workunit", null, lsMod, middle, margin, recordsetGroup,new String[]{"", "Yes", "No"});

        if(input.getThor() != null){
            this.thor.setText(input.getThor());
            disposeFileTypeGroup();       
            disposeRecordsetDetailsGroup();
            if(thor.getText().equals("No")){
               createRecordsetDetailsGroup(c1, groupLayout, lsMod);
            }
            c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                        c1.layout();
        }
        this.thor.addSelectionListener(new SelectionAdapter(){
                public void widgetSelected(SelectionEvent e) {
                    disposeFileTypeGroup();       
                    disposeRecordsetDetailsGroup();
                    
                    if(thor.getText().equals("Yes")) {

                    } else if(thor.getText().equals("No")){
                       createRecordsetDetailsGroup(c1, groupLayout, lsMod);
                    }
                    c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                    c1.layout();
                };
                
            
            });

       
    }
    
    private void disposeRecordsetDetailsGroup(){
        if(this.outputFormat != null)this.outputFormat.dispose();
        if(this.outputType != null)this.outputType.dispose();
        if(this.recordsetDetailsGroup != null)this.recordsetDetailsGroup.dispose();
    }
    private void createRecordsetDetailsGroup(Composite parent, final FormLayout groupLayout, final ModifyListener lsMod) {
        int middle = 45;
        final int margin = 20;
        recordsetDetailsGroup = new Group(parent, SWT.SHADOW_NONE);
        recordsetDetailsGroup.setBackground(new Color(recordsetDetailsGroup.getDisplay(),255,255,255));
        recordsetDetailsGroup.setText("Recordset Output Details");
        recordsetDetailsGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        groupFormat.minimumHeight = 105;
        groupFormat.heightHint = 105;
        groupFormat.widthHint = 530;
        recordsetDetailsGroup.setLayoutData(groupFormat);
        recordsetDetailsGroup.setSize(recordsetDetailsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        recordsetDetailsGroup.layout();


        this.outputFormat = buildText("Format", null, lsMod, middle, margin, recordsetDetailsGroup);
        this.outputType = buildCombo("Type", this.outputFormat, lsMod, middle, margin, recordsetDetailsGroup,new String[]{"", "File", "File - CSV", "File - XML", "Piped", "Named"});
        
        if(input.getOutputFormat() != null){
            this.outputFormat.setText(input.getOutputFormat());
        }
        if(input.getOutputFormat() != null){
            this.outputType.setText(input.getOutputType());
            //disposeFileTypeGroup();
            if(!outputType.getText().equals("")){
                createFileTypeGroup(outputType.getText(), c1, groupLayout, lsMod);
            }
            c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            c1.layout();
        }
        
        this.outputType.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                           disposeFileTypeGroup();
                            
                            if(!outputType.getText().equals("")){
                                createFileTypeGroup(outputType.getText(), c1, groupLayout, lsMod);
                            }
                        c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                        c1.layout();
                    };
            });
       
    }
    
    private void disposeFileTypeGroup(){
         if(this.file != null){
            this.file.dispose();
        }
        if(this.fileOptions != null ){
            this.fileOptions.dispose();
        }
        if(this.typeOptions != null ){
            this.typeOptions.dispose();
        }
        if(this.cluster != null ){
            this.cluster.dispose();
        }
        if(this.encrypt != null ){
            this.encrypt.dispose();
        }
        if(this.overwrite != null){
            this.overwrite.dispose();
        }
        if(this.expire != null ){
            this.expire.dispose();
        }
        if(this.pipeType != null ){
            this.pipeType.dispose();
        }
        if(this.repeat != null ){
            this.repeat.dispose();
        }
        if(this.extend != null ){
            this.extend.dispose();
        }
        if(this.returnAll != null ){
            this.returnAll.dispose();
        }
        if(this.compressed != null ){
            this.compressed.dispose();
        }
        if(this.fileTypeGroup != null){
            this.fileTypeGroup.dispose();
        }
        
    }
    private void createFileTypeGroup(String type, Composite parent, FormLayout groupLayout, ModifyListener lsMod) {
        int middle = 45;
        final int margin = 20;
        fileTypeGroup = new Group(parent, SWT.SHADOW_NONE);
        fileTypeGroup.setBackground(new Color(fileTypeGroup.getDisplay(),255,255,255));
        fileTypeGroup.setText(type + " Details");
        fileTypeGroup.setLayout(groupLayout);
        GridData groupFormat = new GridData(SWT.FILL);
        groupFormat.minimumWidth = 530;
        
        groupFormat.widthHint = 530;
        
        int height = 320;
       // {"", "File", "File - CSV", "File - XML", "Piped", "Named"}
        if(type.equals("File")){
            //height = 105;
            this.file = buildText("File", null, lsMod, middle, margin, fileTypeGroup);
            //this.fileOptions = buildText("Thor File Options", this.file, lsMod, middle, margin, fileTypeGroup);
            this.cluster = buildText("Target Cluster", this.file, lsMod, middle, margin, fileTypeGroup);
            this.encrypt = buildText("Encryption Key", this.cluster, lsMod, middle, margin, fileTypeGroup);
            this.compressed = buildCombo("Compress", this.encrypt, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
            this.overwrite = buildCombo("Overwrite", this.compressed, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
            this.expire = buildText("Days till expire", this.overwrite, lsMod, middle, margin, fileTypeGroup);
            
        }else if(type.equals("File - CSV")){
            height = 300;
            this.file = buildText("File", null, lsMod, middle, margin, fileTypeGroup);
            //this.fileOptions = buildText("CSV File Options", this.file, lsMod, middle, margin, fileTypeGroup);
            this.typeOptions = buildText("CSV Options", this.file, lsMod, middle, margin, fileTypeGroup);
            this.cluster = buildText("Target Cluster", this.typeOptions, lsMod, middle, margin, fileTypeGroup);
            this.encrypt = buildText("Encryption Key", this.cluster, lsMod, middle, margin, fileTypeGroup);
            this.overwrite = buildCombo("Overwrite", this.encrypt, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
            this.expire = buildText("Days till expire", this.overwrite, lsMod, middle, margin, fileTypeGroup);
            
        }else if(type.equals("File - XML")){
            height = 300;
            this.file = buildText("File", null, lsMod, middle, margin, fileTypeGroup);
            //this.fileOptions = buildText("XML File Options", this.file, lsMod, middle, margin, fileTypeGroup);
            this.typeOptions = buildText("XML Options", this.file, lsMod, middle, margin, fileTypeGroup);
            this.cluster = buildText("Target Cluster", this.typeOptions, lsMod, middle, margin, fileTypeGroup);
            this.encrypt = buildText("Encryption Key", this.cluster, lsMod, middle, margin, fileTypeGroup);
            this.overwrite = buildCombo("Overwrite", this.encrypt, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
            this.expire = buildText("Days till expire", this.overwrite, lsMod, middle, margin, fileTypeGroup);
        }else if (type.equals("Piped")){
            height = 165;
            this.typeOptions = buildText("Pipe Options", null, lsMod, middle, margin, fileTypeGroup);
            this.pipeType = buildCombo("Type", this.typeOptions, lsMod, middle, margin, fileTypeGroup,new String[]{"", "CSV", "XML"});
            this.repeat = buildCombo("Repeat", this.pipeType, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
        }else if (type.equals("Named")){
            height = 165;
            this.named = buildText("Name", null, lsMod, middle, margin, fileTypeGroup);
            this.extend = buildCombo("Extend", this.named, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
            this.returnAll = buildCombo("All", this.extend, lsMod, middle, margin, fileTypeGroup,new String[]{"", "Yes", "No"});
        }
        
        groupFormat.minimumHeight = height;
        groupFormat.heightHint = height;
        fileTypeGroup.setLayoutData(groupFormat);
        fileTypeGroup.setSize(fileTypeGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        fileTypeGroup.layout();

        if(this.file != null && !this.file.isDisposed() &&  input.getFile() != null){
            this.file.setText(input.getFile());
        }
        if(this.fileOptions != null && !this.fileOptions.isDisposed() && input.getFileOptions() != null){
            this.fileOptions.setText(input.getFileOptions());
        }
        if(this.typeOptions != null && !this.typeOptions.isDisposed() && input.getTypeOptions() != null){
            this.typeOptions.setText(input.getTypeOptions());
        }
        if(this.cluster != null && !this.cluster.isDisposed() && input.getCluster() != null){
            this.cluster.setText(input.getCluster());
        }
        if(this.encrypt != null && !this.encrypt.isDisposed() && input.getEncrypt() != null){
            this.encrypt.setText(input.getEncrypt());
        }
        if(this.overwrite != null && !this.overwrite.isDisposed() && input.getOverwrite() != null){
            this.overwrite.setText(input.getOverwrite());
        }
        if(this.expire != null && !this.expire.isDisposed() && input.getExpire() != null){
            this.expire.setText(input.getExpire());
        }
        if(this.pipeType != null && !this.pipeType.isDisposed() && input.getPipeType() != null){
            this.pipeType.setText(input.getPipeType());
        }
        if(this.repeat != null && !this.repeat.isDisposed() && input.getRepeat() != null){
            this.repeat.setText(input.getRepeat());
        }
        if(this.extend != null && !this.extend.isDisposed() && input.getExtend() != null){
            this.extend.setText(input.getExtend());
        }
        if(this.returnAll != null && !this.returnAll.isDisposed() && input.getReturnAll() != null){
            this.returnAll.setText(input.getReturnAll());
        }
        if(this.compressed != null && !this.compressed.isDisposed() && input.getCompressed() != null){
            this.compressed.setText(input.getCompressed());
        }
        if(this.named != null && !this.named.isDisposed() && input.getNamed() != null ){
           // System.out.println("read anmed from file:" + input.getNamed() + "|");
            this.named.setText(input.getNamed());
        }

        
        
       
    }
    ////////////////////////////////////////////////////////////
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

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
    	 //input.setName(inputName.getText());
    	super.stepname = stepnameField.getText();
         AutoPopulateSteps ap = new AutoPopulateSteps();
         String serverHost = "";
         String serverPort = "";
             try{
             //Object[] jec = this.jobMeta.getJobCopies().toArray();
                 
                 serverHost = ap.getGlobalVariable(this.transMeta.getSteps(),"server_ip");
                serverPort = ap.getGlobalVariable(this.transMeta.getSteps(),"server_port");
             }catch (Exception e){
                 System.out.println("Error Parsing existing Global Variables ");
                 System.out.println(e.toString());
                 
             }
             
////         input.setServerAddress(serverHost);
////         input.setServerPort(serverPort);
         if(this.stepnameField != null && !this.stepnameField.isDisposed()){
             input.setStepName(this.stepnameField.getText());
         }else{
             input.setStepName("");
         }
         if(this.attributeName != null && !this.attributeName.isDisposed()){
             input.setAttributeName(this.attributeName.getText());
         }else{
             input.setAttributeName("");
         }
         if(this.isDef != null && !this.isDef.isDisposed()){
             input.setIsDef(this.isDef.getText());
         }else{
             input.setIsDef("");
         }
         if(this.inputType != null && !this.inputType.isDisposed()){
             input.setInputType(this.inputType.getText());
         }else{
             input.setInputType("");
         }
         //if(this.includeFormat != null && !this.includeFormat.isDisposed()){
         //    input.setIncludeFormat(this.includeFormat.getText());
         //}else{
         //    input.setIncludeFormat("");
         //}
         if(this.outputType != null && !this.outputType.isDisposed()){
             input.setOutputType(this.outputType.getText());
         }else{
             input.setOutputType("");
         }
         if(this.outputFormat != null && !this.outputFormat.isDisposed()){
             input.setOutputFormat(this.outputFormat.getText());
         }else{
             input.setOutputFormat("");
         }
         if(this.expression != null && !this.expression.isDisposed()){
             input.setExpression(this.expression.getText());
         }else{
             input.setExpression("");
         }
         if(this.file != null && !this.file.isDisposed()){
             input.setFile(this.file.getText());
         }else{
             input.setFile("");
         }
         if(this.typeOptions != null && !this.typeOptions.isDisposed()){
             input.setTypeOptions(this.typeOptions.getText());
         }else{
             input.setTypeOptions("");
         }
         if(this.fileOptions != null && !this.fileOptions.isDisposed()){
             input.setFileOptions(this.fileOptions.getText());
         }else{
             input.setFileOptions("");
         }
         if(this.named != null && !this.named.isDisposed()){
             input.setNamed(this.named.getText());
         }else{
             input.setNamed("");
         }
         if(this.extend != null && !this.extend.isDisposed()){
             input.setExtend(this.extend.getText());
         }else{
             input.setExtend("");
         }
         if(this.returnAll != null && !this.returnAll.isDisposed()){
             input.setReturnAll(this.returnAll.getText());
         }else{
             input.setReturnAll("");
         }
         if(this.thor != null && !this.thor.isDisposed()){
             input.setThor(this.thor.getText());
         }else{
             input.setThor("");
         }
         if(this.cluster != null && !this.cluster.isDisposed()){
             input.setCluster(this.cluster.getText());
         }else{
             input.setCluster("");
         }
         if(this.encrypt != null && !this.encrypt.isDisposed()){
             input.setEncrypt(this.encrypt.getText());
         }else{
             input.setEncrypt("");
         }
         if(this.compressed != null && !this.compressed.isDisposed()){
             input.setCompressed(this.compressed.getText());
         }else{
             input.setCompressed("");
         }
         if(this.overwrite != null && !this.overwrite.isDisposed()){
             input.setOverwrite(this.overwrite.getText());
         }else{
             input.setOverwrite("");
         }
         if(this.expire != null && !this.expire.isDisposed()){
             input.setExpire(this.expire.getText());
         }else{
             input.setExpire("");
         }
         if(this.repeat != null && !this.repeat.isDisposed()){
             input.setRepeat(this.repeat.getText());
         }else{
             input.setRepeat("");
         }
         if(this.pipeType != null && !this.pipeType.isDisposed()){
             input.setPipeType(this.pipeType.getText());
         }else{
             input.setPipeType("");
         }
         


         
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
