package com.clementscode.mmi.swing.crud;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.clementscode.mmi.swing.MediatorListener;

public class CrudMediator implements MediatorListener {

	private Logger log = Logger.getLogger(this.getClass().getName());
	private TriPanelCrud triPanelCrud;

	public CrudMediator(TriPanelCrud triPanelCrud) {
		this.triPanelCrud = triPanelCrud;
	}

	public void execute(Action action) {

		switch (action) {
		case OPEN:
			break;
		case SAVE:
			try {
				triPanelCrud.collectSessionConfigData();
				triPanelCrud.writeSessionConfig();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // TODO: Fix!
			break;
		case SAVE_AS:
			break;
		case DEBUG:
			break;
		case QUIT:
			log.info("User asked to quit...");
			System.exit(0);
			break;
		}

	}

}
