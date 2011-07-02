package com.clementscode.mmi.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActionRecorder extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8924752985309017175L;
	private String text;
	private MediatorListener mediator;
	private com.clementscode.mmi.swing.MediatorListenerCustomer.Action action;
	protected Log log = LogFactory.getLog(this.getClass());

	public ActionRecorder(String text, ImageIcon icon, String desc,
			Integer mnemonic,
			KeyStroke keyStroke,
			com.clementscode.mmi.swing.MediatorListenerCustomer.Action attending,
			MediatorListener mediator) {
		super(text, icon);
		putValue(SHORT_DESCRIPTION, desc);
		putValue(MNEMONIC_KEY, mnemonic);

		// Set an accelerator key; this value is used by menu items
		putValue(Action.ACCELERATOR_KEY, keyStroke);

		this.action = attending;
		this.text = text;
		setMediator(mediator);
	}

	public void actionPerformed(ActionEvent e) {
		System.out
				.println("Action for ActionRecorder " + this.text + " : " + e);

		mediator.execute(action);

	}

	public void setMediator(MediatorListener mediator) {
		this.mediator = mediator;
	}

	public MediatorListener getMediator() {
		return mediator;
	}
}