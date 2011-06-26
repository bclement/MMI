package com.clementscode.mmi.res;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bclement
 * 
 */
public class CategoryItem {

	protected File imgFile;

	protected BufferedImage img;

	protected File audioSD;

	protected File audioPrompt;

	protected Log log = LogFactory.getLog(this.getClass());

	private int itemNumber;

	private static int itemSerialNumber = 0;

	protected CategoryItem() {
		// for unit tests

		idObject();
	}

	/**
	 * @param typeName
	 * @param imageName
	 * @param img
	 * @param audio
	 * @throws IOException
	 */
	public CategoryItem(File imgFile, File audioSD, File audioPrompt)
			throws IOException {
		super();
		idObject();
		this.audioSD = audioSD;
		this.audioPrompt = audioPrompt;
		this.imgFile = imgFile;
		this.img = readImage(imgFile);
	}

	private void idObject() {
		itemNumber = itemSerialNumber++;
	}

	protected BufferedImage readImage(File f) throws IOException {
		System.out.println(String.format("About to read file='%s'", f
				.getCanonicalPath()));
		return ImageIO.read(f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return imgFile.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((audioPrompt == null) ? 0 : audioPrompt.hashCode());
		result = prime * result + ((audioSD == null) ? 0 : audioSD.hashCode());
		result = prime * result + ((imgFile == null) ? 0 : imgFile.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryItem other = (CategoryItem) obj;
		if (audioPrompt == null) {
			if (other.audioPrompt != null)
				return false;
		} else if (!audioPrompt.equals(other.audioPrompt))
			return false;
		if (audioSD == null) {
			if (other.audioSD != null)
				return false;
		} else if (!audioSD.equals(other.audioSD))
			return false;
		if (imgFile == null) {
			if (other.imgFile != null)
				return false;
		} else if (!imgFile.equals(other.imgFile))
			return false;
		return true;
	}

	/**
	 * @return the img
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * @param img
	 *            the img to set
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	/**
	 * @return the audioSD
	 */
	public File getAudioSD() {
		return audioSD;
	}

	/**
	 * @param audioSD
	 *            the audioSD to set
	 */
	public void setAudioSD(File audioSD) {
		this.audioSD = audioSD;
	}

	/**
	 * @return the audioPrompt
	 */
	public File getAudioPrompt() {
		return audioPrompt;
	}

	/**
	 * @param audioPrompt
	 *            the audioPrompt to set
	 */
	public void setAudioPrompt(File audioPrompt) {
		this.audioPrompt = audioPrompt;
	}

	/**
	 * @return the imgFile
	 */
	public File getImgFile() {
		return imgFile;
	}

	/**
	 * @param imgFile
	 *            the imgFile to set
	 */
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public static int getItemSerialNumber() {
		return itemSerialNumber;
	}

	public static void setItemSerialNumber(int itemSerialNumber) {
		CategoryItem.itemSerialNumber = itemSerialNumber;
	}

}
