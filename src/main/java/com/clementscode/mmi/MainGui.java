/**
 * 
 */
package com.clementscode.mmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;
import com.clementscode.mmi.swing.Gui;

/**
 * @author bclement
 * 
 */
public class MainGui {

	// TODO: Get this out of the JAR file...
	public static String propFile = "config.properties";

	// TODO: Get this from the file open menu!
	// TODO: Change this to come from the current directory
	static String confFile = "src/test/resources/session.txt";

	public static String sndKey = "snd.exts";

	public static String imgKey = "img.exts";

	public static void main(String[] args) throws Exception {
		try {
			confFile = args.length > 1 ? args[0] : confFile;
			new MainGui().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public void run() throws Exception {
		Properties props = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(MainGui.propFile);
		props.load(new InputStreamReader(in));
		String[] sndExts = props.getProperty(sndKey).split(",");

		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);
		SessionConfig config = mapper.readValue(new FileInputStream(new File(
				confFile)), SessionConfig.class);
		Session session = new Session(config, sndExts);
		new Gui().run(session);
	}
}
