

package csv_from_wav;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Csv_from_Wav {
//	public static void main(String[] args) {
//		new Csv_from_Wav().convert();
//	}
	public void run()
	{
		Runtime r = Runtime.getRuntime();
	}
	
	public void convert() {
		try 
		{
			CmdTest.run_cmd();
			Thread.sleep(1000 * 5);
			remove_first_row();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	static void remove_first_row() throws Exception
	{
		File f = new File("C:\\Users\\ljh99\\Desktop\\GRV\\Gender_Recognition_by_Voice"
				+ "\\grv\\dataset\\input_data\\output\\voice.csv");
		
		FileReader fr = new FileReader(f);
		BufferedReader buf_read = new BufferedReader(fr);
		
		StringBuffer buf_str = new StringBuffer();
		
		// remove first row
		buf_read.readLine();
		
		System.out.println("go");
		String read;
		while((read = buf_read.readLine()) != null)
		{
			System.out.println(read);
			buf_str.append(read);
			buf_str.append("\n");
		}
		
		fr.close();
		buf_read.close();
		
		// start writing with second row
		FileWriter fw = new FileWriter(f);
		
		fw.write(buf_str.toString());
		
		fw.close();
		System.out.println("end");
	}
	
	// wav파일 복사과정에서 깨짐!! (제대로 복사하는 방법을 알아야 함)
//	public void copy_file_to_voice_file(File source) throws IOException {
//		System.out.println("start copy");
//		String fileName_src = source.getName();
//		System.out.println("voice file name: " + fileName_src);
//		File dest = new File("C:\\Users\\ljh99\\Desktop\\voice_r\\voice\\" + fileName_src);
//		
//	    InputStream is = null;
//	    OutputStream os = null;
//	    try {
//	        is = new FileInputStream(source);
//	        os = new FileOutputStream(dest);
//	        byte[] buffer = new byte[1024];
//	        int length;
//	        while ((length = is.read(buffer)) > 0) {
//	            os.write(buffer, 0, length);
//	        }
//	    } finally {
//	        is.close();
//	        os.close();
//	        System.out.println("end copy");
//	    }
//	}
}


class CmdTest {
    static void run_cmd() throws Exception
    {
		ProcessBuilder builder = new ProcessBuilder(
            "cmd.exe", "/c", "cd \"C:\\Users\\ljh99\\Desktop\\GRV\\Gender_Recognition_by_Voice\\csv_from_wav\" && rscript sound.R");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
}