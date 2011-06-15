package com.clementscode.mmi.swing.crud;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

// From http://www.rgagnon.com/javadetails/java-0203.html
public class CustomCellRenderer implements ListCellRenderer {
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component component = (Component) value;
		component.setBackground(isSelected ? Color.black : Color.white);
		component.setForeground(isSelected ? Color.white : Color.black);
		return component;
	}
}