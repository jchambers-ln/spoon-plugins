package org.hpccsystems.pentaho.steps.eclexecute;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import org.hpccsystems.ecldirect.Column;
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

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ECLExecuteStepDialog extends BaseStepDialog implements StepDialogInterface {

	private ECLExecuteStepMeta input;
    private HashMap controls = new HashMap();
    
    private Text fileName;
    
    private Text stepnameField;
    private Button fileOpenButton;
   
    public ECLExecuteStepDialog(Shell parent, Object in, TransMeta transMeta, String stepName) {
        super(parent, (BaseStepMeta) in, transMeta, stepName);
        input = (ECLExecuteStepMeta) in;
<<<<<<< HEAD
=======
        if(stepName != null && !stepName.equals("")){
        	input.setStepName(stepName);
        }
>>>>>>> master
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
     //Output Declaration
     Group fileGroup = new Group(shell, SWT.SHADOW_NONE);
     props.setLook(fileGroup);
     fileGroup.setText("Configuration Details");
     fileGroup.setLayout(groupLayout);
     FormData fileGroupFormat = new FormData();
     fileGroupFormat.top = new FormAttachment(generalGroup, margin);
     fileGroupFormat.width = 400;
     fileGroupFormat.height = 100;
     fileGroupFormat.left = new FormAttachment(middle, 0);
     fileGroup.setLayoutData(fileGroupFormat);
     
     
     //this.serverAddress = buildText("Server Address", fileGroup, lsMod, middle, margin, fileGroup);
     //controls.put("serverAddress", serverAddress);
     
     this.fileName = buildText("Output File(s) Directory", fileGroup, lsMod, middle, margin, fileGroup);
     controls.put("fileName", fileName);
     
     this.fileOpenButton = buildButton("Choose Location", fileName, lsMod, middle, margin, fileGroup);
     controls.put("fOpen", fileOpenButton);
     
     Listener fileOpenListener = new Listener() {

         public void handleEvent(Event e) {
             String newFile = buildFileDialog();
             if(newFile != ""){
                 fileName.setText(newFile);
             }
         }
     };
     this.fileOpenButton.addListener(SWT.Selection, fileOpenListener);
     
     

        
        
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, fileGroup);

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
        	stepnameField.setText("Execute");
        }
        //add other set functions here
        if (input.getFileName() != null) {
            this.fileName.setText(input.getFileName());
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
<<<<<<< HEAD
=======
    	super.stepname = stepnameField.getText();
>>>>>>> master
    	//add other here
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
            
        input.setServerAddress(serverHost);
        input.setServerPort(serverPort);
        
        input.setFileName(this.fileName.getText());
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
    
    
    
    
    
    
    
    
    
    
    
    private Button buildButton(String strLabel, Control prevControl, 
            ModifyListener isMod, int middle, int margin, Composite groupBox){
       
           Button nButton = new Button(groupBox, SWT.PUSH | SWT.SINGLE | SWT.CENTER);
           nButton.setText(strLabel);
           props.setLook(nButton);
           //nButton.addModifyListener(lsMod)
           FormData fieldFormat = new FormData();
           fieldFormat.left = new FormAttachment(middle, 0);
           fieldFormat.top = new FormAttachment(prevControl, margin);
           fieldFormat.right = new FormAttachment(75, 0);
           fieldFormat.height = 25;
           nButton.setLayoutData(fieldFormat);
       
           return nButton;
           
          
   }
   private String buildFileDialog() {
       
       DirectoryDialog dialog = new DirectoryDialog(shell);
       dialog.setFilterPath("c:\\"); // Windows specific
       //System.out.println("RESULT=" + dialog.open());
       String selected = dialog.open();
       if(selected == null){
           selected = "";
       }
       return selected;
       /*
       //file field
           FileDialog fd = new FileDialog(shell, SWT.SAVE);

           fd.setText("Save");
           fd.setFilterPath("C:/");
           String[] filterExt = { "*.csv", ".xml", "*.txt", "*.*" };
           //fd.setFilterExtensions(filterExt);
           String selected = fd.open();
           if(fd.getFileName() != ""){
               return fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName();
           }else{
               return "";
           }
        * */

           
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

}
