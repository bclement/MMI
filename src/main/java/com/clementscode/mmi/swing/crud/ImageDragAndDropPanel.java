package com.clementscode.mmi.swing.crud;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


public class ImageDragAndDropPanel extends JPanel implements Transferable {
	private Logger log = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDragAndDropPanel(ImageIcon ii) {
		super();
		// Add the listener which will export this panel for dragging
		this.addMouseListener(new DraggableMouseListener());

		// Add the handler, which negotiates between drop target and this
		// draggable panel
		this.setTransferHandler(new DragAndDropTransferHandler());

		add(new JLabel(ii));
	}

	/**
	 * <p>
	 * One of three methods defined by the Transferable interface.
	 * </p>
	 * <p>
	 * If multiple DataFlavor's are supported, can choose what Object to return.
	 * </p>
	 * <p>
	 * In this case, we only support one: the actual JPanel.
	 * </p>
	 * <p>
	 * Note we could easily support more than one. For example, if supports text
	 * and drops to a JTextField, could return the label's text or any arbitrary
	 * text.
	 * </p>
	 * 
	 * @param flavor
	 * @return
	 */
	public Object getTransferData(DataFlavor flavor) {

		log.info("Step 7 of 7: Returning the data from the Transferable object. In this case, the actual panel is now transfered!");

		DataFlavor thisFlavor = null;

		try {
			thisFlavor = ImageFilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		// For now, assume wants this class... see loadDnD
		if (thisFlavor != null && flavor.equals(thisFlavor)) {
			return ImageDragAndDropPanel.this;
		}

		return null;
	}

	/**
	 * <p>
	 * One of three methods defined by the Transferable interface.
	 * </p>
	 * <p>
	 * Returns supported DataFlavor. Again, we're only supporting this actual
	 * Object within the JVM.
	 * </p>
	 * <p>
	 * For more information, see the JavaDoc for DataFlavor.
	 * </p>
	 * 
	 * @return
	 */
	public DataFlavor[] getTransferDataFlavors() {

		DataFlavor[] flavors = { null };

		// log.info("Step 4 of 7: Querying for acceptable DataFlavors to determine what is available. Our example only supports our custom ImageDragAndDropPanel DataFlavor.");

		try {
			flavors[0] = ImageFilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		return flavors;
	}

	/**
	 * <p>
	 * One of three methods defined by the Transferable interface.
	 * </p>
	 * <p>
	 * Determines whether this object supports the DataFlavor. In this case,
	 * only one is supported: for this object itself.
	 * </p>
	 * 
	 * @param flavor
	 * @return True if DataFlavor is supported, otherwise false.
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {

		log.info("Step 6 of 7: Verifying that DataFlavor is supported.  Our example only supports our custom RandomDragAndDropPanel DataFlavor.");

		DataFlavor[] flavors = { null };
		try {
			flavors[0] = ImageFilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return false;
		}

        for (DataFlavor f : flavors) {
			if (f.equals(flavor)) {
				return true;
			}
		}

        return false;
	}

}
