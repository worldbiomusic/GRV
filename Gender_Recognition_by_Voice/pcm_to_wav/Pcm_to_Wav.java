package pcm_to_wav;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Pcm_to_Wav {
	
	public static void main(String[] args) {
		
		Pcm_to_Wav p2w = new Pcm_to_Wav();
		File src = new File("C:\\Users\\ljh99\\Desktop\\voice file\\download_4_Korean_English_by_Korean_part2\\female"); 
		File dst = new File("pcm_to_wav/data_wav/female");
		
		int num = 0;
		//TODO: 대량으로 만들기
		File[] files_src = src.listFiles();  
		for(File f : files_src)
		{
			File dir_last = p2w.getLastDir(f);
			for(File file_voice : dir_last.listFiles())
			{
				if(p2w.isPcmFile(file_voice))
				{
					try 
					{
						p2w.rawToWave(file_voice, new File(dst.getPath() + "/voice_f_." + num++ + ".wav"));
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
				
		}
		
	}
	
	File getLastDir(File f)
	{
		for(File file : f.listFiles())
		{
			if(file.isDirectory())
				return getLastDir(file);
		}
		
		return f;
	}
	
	boolean isPcmFile(File f)
	{
		String name = f.getName();
		int len = name.length();
		
		String extension = name.substring(len - 3, len);
		
		return extension.equalsIgnoreCase("pcm");
	}
	
	private void rawToWave(final File rawFile, final File waveFile) throws IOException {

		byte[] rawData = new byte[(int) rawFile.length()];
		DataInputStream input = null;
		try {
		    input = new DataInputStream(new FileInputStream(rawFile));
		    input.read(rawData);
		} finally {
		    if (input != null) {
		        input.close();
		    }
		}

		DataOutputStream output = null;
		try {
		    output = new DataOutputStream(new FileOutputStream(waveFile));
		    // WAVE header
		    // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
		    writeString(output, "RIFF"); // chunk id
		    writeInt(output, 36 + rawData.length); // chunk size
		    writeString(output, "WAVE"); // format
		    writeString(output, "fmt "); // subchunk 1 id
		    writeInt(output, 16); // subchunk 1 size
		    writeShort(output, (short) 1); // audio format (1 = PCM)
		    writeShort(output, (short) 1); // number of channels
		    writeInt(output, 16000); // sample rate
		    writeInt(output, 16); // byte rate TODO: bit rate
		    writeShort(output, (short) 2); // block align
		    writeShort(output, (short) 16); // bits per sample
		    writeString(output, "data"); // subchunk 2 id
		    writeInt(output, rawData.length); // subchunk 2 size
		    // Audio data (conversion big endian -> little endian)
		    short[] shorts = new short[rawData.length / 2];
		    ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
		    ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
		    for (short s : shorts) {
		        bytes.putShort(s);
		    }

		    output.write(fullyReadFileToBytes(rawFile));
		} finally {
		    if (output != null) {
		        output.close();
		    }
		}
		}
		byte[] fullyReadFileToBytes(File f) throws IOException {
		int size = (int) f.length();
		byte bytes[] = new byte[size];
		byte tmpBuff[] = new byte[size];
		FileInputStream fis= new FileInputStream(f);
		try { 

		    int read = fis.read(bytes, 0, size);
		    if (read < size) {
		        int remain = size - read;
		        while (remain > 0) {
		            read = fis.read(tmpBuff, 0, remain);
		            System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
		            remain -= read;
		        } 
		    } 
		}  catch (IOException e){
		    throw e;
		} finally { 
		    fis.close();
		} 

		return bytes;
		} 
		private void writeInt(final DataOutputStream output, final int value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		output.write(value >> 16);
		output.write(value >> 24);
		}

		private void writeShort(final DataOutputStream output, final short value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		}

		private void writeString(final DataOutputStream output, final String value) throws IOException {
		for (int i = 0; i < value.length(); i++) {
		    output.write(value.charAt(i));
		    }
		}
}
