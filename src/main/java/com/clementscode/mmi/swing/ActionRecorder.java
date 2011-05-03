package com.clementscode.mmi.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class ActionRecorder extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8924752985309017175L;
	private int pointValue;
	private String text;
	private Mediator mediator;
	
	public ActionRecorder(String text, ImageIcon icon,
                        String desc, Integer mnemonic, int numPoints,Mediator mediator) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.setPointValue(numPoints);
        this.text=text;
        setMediator(mediator);
    }
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action for ActionRecorder " + this.text + " : " + e);
        mediator.switchImage();
    }
	public void setPointValue(int pointValue) {
		this.pointValue = pointValue;
	}
	public int getPointValue() {
		return pointValue;
	}
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}
	public Mediator getMediator() {
		return mediator;
	}
}