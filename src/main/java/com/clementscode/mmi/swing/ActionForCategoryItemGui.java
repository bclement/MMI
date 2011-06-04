package com.clementscode.mmi.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

public class ActionForCategoryItemGui extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private String cmd;
	private GuiForCategoryItem guiForCategoryItem;

	public ActionForCategoryItemGui(String cmd,
			GuiForCategoryItem guiForCategoryItem, String text) {
		super(text);
		this.cmd = cmd;
		this.guiForCategoryItem = guiForCategoryItem;

	}

	public void actionPerformed(ActionEvent e) {
		CrudFrame crudFrame = guiForCategoryItem.getCrudFrame();

		if (CrudFrame.ADD_CATEGORY_ITEM.equals(cmd)) {

			GuiForCategoryItem g4ci = new GuiForCategoryItem(crudFrame);
			crudFrame.getLstGuiForCategoryItems().add(g4ci);
			crudFrame.getDiyTable().add(g4ci);
		} else if (CrudFrame.DELETE_CATEGORY_ITEM.equals(cmd)) {
			JPanel diyTable = crudFrame.getDiyTable();
			diyTable.remove(guiForCategoryItem);
		}
		crudFrame.refreshGui();

	}

}
