package gui.main_frame;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser_GRV extends JFileChooser{
	
	public FileChooser_GRV(String label, String extension)
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter(label, extension);
		
		setFileFilter(filter);
	}
}
