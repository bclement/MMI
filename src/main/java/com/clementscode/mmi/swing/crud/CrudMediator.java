package com.clementscode.mmi.swing.crud;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.MediatorListener;

public class CrudMediator implements MediatorListener, SaveCallback {

	private Logger log = Logger.getLogger(this.getClass().getName());
	private TriPanelCrud triPanelCrud;

	public CrudMediator(TriPanelCrud triPanelCrud) {
		this.triPanelCrud = triPanelCrud;
	}

	public void execute(Action action) {

		switch (action) {
		case OPEN:
			triPanelCrud.openSessionConfig();
			break;
		case SAVE:
			save();
			break;
		case DEBUG:
			triPanelCrud.showDebugFrame();
			break;
		case QUIT:
			log.info("User asked to quit...");
			System.exit(0);
			break;
		}

	}

	public void save() {
		try {
			triPanelCrud.collectSessionConfigData();
			triPanelCrud.writeSessionConfig();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
