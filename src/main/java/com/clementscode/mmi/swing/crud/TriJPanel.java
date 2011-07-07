package com.clementscode.mmi.swing.crud;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.clementscode.mmi.res.ItemConfig;
import com.clementscode.mmi.swing.LabelAndField;

public class TriJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass().getName());
	private DragableJLabelWithImage lblDestination;
	private JTextField tfPrompt;
	private JTextField tfAnswer;
	private int row = -1;
	private TriPanelCrud triPanelCrud;

	public TriJPanel(TriPanelCrud triPanelCrud) {
		this.triPanelCrud = triPanelCrud;
	}

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



	public void setBorder(int row) {
		this.row = row;
		String strBorder = "row=" + row;
		TitledBorder border = BorderFactory.createTitledBorder(strBorder);
		this.setBorder(border);
		if (TriPanelCrud.bAutoMode) {
			String whatFn = TriPanelCrud.findSoundFile("What is");
			tfPrompt.setText(whatFn);
			log.info("Just set tfPrompt: " + whatFn);

			String pictureFileName = TriPanelCrud.mapPictureNumberToPictureFileName
					.get(row);
			File pFile = new File(pictureFileName);
			String n = pFile.getName();
			int endIndex = n.length() - "X.jpg".length();
			n = n.substring(0, endIndex);
			whatFn = TriPanelCrud.findSoundFile(n);
			tfAnswer.setText(whatFn);

			log.info("Just set tfAnswer: " + whatFn);
		}
	}

	public int getPictureNumber() {

		return row;
	}

	public void setItemConfig(ItemConfig itemConfig) {
		int rn = triPanelCrud.findRowForPicture(itemConfig.getVisualSD());
		setBorder(rn);
		lblDestination.setIcon(triPanelCrud.getIconNumber(rn));
		tfPrompt.setText(itemConfig.getAudioSD());
		tfAnswer.setText(itemConfig.getAudioPrompt());
	}

}
