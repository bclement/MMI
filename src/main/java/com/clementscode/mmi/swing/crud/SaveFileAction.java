package com.clementscode.mmi.swing.crud;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

/**
 * This action creates and shows a modal save-file dialog. Started with
 * http://www.exampledepot.com/egs/javax.swing.filechooser/createdlg.html
 */
public class SaveFileAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass().getName());
	JFileChooser chooser;
    JFrame frame;
	private File file;
	private SaveCallback sb;

	SaveFileAction(String text, String desc,
			KeyStroke keyStroke, JFrame frame, JFileChooser chooser,
 File theFile, SaveCallback sb) {
        super("Save As...");

		putValue(SHORT_DESCRIPTION, desc);
		// putValue(MNEMONIC_KEY, mnemonic);

		// Set an accelerator key; this value is used by menu items
		putValue(Action.ACCELERATOR_KEY, keyStroke);

        this.chooser = chooser;
        this.frame = frame;
		this.file = theFile;
		this.sb = sb;
    }

    public void actionPerformed(ActionEvent evt) {
        // Show dialog; this method does not return until dialog is closed
        chooser.showSaveDialog(frame);

        // Get the selected file
		file = chooser.getSelectedFile();
		log.info(String.format("Selected '%s' to save session setup to.", file));
		sb.save();
    }

	public File getFile() {
		return file;
	}
};
