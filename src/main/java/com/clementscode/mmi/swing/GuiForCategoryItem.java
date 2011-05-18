package com.clementscode.mmi.swing;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GuiForCategoryItem extends JPanel {

	private ActionListener actionListender;

	public GuiForCategoryItem(ActionListener al) {
		super();
		this.actionListender = al;
		JTextField tfRepeatCount = new JTextField();
		add(new LabelAndField("Repeats: ", tfRepeatCount));
		addButton("Add Another");
		addButton("Delete");
	}

	private void addButton(String string) {
		JButton b = new JButton(string);
		b.addActionListener(actionListender);
		add(b);
	}

}
