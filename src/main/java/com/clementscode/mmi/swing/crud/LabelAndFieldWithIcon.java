package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.clementscode.mmi.swing.LabelAndField;

public class LabelAndFieldWithIcon extends JPanel implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;
	private LabelAndField topPanel;
	private ImageIcon imgIcon;
	private JTextField tf;
	private JLabel lblImage;
	private TriPanelCrud1 gui;

	public LabelAndFieldWithIcon(String lblStr, JTextField tf, TriPanelCrud1 gui) {
		super();
		this.gui = gui;
		setLayout(new BorderLayout());
		topPanel = new LabelAndField(lblStr, tf);
		this.tf = tf;
		tf.addActionListener(this);
		tf.addFocusListener(this);
		add(topPanel, BorderLayout.NORTH);
		imgIcon = new ImageIcon();
		lblImage = new JLabel(imgIcon);
		add(lblImage, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e);
	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("focusGained: e=" + arg0);
	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("focusLost: e=" + arg0);
		JTextField tf = (JTextField) arg0.getSource();
		String imagePathStr = tf.getText();
		System.out.println("\ttf=" + imagePathStr);
		ImageIcon ii = gui.smallImageIcon(imagePathStr, 128, 128);
		lblImage.setIcon(ii);
		this.revalidate();
		// JPanel pp = (JPanel) this.getParent();
		// pp.invalidate();
		// pp.revalidate();
		gui.refreshGui();
		System.out.println("Just put ii in and refreshedGui()...ii=" + ii);
	}
}
