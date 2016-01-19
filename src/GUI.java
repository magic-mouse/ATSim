import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import jssc.SerialPort;
import jssc.SerialPortList;

import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUI extends JFrame {

	JComboBox comboBox;
	JTextArea textArea;
	
	public GUI() throws HeadlessException {
		setup();
	}

	private void setup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		//splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		panel_1.add(textArea_1, BorderLayout.CENTER);
		
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea_1));
		System.setOut(printStream);
		System.setErr(printStream);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("COM port:");
		panel_2.add(lblNewLabel, BorderLayout.WEST);
		
		String[] portNames = SerialPortList.getPortNames();
		
		comboBox = new JComboBox(portNames);
	
		panel_2.add(comboBox, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		panel_2.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFiles = new JMenu("File");
		menuBar.add(mnFiles);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(getComponent(0)) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				 
				  loadFile(file);
				  
				}
				
			}
		});
		mnFiles.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(getComponent(0)) == JFileChooser.APPROVE_OPTION) {
					 
					  try {
						  File file = fileChooser.getSelectedFile();
						  FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
						textArea.write(fw);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		});
		mnFiles.add(mntmSave);

		
		JButton btnNewButton = new JButton("Run simulation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Simulation is running:");
				Simulator s = new Simulator();
				s.Com = comboBox.getSelectedItem().toString();
				s.Program = textArea.getText();
				
				Thread t = new Thread(s);
				t.run();
				
				
				while (t.getState() == Thread.State.TERMINATED);
				System.out.println("Program finished");
			}
		});
		getContentPane().add(btnNewButton, BorderLayout.SOUTH);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setResizeWeight(0.5);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane);
		scrollPane.setViewportView(panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		splitPane_1.setRightComponent(scrollPane_1);
		scrollPane_1.setViewportView(panel_1);
		getContentPane().add(splitPane_1, BorderLayout.CENTER);
	}

	protected void loadFile(File file) {
		FileReader fr;
		try {
			fr = new FileReader(file);
			textArea.read(fr, file);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}

	public GUI(GraphicsConfiguration arg0) {
		super(arg0);
		setup();
	}

	public GUI(String arg0) throws HeadlessException {
		super(arg0);
		setup();
	}

	public GUI(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		setup();
	}

}
