package com.clementscode.mmi.swing.crud;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.clementscode.mmi.swing.LabelAndField;

public class TriJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private DragableJLabelWithImage lblDestination;
	private JTextField tfPrompt;
	private JTextField tfAnswer;


	private int row;

	public void setImage(DragableJLabelWithImage lblDestination) {
		this.lblDestination = lblDestination;
		this.add(lblDestination);

	}

	public void createPromptAndAnswer() {
		tfPrompt = new JTextField(20);
		tfAnswer = new JTextField(20);
		this.add(new LabelAndField("Prompt: ", tfPrompt));
		this.add(new LabelAndField("Answer: ", tfAnswer));
	}



	public String getPrompt() {
		return tfPrompt.getText();
	}

	public String getAnswer() {
		return tfAnswer.getText();
	}

	public String getPictureFileName() {
		String pictureFileName = null;

		pictureFileName = lblDestination.getText();
		pictureFileName += row;
		return pictureFileName;
	}

	public void setBorder(int row) {
		this.row = row;
		String strBorder = "row=" + row;
		TitledBorder border = BorderFactory.createTitledBorder(strBorder);
		this.setBorder(border);
	}

}
