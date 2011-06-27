package com.clementscode.mmi.swing.crud;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class ExportDoneCallbackPanel implements ExportDoneCallback {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private JPanel panel;
	private DragableJLabelWithImage sourceDragableJLabelWithImage;

	public ExportDoneCallbackPanel(JPanel panel,
			DragableJLabelWithImage sourceDragableJLabelWithImage) {
		this.panel = panel;
		this.sourceDragableJLabelWithImage = sourceDragableJLabelWithImage;
	}

	public void exportDone(String overWrittenComponent, JComponent source) {
		String str = sourceDragableJLabelWithImage.getName();
		TitledBorder title = BorderFactory.createTitledBorder(str);
		panel.setBorder(title);
		log.info("Just set title to " + str);
	}

	public void importData(JComponent comp) {
		// TODO Auto-generated method stub

	}

}
