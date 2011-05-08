package com.clementscode.mmi.swing;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelAndField extends JPanel {

	private static final long serialVersionUID = -6196415255254094804L;

	public LabelAndField(String strLabel, JComponent field) {
		super();
		add(new JLabel(strLabel));
		add(field);
	}

}
