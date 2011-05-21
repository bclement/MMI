package com.clementscode.mmi.swing;

public class MediatorForCrud implements MediatorListener {

	private CrudFrame crudFrame;

	public MediatorForCrud(CrudFrame crudFrame) {
		this.crudFrame = crudFrame;
	}

	public void execute(int action) {
		switch (action) {
		case Mediator.OPEN:
			crudFrame.openSessionFile();
			break;
		case Mediator.QUIT:
			System.out.println("Normal exit.");
			System.exit(0);
			break;
		}
	}

}
