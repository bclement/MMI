package com.clementscode.mmi.swing.crud;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class DragableJLabelWithImage extends JLabel {
	private static final long serialVersionUID = 1L;
	private MattsTransferHandler mattsTransferHandler;
	private DragableJLabelWithImage lblSource;
	private DragableJLabelWithImage lblDestination;
	private JPanel parentPanel;

	public DragableJLabelWithImage() {
		super();
		// Specify that the label's text property be transferable; the value
		// of
		// this property will be used in any drag-and-drop involving this
		// label
		final String propertyName = "icon";
		mattsTransferHandler = new MattsTransferHandler(propertyName);
		setTransferHandler(mattsTransferHandler);

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

	public void setSource(DragableJLabelWithImage lblSource) {
		this.lblSource = lblSource;

		mattsTransferHandler.setSource(lblSource);

	}

	public void setDestination(DragableJLabelWithImage lblDestination) {
		this.lblDestination = lblDestination;
		mattsTransferHandler.setDestination(lblDestination);
	}

	public void setParentPanel(JPanel triPanel) {
		parentPanel = triPanel;

	}

	public JPanel getParentPanel() {
		return parentPanel;
	}
}
