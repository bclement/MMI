package com.clementscode.mmi.swing.crud;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * <p>
 * Listener that make source draggable.
 * </p>
 * <p>
 * Thanks, source modified from:
 * http://www.zetcode.com/tutorials/javaswingtutorial/draganddrop/
 * </p>
 */
public class DraggableMouseListener extends MouseAdapter {

    @Override()
	public void mousePressed(MouseEvent e) {
		System.out
				.println("Step 1 of 7: Mouse pressed. Going to export our ImageDragAndDropPanel so that it is draggable.");

		JComponent c = (JComponent) e.getSource();
		TransferHandler handler = c.getTransferHandler();
		handler.exportAsDrag(c, e, TransferHandler.COPY);
	}
} // DraggableMouseListener
