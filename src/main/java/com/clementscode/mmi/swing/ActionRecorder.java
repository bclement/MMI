package com.clementscode.mmi.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.clementscode.mmi.res.CategoryItem;

public class ActionRecorder extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8924752985309017175L;
	private String text;
	private Mediator mediator;
	private int action;
	
	public ActionRecorder(String text, ImageIcon icon,
                        String desc, Integer mnemonic, int action,Mediator mediator) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
       this.action=action;
        this.text=text;
        setMediator(mediator);
    }
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action for ActionRecorder " + this.text + " : " + e);
        
        mediator.execute(action);
       
        
    }
	
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}
	public Mediator getMediator() {
		return mediator;
	}
}