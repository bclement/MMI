package com.clementscode.mmi.swing.crud.broken;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;


/**
 * <p>
 * Used by both the draggable class and the target for negotiating data.
 * </p>
 * <p>
 * Note that this should be set for both the draggable object and the drop
 * target.
 * </p>
 * 
 * @author besmit
 */
public class DragAndDropTransferHandler extends TransferHandler implements
		DragSourceMotionListener {
	private Logger log = Logger.getLogger(this.getClass().getName());
	public DragAndDropTransferHandler() {
		super();
	}

	/**
	 * <p>
	 * This creates the Transferable object. In our case, RandomDragAndDropPanel
	 * implements Transferable, so this requires only a type cast.
	 * </p>
	 * 
	 * @param c
	 * @return
	 */
	@Override()
	public Transferable createTransferable(JComponent c) {

		log.info("Step 3 of 7: Casting the ImageDragAndDropPanel as Transferable. The Transferable ImageDragAndDropPanel will be queried for acceptable DataFlavors as it enters drop targets, as well as eventually present the target with the Object it transfers.");

		// TaskInstancePanel implements Transferable
		if (c instanceof ImageDragAndDropPanel) {
			Transferable tip = (ImageDragAndDropPanel) c;
			return tip;
		}

		// Not found
		return null;
	}

	public void dragMouseMoved(DragSourceDragEvent dsde) {
	}

	/**
	 * <p>
	 * This is queried to see whether the component can be copied, moved, both
	 * or neither. We are only concerned with copying.
	 * </p>
	 * 
	 * @param c
	 * @return
	 */
	@Override()
	public int getSourceActions(JComponent c) {

		log.info("Step 2 of 7: Returning the acceptable TransferHandler action. Our RandomDragAndDropPanel accepts Copy only.");

		if (c instanceof ImageDragAndDropPanel) {
			return TransferHandler.COPY;
		}

		return TransferHandler.NONE;
	}
} // DragAndDropTransferHandler
