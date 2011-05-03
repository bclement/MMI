package com.clementscode.swing;

public class Mediator {
	private Gui gui;

	public Mediator() {
	}

	public Mediator(Gui gui) {
		this.gui = gui;
	}

	public void switchImage() {
		gui.switchImage();
	}
}
