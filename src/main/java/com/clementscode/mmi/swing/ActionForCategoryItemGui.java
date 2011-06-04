package com.clementscode.mmi.swing;

import java.awt.Component;
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
		JPanel diyTable = crudFrame.getDiyTable();
		if (CrudFrame.ADD_CATEGORY_ITEM.equals(cmd)) {

			GuiForCategoryItem g4ci = new GuiForCategoryItem(crudFrame);
			crudFrame.getLstGuiForCategoryItems().add(g4ci);
			int oneBellow = 0;
			int numComponents = diyTable.getComponentCount();
			for (int n = 0; n < numComponents; ++n) {
				Component comp = diyTable.getComponent(n);
				if (comp.equals(guiForCategoryItem)) {
					oneBellow = n + 1;
					break;
				}
			}
			crudFrame.getDiyTable().add(g4ci, oneBellow);
		} else if (CrudFrame.DELETE_CATEGORY_ITEM.equals(cmd)) {

			diyTable.remove(guiForCategoryItem);
		}
		crudFrame.refreshGui();

	}

}
