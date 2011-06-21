package com.clementscode.mmi.swing.crud;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LoggingFrame;

public class ImageFilePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	List<ImageDragAndDropPanel> imageDragAndDropPanels;
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame; // TODO: Remove loggingFrame
	/**
	 * <p>
	 * This represents the data that is transmitted in drag and drop.
	 * </p>
	 * <p>
	 * In our limited case with only 1 type of dropped item, it will be a panel
	 * object!
	 * </p>
	 * <p>
	 * Note DataFlavor can represent more than classes -- easily text, images,
	 * etc.
	 * </p>
	 */
	private static DataFlavor dragAndDropPanelDataFlavor = null;

	public static void main(String[] args) {
		try {
			new ImageFilePanel().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private void run() {


		JFrame frame = new JFrame("Test IFP");
		frame.getContentPane().add(new JScrollPane(this));
		frame.pack();
		frame.setVisible(true);
	}
	public ImageFilePanel() {
		super();
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		imageDragAndDropPanels = new ArrayList<ImageDragAndDropPanel>();
		// Need to keep reference so can later communicate with drop listener
		// TODO: this.demo = demo;

		// Again, needs to negotiate with the draggable object
		this.setTransferHandler(new DragAndDropTransferHandler());

		// Create the listener to do the work when dropping on this object!
		this.setDropTarget(new MyDropTarget(ImageFilePanel.this,
				new ImagePanelDropTargetListener(ImageFilePanel.this)));

		setLayout(new GridLayout(0, 1));
		Vector<ImageIcon> vector = new Vector<ImageIcon>();
		// TODO: Fix!
		Utils.visitAllFiles(new File("/Users/mgpayne/resources/"), vector);
		vector = justSix(vector);
		for (ImageIcon ii : vector) {
			ImageDragAndDropPanel iddp = new ImageDragAndDropPanel(ii);
			imageDragAndDropPanels.add(iddp);
			add(iddp);
		}
	}


	private Vector<ImageIcon> justSix(Vector<ImageIcon> vector) {
		Vector<ImageIcon> v = new Vector<ImageIcon>();

		for (int i = 0; i < 3; ++i) {
			v.add(vector.get(i));
		}

		return v;
	}

	/**
	 * <p>
	 * Returns (creating, if necessary) the DataFlavor representing
	 * RandomDragAndDropPanel
	 * </p>
	 * 
	 * @return
	 */
	public static DataFlavor getDragAndDropPanelDataFlavor() throws Exception {
		// Lazy load/create the flavor
		if (dragAndDropPanelDataFlavor == null) {
			dragAndDropPanelDataFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType
							+ ";class=com.clementscode.mmi.swing.crud.ImageDragAndDropPanel");
		}

		return dragAndDropPanelDataFlavor;
	}

	void refreshGui() {
		this.validate();
		this.repaint();
	}


	public List<ImageDragAndDropPanel> getImageDragAndDropPanels() {

		return imageDragAndDropPanels;
	}
}
