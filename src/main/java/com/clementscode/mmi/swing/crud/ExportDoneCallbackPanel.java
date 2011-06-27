package com.clementscode.mmi.swing.crud;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class ExportDoneCallbackPanel implements ExportDoneCallback {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private JPanel panel;

	public ExportDoneCallbackPanel(JPanel panel) {
		this.panel = panel;
	}

	public void execute(JComponent source) {
		String str = source.getName();
		TitledBorder title = BorderFactory.createTitledBorder(str);
		panel.setBorder(title);
		log.info("Just set title to " + str);
	}

}
