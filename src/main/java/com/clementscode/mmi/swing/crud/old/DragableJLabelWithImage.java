package com.clementscode.mmi.swing.crud.old;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

public class DragableJLabelWithImage extends JLabel {
	private static final long serialVersionUID = 1L;

	public DragableJLabelWithImage() {
		super();
		// Specify that the label's text property be transferable; the value
		// of
		// this property will be used in any drag-and-drop involving this
		// label
		final String propertyName = "icon";
		setTransferHandler(new MattsTransferHandler(propertyName));

		// Listen for mouse clicks
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JComponent comp = (JComponent) evt.getSource();
				TransferHandler th = comp.getTransferHandler();

				// Start the drag operation
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});

	}
}
