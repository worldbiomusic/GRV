package gui.main_frame;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Dialog_GRV extends Dialog{

	JTextArea txt_area;
	JLabel label_output;
	JButton btn_ok;
	
	public Dialog_GRV(Frame owner, String title) {
		super(owner, title);
		
		// initialize dialog
		init_dialog();
		
		// initialize components
		init_component();
		
		// add components
		add_component();
	}
	
	void init_dialog()
	{
		// set layout
		setLayout(new GridLayout(3, 1));
		
		// set size
		setSize(200, 500);
	}
	
	void init_component()
	{
		txt_area = new JTextArea();
		
		ImageIcon icon = new ImageIcon("grv/male.png");
		label_output = new JLabel(icon);
		btn_ok = new JButton("OK");
		
		btn_ok.addActionListener( (e) -> {
			this.setVisible(false);
		});
	}
	
	void add_component()
	{
		add(label_output);
		add(txt_area);
		add(btn_ok);
	}
	
	void set_output(String output, double accuracy)
	{
		txt_area.setText("Accuracy: " + accuracy);
		
		if(output.contains("female"))
		{
			label_output.setIcon(new ImageIcon("grv/female.png"));
		}
		else if(output.contains("none"))
		{
			label_output.setIcon(new ImageIcon("grv/none.png"));
		}
		
		this.setVisible(true);
	}
	
}
