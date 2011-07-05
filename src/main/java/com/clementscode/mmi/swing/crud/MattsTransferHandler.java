package com.clementscode.mmi.swing.crud;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MattsTransferHandler extends TransferHandler {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	private static int mthNumberSeq = 0;
	// private String overWrittenComponent;
	private static List<MattsTransferHandler> lstMth = null;
	private static int importNumber = -1;// , exportNumber = -1;

	// private DragableJLabelWithImage lblSource;
	private DragableJLabelWithImage lblDestination;



	private int row;
	private JPanel parent;
	// private int number;
	private int mthNumber;

	public MattsTransferHandler(String propertyName) {
		super(propertyName);
		mthNumber = mthNumberSeq++;
		if (null == lstMth) {
			lstMth = new ArrayList<MattsTransferHandler>();
		}
		lstMth.add(this);
		// this.edc = edc;
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
		log.info("exportDone source=" + source);
		// exportNumber = mthNumber;
		super.exportDone(source, data, action);
		// edc.exportDone(overWrittenComponent, source);
		DragableJLabelWithImage sourceLbl = (DragableJLabelWithImage) source;
		String name = sourceLbl.getName();
		String rowStr = StringUtils.split(name, ";")[0];
		rowStr = rowStr.substring("row=".length());
		row = Integer.parseInt(rowStr);
		// final JPanel triPanelDestination = (JPanel)
		// lblDestination.getParent();
		MattsTransferHandler mthImport = lstMth.get(importNumber);
		parent = mthImport.parent;
		System.out.println("parent.getName()=" + parent.getName());
		final TriJPanel triPanelDestination = (TriJPanel) parent;
		System.out.println("mthNumber=" + mthNumber);
		System.out.println("PanelRow destination="
				+ triPanelDestination.getName());

		
		// Try some invoke later stuff here...
		Runnable doWorkRunnable = new Runnable() {
			public void run() {

				triPanelDestination.setBorder(row);

				triPanelDestination.invalidate();
				triPanelDestination.revalidate();

			}
		};
		SwingUtilities.invokeLater(doWorkRunnable);

	}

	// Causes a transfer to a component from a clipboard or a DND drop
	// operation.
	public boolean importData(JComponent comp, Transferable t) {
		System.out.println(String.format("importData(comp=%s,t=%s)", comp, t));
		// edc.importData(comp);
		// JLabel label = (JLabel) comp;
		// this.overWrittenComponent = label.getText();
		System.out.println("mthNumber=" + mthNumber);
		importNumber = mthNumber;
		lblDestination = (DragableJLabelWithImage) comp;
		try {
			parent = (JPanel) lblDestination.getParent();
			System.out.println("Parent name: " + parent.getName());
		} catch (Exception bland) {
			bland.printStackTrace();
		}
		return super.importData(comp, t);
	}

	// Causes a transfer to occur from a clipboard or a drag and drop operation.
	public boolean importData(TransferHandler.TransferSupport support) {
		System.out.println("TransferHandler.TransferSupport support support="
				+ support);
		return super.importData(support);
	}

	public void setDestination(DragableJLabelWithImage lblDestination) {
		this.lblDestination = lblDestination;

	}

}
