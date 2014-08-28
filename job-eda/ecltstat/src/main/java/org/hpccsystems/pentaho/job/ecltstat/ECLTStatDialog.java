/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltstat;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;
import org.hpccsystems.recordlayout.RecordList;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * 
 * @author KeshavS
 */
public class ECLTStatDialog extends ECLJobEntryDialog {// extends JobEntryDialog
														// implements
														// JobEntryDialogInterface
														// {

	private ECLTStat jobEntry;
	private Text jobEntryName;
	private Combo X1;
	private Combo X2;
	private Combo Paired;
	private Combo Variance;
	private Text mew;
	private Combo datasetName;
	private Button wOK, wCancel;
	private boolean backupChanged;
	@SuppressWarnings("unused")
	private SelectionAdapter lsDef;

	public ECLTStatDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLTStat) jobEntryInt;
		if (this.jobEntry.getName() == null) {
			this.jobEntry.setName("Student's T Statistic");
		}
	}

	public JobEntryInterface open() {
		Shell parentShell = getParent();
		Display display = parentShell.getDisplay();

		String datasets[] = null;
		final AutoPopulate ap = new AutoPopulate();
		try {
			// Object[] jec = this.jobMeta.getJobCopies().toArray();

			datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
			// defJobName = ap.getGlobalVariable(this.jobMeta.getJobCopies(),
			// "jobName");
		} catch (Exception e) {
			System.out.println("Error Parsing existing Datasets");
			System.out.println(e.toString());
			datasets = new String[] { "" };
		}

		shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN
				| SWT.MAX);

		props.setLook(shell);
		JobDialog.setShellImage(shell, jobEntry);

		final ModifyListener lsMod = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				jobEntry.setChanged();
			}
		};

		backupChanged = jobEntry.hasChanged();

		FormLayout formLayout = new FormLayout();
		// formLayout.marginWidth = 100;
		// formLayout.marginHeight = 180;
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		final int middle = props.getMiddlePct();
		final int margin = Const.MARGIN;

		shell.setLayout(formLayout);
		shell.setText("Student's T Statistic");

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
		generalGroupFormat.height = 50;
		generalGroupFormat.left = new FormAttachment(middle, 0);
		generalGroupFormat.right = new FormAttachment(100, 0);
		generalGroup.setLayoutData(generalGroupFormat);

		jobEntryName = buildText("Job Entry Name :", null, lsMod, middle,
				margin, generalGroup);

		// All other contols
		// Dataset Declaration
		final Group datasetGroup = new Group(shell, SWT.SHADOW_NONE);
		props.setLook(datasetGroup);
		datasetGroup.setText("Dataset Details");
		datasetGroup.setLayout(groupLayout);
		FormData datasetGroupFormat = new FormData();
		datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
		datasetGroupFormat.width = 400;
		datasetGroupFormat.height = 235;
		datasetGroupFormat.left = new FormAttachment(middle, 0);
		datasetGroupFormat.right = new FormAttachment(100, 0);
		datasetGroup.setLayoutData(datasetGroupFormat);

		datasetName = buildCombo("Dataset Name :", jobEntryName, lsMod, middle,margin, datasetGroup, datasets);

		X1 = buildCombo("Var1 :", datasetName, lsMod, middle,margin, datasetGroup, new String[] {});
		X2 = buildCombo("Var2 :", X1, lsMod, middle, margin,datasetGroup, new String[] {});
		Variance = buildCombo("Assumption :", X2, lsMod, middle, margin,datasetGroup, new String[] { "Equal Variance", "Unequal Variance" });
		Paired = buildCombo("Dependance :", Variance, lsMod, middle, margin,datasetGroup, new String[] { "Paired", "Unpaired" });
		Paired.setText("Unpaired");
		mew = buildText("Test Value :", Paired,lsMod, middle, margin, datasetGroup);
		mew.setEnabled(false);

		wOK = new Button(shell, SWT.PUSH);
		wOK.setText("OK");
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText("Cancel");

		BaseStepDialog.positionBottomButtons(shell,new Button[] { wOK, wCancel }, margin, datasetGroup);

		datasetName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				
				if (datasetName.getText() != null || !datasetName.getText().equals("")) {
					try {

						// String[] items = ap.fieldsByDataset(
						// datasetName.getText(),jobMeta.getJobCopies());
						RecordList rec = ap.rawFieldsByDataset(
								datasetName.getText(), jobMeta.getJobCopies());
						String[] items = new String[rec.getRecords().size()];
						for (int i = 0; i < rec.getRecords().size(); i++) {
							items[i] = rec.getRecords().get(i).getColumnName().toLowerCase();
						}
						
						X1.setItems(items);
						X2.setItems(items);

					} catch (Exception ex) {
						System.out.println("failed to load record definitions");
						System.out.println(ex.toString());
						ex.printStackTrace();
					}
				}
			}

		});
		
		X2.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				if(X2.getText().equals("0") || X2.getText().equals("")){
					Paired.setText("Unpaired");
					Paired.setEnabled(false);
					Variance.setText("Equal Variance");
					Variance.setEnabled(false);
				}
				else{
					Paired.setText("");
					Paired.setEnabled(true);
					Variance.setText("");
					Variance.setEnabled(true);
				}
				
			}
			
		});
		
		Paired.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {				
				if(Paired.getText().equalsIgnoreCase("Paired")){
					mew.setEnabled(true);
				}
					
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

		// Detect X or ALT-F4 or something that kills this window...

		shell.addShellListener(new ShellAdapter() {

			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});

		if (jobEntry.getName() != null) {
			jobEntryName.setText(jobEntry.getName());
		}
		
		if (jobEntry.getDatasetName() != null) {
			datasetName.setText(jobEntry.getDatasetName());
		}

		if (jobEntry.getX1() != null) {
			X1.setText(jobEntry.getX1());
		}
		
		if (jobEntry.getX2() != null) {
			X2.setText(jobEntry.getX2());
		}
		
		if (jobEntry.getPaired() != null) {
			Paired.setText(jobEntry.getPaired());
		}
		
		if (jobEntry.getVariance() != null) {
			Variance.setText(jobEntry.getVariance());
		}
		
		if (!jobEntry.getMew().equals("")) {
			mew = buildText("Test Value :", Paired,lsMod, middle, margin, datasetGroup);
			mew.setText(jobEntry.getMew());
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

	private boolean validate() {
		boolean isValid = true;
		String errors = "";

		if (this.jobEntryName.getText().equals("")) {
			isValid = false;
			errors += "\"Job Entry Name\" is a required field!\r\n";
		}

		if (this.datasetName.getText().equals("")) {
			isValid = false;
			errors += "\"Dataset Name\" is a required field!\r\n";
		}
		
		if (this.X1.getText().equals("")) {
			isValid = false;
			errors += "Select the First Variable!\r\n";
		}
		
		if (this.X2.getText().equals("")) {
			isValid = false;
			errors += "Select the First Variable!\r\n";
		}
		
		if (this.X2.getText().equalsIgnoreCase(this.X1.getText())) {
			isValid = false;
			errors += "Select the First Variable!\r\n";
		}
		
		/*if (this.Paired.getText().equals("")) {
			isValid = false;
			errors += "\"Dependance\" is a required field!\r\n";
		}*/
		
		if (this.Variance.getText().equals("")) {
			isValid = false;
			errors += "\"Variance Assumption\" is a required field!\r\n";
		}

		if (!isValid) {
			ErrorNotices en = new ErrorNotices();
			errors += "\r\n";
			errors += "If you continue to save with errors you may encounter compile errors if you try to execute the job.\r\n\r\n";
			isValid = en.openValidateDialog(getParent(), errors);
		}
		return isValid;

	}

	private void ok() {
		if (!validate()) {
			return;
		}

		jobEntry.setName(jobEntryName.getText());
		jobEntry.setDatasetName(datasetName.getText());
		jobEntry.setX1(X1.getText());
		jobEntry.setX2(X2.getText());
		jobEntry.setPaired(Paired.getText());
		jobEntry.setVariance(Variance.getText());
		if(mew.getText() != null)
			jobEntry.setMew(mew.getText());
		else
			jobEntry.setMew("");
		
		dispose();
	}

	private void cancel() {
		jobEntry.setChanged(backupChanged);
		jobEntry = null;
		dispose();
	}

}
