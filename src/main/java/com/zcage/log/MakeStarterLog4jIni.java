package com.zcage.log;

import java.io.PrintWriter;
import java.util.Properties;

public class MakeStarterLog4jIni {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Properties logProperties = new Properties();
		logProperties.put("log4j.rootLogger", "INFO, CONSOLE, TEXTAREA");
		logProperties.put("log4j.appender.CONSOLE",
				"org.apache.log4j.ConsoleAppender"); // A standard console
														// appender
		logProperties.put("log4j.appender.CONSOLE.layout",
				"org.apache.log4j.PatternLayout"); // See:
													// http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
		logProperties.put("log4j.appender.CONSOLE.layout.ConversionPattern",
				"%d{HH:mm:ss} [%12.12t] %5.5p %40.40c: %m%n");

		logProperties.put("log4j.appender.TEXTAREA",
				"com.zcage.log.TextAreaAppender"); // Our custom appender
		logProperties.put("log4j.appender.TEXTAREA.layout",
				"org.apache.log4j.PatternLayout"); // See:
													// http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
		logProperties.put("log4j.appender.TEXTAREA.layout.ConversionPattern",
				"%d{HH:mm:ss} %5.5p %40.40c: %m%n");
		PrintWriter out = new PrintWriter("log4j.ini");
		for (Object str : logProperties.keySet()) {
			String key = (String) str;
			out.println(String.format("%s=%s", key, logProperties.get(key)));
		}
		out.close();
	}

}
