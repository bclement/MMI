package com.clementscode.mmi.swing.crud;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ExportDoneCallbackImageFilePanel implements ExportDoneCallback {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private int sourceRow;
	private JComponent destinationComponent;
	private JPanel jPanel;
	private String text;

	public ExportDoneCallbackImageFilePanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	public void exportDone(String overWrittenComponent, JComponent source2) {
		log.info(String.format("exportDone(%s,%s) sourceRow=%d text=%s",
				overWrittenComponent, source2, sourceRow, text));

	}

	public void importData(JComponent comp) {
		this.destinationComponent = comp;
		JLabel label = (JLabel) comp;
		this.text = label.getText();
		log.info(String.format("importData comp=" + comp));
	}

	public void setSource(int row) {
		this.sourceRow = row;
	}

}
