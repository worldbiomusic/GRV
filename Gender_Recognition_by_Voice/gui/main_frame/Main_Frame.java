package gui.main_frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import csv_from_wav.Csv_from_Wav;
import grv.GRV;

public class Main_Frame extends JFrame{
	
	// voice path
	//C:\Users\ljh99\Desktop\GRV\Gender_Recognition_by_Voice\grv\dataset\input_data\voice
	
	// out packages
	GRV grv;
	
	Csv_from_Wav cfw;
	
	
	// swing components
	Container c;
	
	FileChooser_GRV chooser;
	
	Dialog_GRV dialog_output;
	
	
	// north
	JLabel label_file_name;
	
	// west
	JButton btn_input_file;
	
	// center
	JButton btn_run_GRV;
	
	//listener
	Listener_btn_main_frame listener_btn;
	
	File f;
	
	int ret;
	
	void init_frame()
	{
		setTitle("GRV");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
	}
	
	void init_neural_network()
	{
		// make GRV
		grv = new GRV();
		cfw = new Csv_from_Wav();
		
		// initialize neural network
		grv.init_nerual_network();
		grv.run_training_stage();
		grv.run_testing_stage();
		
		btn_input_file.setEnabled(true);
	}
	
	void init_component()
	{
		// get, set contentpane
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		// make file chooser
		chooser = new FileChooser_GRV("only wav", "wav");
		
		// make dialog
		dialog_output = new Dialog_GRV(this, "output");
				
		// north
		label_file_name = new JLabel("File: ");
		label_file_name.setFont(new Font("Arial", Font.BOLD, 40));
		
		// west
		btn_input_file = new JButton("Click to input voice file");
		btn_input_file.setEnabled(false);
		
		// center
		btn_run_GRV = new JButton("input file to GRV");
	}

	void init_listener()
	{
		listener_btn = new Listener_btn_main_frame();
	}
	
	
	void add_north()
	{
		c.add(label_file_name, BorderLayout.NORTH);
	}
	
	void add_west()
	{
		// add listener
		btn_input_file.addActionListener(listener_btn);
		
		c.add(btn_input_file, BorderLayout.WEST);
	}
	
	void add_center()
	{
		// add listener
		try {
			btn_run_GRV.addActionListener(listener_btn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		c.add(btn_run_GRV);
	}
	
	void run()
	{
		
		
		// initialize frame
		init_frame();
		
		// initialize components
		init_component();
		
		// initialize listener
		init_listener();
		
		// add north, west, center
		add_north();
		add_west();
		add_center();
		
		// set visible
		setVisible(true);
		
		// initialize neural network
		init_neural_network();
	}
	
	class Listener_btn_main_frame implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton b = (JButton)e.getSource();
			
			if(b == btn_input_file)
			{
				ret = chooser.showOpenDialog(null);
				
				if(ret == JFileChooser.APPROVE_OPTION)
				{
					f = chooser.getSelectedFile();
					label_file_name.setText("File: " + f.getName());
				}
			}
			else if(b == btn_run_GRV)
			{
					cfw.convert();
					
					System.out.println("file converted to csv file");
					
					String output = grv.run_input_stage();
					
					dialog_output.set_output(output, grv.accuracy);
					
					// delete file in voice
					File[] files = new File("grv/dataset/input_data/voice").listFiles();
					for(File f : files)
						f.delete();
			}
				
			
		}
		
	}
	
	public static void main(String[] args) {
		new Main_Frame().run();
	}
}
