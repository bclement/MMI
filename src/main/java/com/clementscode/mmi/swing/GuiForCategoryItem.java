package com.clementscode.mmi.swing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.sound.SoundRunner;

public class GuiForCategoryItem extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String BROWSE_AUDIO_FILE = "BROWSE_AUDIO_FILE";
	private static final String BROWSE_IMAGE_FILE = "BROWSE_IMAGE_FILE";
	private static final String PLAY_SOUND = "PLAY_SOUND";
	private CrudFrame crudFrame;
	private JTextField tfRepeatCount;
	private JTextField tfImageFileName;
	private ImageIcon imageIcon;
	private JTextField tfAudioFileName;
	private JButton play;

	public GuiForCategoryItem(CrudFrame al) {
		super();
		this.crudFrame = al;
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
		play = new JButton("Play");
		play.setActionCommand(PLAY_SOUND);
		play.addActionListener(this);
		add(play);
		browse = new JButton("Browse...");
		browse.setActionCommand(BROWSE_AUDIO_FILE);
		browse.addActionListener(this);
		add(browse);
		addButton("Add Another", CrudFrame.ADD_CATEGORY_ITEM);
		addButton("Delete", CrudFrame.DELETE_CATEGORY_ITEM);

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Category Item");
		setBorder(title);
	}

	public String getImageFileName() {
		return tfImageFileName.getText();
	}

	private void addButton(String text, String cmd) {
		JButton b = new JButton(text);
		Action actionForCategoryItemGui = new ActionForCategoryItemGui(cmd,
				this, text);
		b.setAction(actionForCategoryItemGui);
		// b.setAc
		// b.addActionListener(actionListener);
		add(b);
	}

	public void actionPerformed(ActionEvent e) {
		File file;

		if (PLAY_SOUND.equals(e.getActionCommand())) {
			File soundFile = new File(tfAudioFileName.getText());

			new Thread(new SoundRunner(soundFile)).start();

		} else {

			JFileChooser chooser = null;
			FileNameExtensionFilter filter = null;
			if (BROWSE_AUDIO_FILE.equals(e.getActionCommand())) {
				filter = new FileNameExtensionFilter("WAV Sound clips only...",
						"wav");
				chooser = new JFileChooser(new File(tfAudioFileName.getText()));
			} else if (BROWSE_IMAGE_FILE.equals(e.getActionCommand())) {
				filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg",
						"gif");
				chooser = new JFileChooser(new File(tfImageFileName.getText()));
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
						imageIcon
								.setImage(getScaledImage(ii.getImage(), 32, 32));
					}
				} catch (Exception bland) {
					bland.printStackTrace();
				}
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

	public void populate(CategoryItem categoryItem) {
		try {
			tfAudioFileName.setText(categoryItem.getAudio().getCanonicalPath());
			tfImageFileName.setText(categoryItem.getImgFile()
					.getCanonicalPath());
			imageIcon.setImage(getScaledImage(categoryItem.getImg(), 32, 32));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public CrudFrame getCrudFrame() {
		return crudFrame;
	}

	public void setCrudFrame(CrudFrame crudFrame) {
		this.crudFrame = crudFrame;
	}

}
