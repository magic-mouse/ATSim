import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.PrintStream;

import jssc.SerialPort;
import jssc.SerialPortList;
import javax.swing.JScrollPane;


public class GUI extends JFrame {

	JComboBox comboBox;
	JTextArea textArea;
	
	public GUI() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		splitPane.setResizeWeight(0.5);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
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
	
		
	}

	public GUI(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GUI(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GUI(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
