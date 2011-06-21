package com.clementscode.mmi.swing.crud;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p>
 * Listens for drops and performs the updates.
 * </p>
 * <p>
 * The real magic behind the drop!
 * </p>
 */
public class ImagePanelDropTargetListener implements DropTargetListener {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private final ImageFilePanel rootPanel;

	/**
	 * <p>
	 * Two cursors with which we are primarily interested while dragging:
	 * </p>
	 * <ul>
	 * <li>Cursor for droppable condition</li>
	 * <li>Cursor for non-droppable consition</li>
	 * </ul>
	 * <p>
	 * After drop, we manually change the cursor back to default, though does
	 * this anyhow -- just to be complete.
	 * </p>
	 */
	private static final Cursor droppableCursor = Cursor
			.getPredefinedCursor(Cursor.HAND_CURSOR),
			notDroppableCursor = Cursor
					.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

	public ImagePanelDropTargetListener(ImageFilePanel sheet) {
		this.rootPanel = sheet;
	}

    // Could easily find uses for these, like cursor changes, etc.
	public void dragEnter(DropTargetDragEvent dtde) {
		log.info("dragEnter");
	}

	public void dragOver(DropTargetDragEvent dtde) {
		if (!this.rootPanel.getCursor().equals(droppableCursor)) {
			this.rootPanel.setCursor(droppableCursor);
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		log.info("dropActionChanged");
	}

	public void dragExit(DropTargetEvent dte) {
		this.rootPanel.setCursor(notDroppableCursor);
	}

    /**
	 * <p>
	 * The user drops the item. Performs the drag and drop calculations and
	 * layout.
	 * </p>
	 * 
	 * @param dtde
	 */
	public void drop(DropTargetDropEvent dtde) {

		log.info("Step 5 of 7: The user dropped the panel. The drop(...) method will compare the drops location with other panels and reorder the panels accordingly.");

		// Done with cursors, dropping
		this.rootPanel.setCursor(Cursor.getDefaultCursor());

		// Just going to grab the expected DataFlavor to make sure
		// we know what is being dropped
		DataFlavor dragAndDropPanelFlavor = null;

		Object transferableObj = null;
		Transferable transferable = null;

		try {
			// Grab expected flavor
			dragAndDropPanelFlavor = ImageFilePanel
					.getDragAndDropPanelDataFlavor();

			transferable = dtde.getTransferable();
			DropTargetContext c = dtde.getDropTargetContext();

			// What does the Transferable support
			if (transferable.isDataFlavorSupported(dragAndDropPanelFlavor)) {
				transferableObj = dtde.getTransferable().getTransferData(
						dragAndDropPanelFlavor);
			}

		} catch (Exception ex) { /* nope, not the place */
			ex.printStackTrace();
		}

		// If didn't find an item, bail
		if (transferableObj == null) {
			return;
		}

		// Cast it to the panel. By this point, we have verified it is
		// a RandomDragAndDropPanel.
		ImageDragAndDropPanel droppedPanel = (ImageDragAndDropPanel) transferableObj;

		// Get the y offset from the top of the WorkFlowSheetPanel
		// for the drop option (the cursor on the drop)
		final int dropYLoc = dtde.getLocation().y;

        // We need to map the Y axis values of drop as well as other
		// RandomDragAndDropPanel so can sort by location.
		Map<Integer, ImageDragAndDropPanel> yLocMapForPanels = new HashMap<Integer, ImageDragAndDropPanel>();
		yLocMapForPanels.put(dropYLoc, droppedPanel);

        // Iterate through the existing demo panels. Going to find their
		// locations.
		for (ImageDragAndDropPanel nextPanel : rootPanel
				.getImageDragAndDropPanels()) {
			

            // Grab the y value
			int y = nextPanel.getY();

            // If the dropped panel, skip
			if (!nextPanel.equals(droppedPanel)) {
				yLocMapForPanels.put(y, nextPanel);
			}
		}

        // Grab the Y values and sort them
		List<Integer> sortableYValues = new ArrayList<Integer>();
		sortableYValues.addAll(yLocMapForPanels.keySet());
		Collections.sort(sortableYValues);

        // Put the panels in list in order of appearance
		List<ImageDragAndDropPanel> orderedPanels = new ArrayList<ImageDragAndDropPanel>();
		for (Integer i : sortableYValues) {
			orderedPanels.add(yLocMapForPanels.get(i));
		}

		// Grab the in-memory list and re-add panels in order.
		List<ImageDragAndDropPanel> inMemoryPanelList = this.rootPanel
				.getImageDragAndDropPanels();
		inMemoryPanelList.clear();
		inMemoryPanelList.addAll(orderedPanels);

		// Request relayout contents, or else won't update GUI following drop.
		// Will add back in the order to which we just sorted
		this.rootPanel.refreshGui();
	}
} // DemoPanelDropTargetListener

