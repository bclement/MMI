package com.clementscode.mmi.swing.crud;

import javax.swing.JComponent;

public interface ExportDoneCallback {
	public void exportDone(String overWrittenComponent, JComponent source2);

	public void importData(JComponent comp);
}
