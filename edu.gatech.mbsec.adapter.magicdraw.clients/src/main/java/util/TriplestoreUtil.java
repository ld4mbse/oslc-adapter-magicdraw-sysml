package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class TriplestoreUtil {
	
	 public static String configFilePath = "triplestore configuration/config.properties";
	
	public static String getTriplestoreLocation() {
		Properties prop = new Properties();
		

		// load property file content and convert backslashes into forward
		// slashes
		String str;
		String triplestoreDirectory = null;
		
			try {
				str = readFile(configFilePath, Charset.defaultCharset());
				prop.load(new StringReader(str.replace("\\", "/")));

				// get the property value
				triplestoreDirectory = prop.getProperty("triplestoreDirectory");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		return triplestoreDirectory;
	}
	
	public static String getTriplestoreLocation(String configLocation) {
		configFilePath = configLocation;
		return getTriplestoreLocation();
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return encoding.decode(ByteBuffer.wrap(encoded)).toString();
			}
}
