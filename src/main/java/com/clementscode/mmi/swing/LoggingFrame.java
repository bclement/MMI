package com.clementscode.mmi.swing;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.PropertyConfigurator;

import com.zcage.log.TextAreaAppender;

public class LoggingFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea jTextArea;

	// Started with ideas from http://textareaappender.zcage.com/

	public LoggingFrame() {
		super("LoggingFrame");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jTextArea = new JTextArea(40, 80);
		TextAreaAppender.setTextArea(jTextArea);
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		JScrollPane jScrollPane = new JScrollPane(jTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(jScrollPane);

		Properties logProperties = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("log4j.ini");
		try {
			logProperties.load(in);
			PropertyConfigurator.configure(logProperties);
		} catch (IOException e) {
			// TODO Put big ugly message in the jTextArea....
			e.printStackTrace();
		}
		pack();
	}

}
