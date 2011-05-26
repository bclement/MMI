package com.clementscode.mmi.swing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

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
	private static final String BROWSE_AUDIO_FILE = "BROWSE_AUDIO_FILE";
	private static final String BROWSE_IMAGE_FILE = "BROWSE_IMAGE_FILE";
	private ActionListener actionListender;
	private JTextField tfRepeatCount;
	private JTextField tfImageFileName;
	private ImageIcon imageIcon;
	private JTextField tfAudioFileName;

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
		browse.setActionCommand(BROWSE_IMAGE_FILE);
		browse.addActionListener(this);
		add(browse);
		tfAudioFileName = new JTextField(12);
		add(new LabelAndField("Audio:", tfAudioFileName));
		browse = new JButton("Browse...");
		browse.setActionCommand(BROWSE_AUDIO_FILE);
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
		File file;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = null;
		if (BROWSE_AUDIO_FILE.equals(e.getActionCommand())) {
			filter = new FileNameExtensionFilter("WAV Sound clips only...",
					"wav");
		} else if (BROWSE_IMAGE_FILE.equals(e.getActionCommand())) {
			filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg",
					"gif");
		}

		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				if (BROWSE_AUDIO_FILE.equals(e.getActionCommand())) {
					tfAudioFileName.setText(file.getCanonicalPath());
				} else if (BROWSE_IMAGE_FILE.equals(e.getActionCommand())) {
					tfImageFileName.setText(file.getCanonicalPath());
					ImageIcon ii = new ImageIcon(file.getCanonicalPath());
					imageIcon.setImage(getScaledImage(ii.getImage(), 32, 32));
				}
			} catch (Exception bland) {
				bland.printStackTrace();
			}
		}

	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * @param srcImg
	 *            - source image to scale
	 * @param w
	 *            - desired width
	 * @param h
	 *            - desired height
	 * @return - the new resized image
	 */
	private Image getScaledImage(Image srcImg, int w, int h) {
		// from:
		// http://download.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java

		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	private boolean set;

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

}
