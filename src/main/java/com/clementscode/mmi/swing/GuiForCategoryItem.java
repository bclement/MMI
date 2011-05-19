package com.clementscode.mmi.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GuiForCategoryItem extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private ActionListener actionListender;
	private JTextField tfRepeatCount;
	private JTextField tfImageFileName;
	private ImageIcon imageIcon;

	public GuiForCategoryItem(ActionListener al) {
		super();
		this.actionListender = al;
		tfRepeatCount = new JTextField("1");
		add(new LabelAndField("Repeats: ", tfRepeatCount));
		tfImageFileName = new JTextField(12);
		add(new LabelAndField("Image:", tfImageFileName));
		imageIcon = new ImageIcon();
		add(new JButton(imageIcon));
		JButton browse = new JButton("Browse...");
		browse.addActionListener(this);
		add(browse);
		JTextField tfAudioFileName = new JTextField(12);
		add(new LabelAndField("Audio:", tfAudioFileName));
		browse = new JButton("Browse...");
		browse.addActionListener(this);
		add(browse);
		addButton("Add Another");
		addButton("Delete");

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Category Item");
		setBorder(title);
	}

	private void addButton(String string) {
		JButton b = new JButton(string);
		b.addActionListener(actionListender);
		add(b);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & GIF Images", "jpg", "gif");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: "
					+ chooser.getSelectedFile().getName());
		}

	}

}
