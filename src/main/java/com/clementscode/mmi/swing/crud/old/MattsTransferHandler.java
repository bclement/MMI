package com.clementscode.mmi.swing.crud.old;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class MattsTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;

	public MattsTransferHandler(String propertyName) {
		super(propertyName);
	}

	// This output is exciting:
	//
	// TransferHandler.TransferSupport support
	// importData(comp=com.clementscode.mmi.swing.crud.DragableJLabelWithImage[i=1,128,0,128x128,alignmentX=0.0,alignmentY=0.0,border=,flags=8388608,maximumSize=,minimumSize=,preferredSize=,defaultIcon=/Users/mgpayne/resources/animals/ape/Ape2.jpg,disabledIcon=,horizontalAlignment=LEADING,horizontalTextPosition=TRAILING,iconTextGap=4,labelFor=,text=,verticalAlignment=CENTER,verticalTextPosition=CENTER],t=java.awt.dnd.DropTargetContext$TransferableProxy@42aa0877)
	// exportDone
	// source=com.clementscode.mmi.swing.crud.DragableJLabelWithImage[i=0,0,0,128x128,alignmentX=0.0,alignmentY=0.0,border=,flags=41943040,maximumSize=,minimumSize=,preferredSize=,defaultIcon=/Users/mgpayne/resources/animals/ape/Ape1.jpg,disabledIcon=,horizontalAlignment=LEADING,horizontalTextPosition=TRAILING,iconTextGap=4,labelFor=,text=,verticalAlignment=CENTER,verticalTextPosition=CENTER]
	//
	// So, importData shows the destination before it's over written
	// exportDone shows the source data

	// Invoked after data has been exported.
	protected void exportDone(JComponent source, Transferable data, int action) {
		System.out.println("exportDone source=" + source);
		super.exportDone(source, data, action);
	}

	// Causes a transfer to a component from a clipboard or a DND drop
	// operation.
	public boolean importData(JComponent comp, Transferable t) {
		System.out.println(String.format("importData(comp=%s,t=%s)", comp, t));
		return super.importData(comp, t);
	}

	// Causes a transfer to occur from a clipboard or a drag and drop operation.
	public boolean importData(TransferHandler.TransferSupport support) {
		System.out.println("TransferHandler.TransferSupport support");
		return super.importData(support);
	}

}
