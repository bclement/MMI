package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LabelAndField;
import com.clementscode.mmi.swing.LoggingFrame;



public class TriPanelCrud3 extends JFrame implements ExportDoneCallback {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JPanel mainPanel;
	private Vector vector;
	private ArrayList<JPanel> lstTriPanels;
	private ArrayList<DragableJLabelWithImage> lstImageSources;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new TriPanelCrud3();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public TriPanelCrud3() {
		super("TriPanelCrud3");
		vector = new Vector();
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(new JScrollPane(soundFilePanel()), BorderLayout.WEST);
		mainPanel.add(new JScrollPane(tripleStimulusPanel()),
				BorderLayout.CENTER);

		mainPanel.add(new JScrollPane(imageFilePanel()), BorderLayout.EAST);

		pack();
		setVisible(true);
	}


	private JPanel imageFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		String dirName = "/Users/mgpayne/resources/";

		visitAllFiles(new File(dirName));
		int row = 0;
		lstImageSources = new ArrayList<DragableJLabelWithImage>();
		int n=0;
		for (Object obj : vector) {
			ImageIcon ii = (ImageIcon) obj;
			// ExportDoneCallbackImageFilePanel cb = new
			// ExportDoneCallbackImageFilePanel(
			// lstTriPanels.get(n++));
			ExportDoneCallbackImageFilePanel cb = new ExportDoneCallbackImageFilePanel(
					null);
			DragableJLabelWithImage lbl = new DragableJLabelWithImage(
					cb);
			lbl.setIcon(ii);
			cb.setSource(row);
			lbl.setName(String.format("row=%d;%s", row++, ii.getDescription()));
			lstImageSources.add(lbl);

			panel.add(lbl);
		}


		return panel;
	}

	// Process only files under dir
	// Started with
	// from http://www.exampledepot.com/egs/java.io/TraverseTree.html
	public void visitAllFiles(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				visitAllFiles(new File(dir, children[i]));
			}
		} else {
			String fn = dir.getAbsolutePath();
			if (fn.indexOf("jpg") > 0) {
				ImageIcon smallIi = smallImageIcon(fn, 128, 128);
				vector.add(smallIi);
				smallIi.setDescription(fn);
			}
		}
	}

	static ImageIcon smallImageIcon(String fn, int i, int j) {
		ImageIcon ii = new ImageIcon(fn);
		Image smallImg = getScaledImage(ii.getImage(), 128,
				128);
		ImageIcon smallIi = new ImageIcon(smallImg);
		smallIi.setDescription(fn);
		return smallIi;
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
	public static Image getScaledImage(Image srcImg, int w, int h) {
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
	private JPanel tripleStimulusPanel() {
		JPanel panel = new JPanel();

		lstTriPanels = new ArrayList<JPanel>();

		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < 10; ++i) {
			JPanel triPanel = new JPanel();
			lstTriPanels.add(triPanel);
			TitledBorder title = BorderFactory.createTitledBorder("title");
			triPanel.setBorder(title);
			// triPanel.add(new LabelAndFieldWithIcon("Image: ",
			// new JTextField(20), this));
//			DragableJLabelWithImage lbl = new DragableJLabelWithImage(
//					new ExportDoneCallbackPanel(triPanel,
//							lstImageSources.get(i)));
			
			DragableJLabelWithImage lbl = new DragableJLabelWithImage(
					new ExportDoneCallbackPanel(triPanel, null));
			triPanel.add(lbl);
			String fn = "/Users/mgpayne/resources/people/plumber/Plumber3.jpg";
			ImageIcon smallIi = smallImageIcon(fn, 128, 128);
			lbl.setIcon(smallIi);
			lbl.setText("" + i);
			triPanel.add(new LabelAndField("Prompt: ", new JTextField(20)));
			triPanel.add(new LabelAndField("Answer: ", new JTextField(20)));
			panel.add(triPanel);
		}

		return panel;
	}

	private JPanel soundFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < 10; ++i) {
			// TODO: Make this a function or a class....
			JLabel label1 = new JLabel("sound file " + i);
			makeDragable(label1);

			panel.add(label1);
		}
		return panel;
	}

	private void makeDragable(JLabel label1) {
		// Specify that the label's text property be transferable; the value
		// of
		// this property will be used in any drag-and-drop involving this
		// label
		final String propertyName = "text";
		label1.setTransferHandler(new TransferHandler(propertyName));

		// Listen for mouse clicks
		label1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JComponent comp = (JComponent) evt.getSource();
				TransferHandler th = comp.getTransferHandler();

				// Start the drag operation
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});

	}

	void refreshGui() {

		mainPanel.revalidate();
		this.pack();
	}

	public void exportDone(String overWrittenComponent, JComponent source) {
		int n = 0;
		// TODO: Parse source to get the row and do the correct stuff...

		String str = source.getName();
		String[] lst = StringUtils.split(str, ";");
		String rowStr = lst[0];
		rowStr = rowStr.substring("row=".length());

		// rowStr = overWrittenComponent;
		n = Integer.parseInt(rowStr);
		// TODO: Fix bad logic! Need to get row of the drop site!
		JPanel p = lstTriPanels.get(n);
		TitledBorder title = BorderFactory.createTitledBorder(str);
		p.setBorder(title);

	}

	public void importData(JComponent comp) {
		// TODO Auto-generated method stub

	}

}
