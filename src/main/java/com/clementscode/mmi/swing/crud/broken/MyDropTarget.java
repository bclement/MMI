package com.clementscode.mmi.swing.crud.broken;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import org.apache.log4j.Logger;

public class MyDropTarget extends DropTarget {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;

	public MyDropTarget(ImageFilePanel imageFilePanel,
			ImagePanelDropTargetListener imagePanelDropTargetListener) {
		super(imageFilePanel, imagePanelDropTargetListener);
		log.info("Constructor MyDropTarget");
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		log.info("dtde=" + dtde);
		super.dragEnter(dtde);
	}

	public void dragOver(DropTargetDragEvent dtde) {
		log.info("dragOver dtde=" + dtde);
		super.dragOver(dtde);
	}

	public void dragExit(DropTargetEvent dte) {
		log.info("dragExit");
		super.dragExit(dte);
	}

	public void drop(DropTargetDropEvent dtde) {
		log.info("drop");
		super.drop(dtde);
	}

}
