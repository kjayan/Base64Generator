package com.ui.generator.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class Base64Generator{

	protected Shell shell;
	private Text fileName;
	private Text resultTextArea;
	private Label errorMsgLabel;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Base64Generator window = new Base64Generator();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {

		Display display = Display.getDefault();
		createContents(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {

		shell = new Shell(display,SWT.TITLE|SWT.CLOSE|SWT.MIN);
		shell.setSize(606, 908);
		shell.setText("Base64 Generator");
		shell.setLayout(null);
		
		Listener keyboardListener = new Listener(){
			@Override
			public void handleEvent(Event event) {
				if (event.stateMask == SWT.CTRL && event.keyCode == 'a' ) {
				       ((Text)event.widget).selectAll();
				}
				else if (event.stateMask == SWT.CTRL && event.keyCode == 'c' ) {
				       ((Text)event.widget).copy();
				}
				else if (event.stateMask == SWT.CTRL && event.keyCode == 'v' ) {
				       ((Text)event.widget).paste();
				}
				else if (event.stateMask == SWT.CTRL && event.keyCode == 'x' ) {
				       ((Text)event.widget).cut();
				}
			}
		};
		
		fileName = new Text(shell, SWT.BORDER);
		fileName.setBounds(38, 57, 449, 21);

		
		Button chooseFileBtn = new Button(shell, SWT.NONE);
		chooseFileBtn.setBounds(493, 53, 75, 25);
		chooseFileBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Choose File button selected");
				String selectedFile = new FileDialog(shell).open();
				if (selectedFile != null) {
					fileName.setText(selectedFile);
				}
			}
		});
		chooseFileBtn.setText("Choose File");
		
		Button generateBtn = new Button(shell, SWT.NONE);
		generateBtn.setBounds(265, 103, 75, 25);
		generateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Generate Button clicked");
				parseFileAndGenerate();
			}
		});
		generateBtn.setText("Generate");
		
		resultTextArea = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		resultTextArea.setBounds(38, 148, 530, 712);
		resultTextArea.addListener(SWT.KeyUp,keyboardListener);
		
		Label headerLabel = new Label(shell, SWT.NONE);
		headerLabel.setBounds(38, 20, 530, 31);
		headerLabel.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 14, SWT.BOLD));
		headerLabel.setAlignment(SWT.CENTER);
		headerLabel.setText("Base64 Generator");
		
		errorMsgLabel = new Label(shell, SWT.NONE);
		errorMsgLabel.setBounds(378, 103, 190, 25);
		errorMsgLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		errorMsgLabel.setFont(SWTResourceManager.getFont("Trebuchet MS", 10, SWT.BOLD | SWT.ITALIC));


	}
	
	private void parseFileAndGenerate(){
		resultTextArea.setText("");
		errorMsgLabel.setText("");
		File file = new File(fileName.getText());
		StringBuilder fileContent = new StringBuilder();
		FileInputStream fIStream = null;
		try{
			fIStream = new FileInputStream(file);
			System.out.println("Size of File :"+fIStream.available());
			int content = 0;
			while((content = fIStream.read()) != -1){
				fileContent.append((char)content);
			}
			
		}catch(IOException e){
			System.out.println("Invalid File");
			errorMsgLabel.setText("Please choose a valid file");

		}finally{
			try{
				if(fIStream != null){
					fIStream.close();
				}
			}catch(IOException e){
				System.out.println("Error while closing FileInputStream");
			}
		}
		generateBase64Content(fileContent.toString());
	}
	
	private void generateBase64Content(String stringToConvert){
		String base64Content = DatatypeConverter.printBase64Binary(stringToConvert.getBytes());
		resultTextArea.setText(base64Content);
	}
}
