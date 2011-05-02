/**
 * 
 */
package com.clementscode.mmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;

/**
 * @author bclement
 * 
 */
public class Main {

	static String propFile = "src/main/resources/config.properties";

	static String confFile = "src/test/resources/session.txt";

	static String sndKey = "snd.exts";

	static String imgKey = "img.exts";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileReader(new File(propFile)));
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
		new SessionRunner().run(session);
	}
}
