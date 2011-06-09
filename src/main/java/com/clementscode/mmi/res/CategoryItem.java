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

	protected File audio;

	protected Log log = LogFactory.getLog(this.getClass());

	protected CategoryItem() {
		// for unit tests
	}

	/**
	 * @param typeName
	 * @param imageName
	 * @param img
	 * @param audio
	 * @throws IOException
	 */
	public CategoryItem(File imgFile, File audio) throws IOException {
		super();
		this.audio = audio;
		this.imgFile = imgFile;
		this.img = readImage(imgFile);
	}

	public CategoryItem(File imgFile, BufferedImage img, File audio)
			throws IOException {
		// for unit tests
		super();
		this.audio = audio;
		this.imgFile = imgFile;
		this.img = img;
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
		return "CategoryItem [audio=" + audio + ", imgFile=" + imgFile + "]";
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
		result = prime * result + ((audio == null) ? 0 : audio.hashCode());
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
		if (audio == null) {
			if (other.audio != null)
				return false;
		} else if (!audio.equals(other.audio))
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
	 * @return the audio
	 */
	public File getAudio() {
		return audio;
	}

	/**
	 * @param audio
	 *            the audio to set
	 */
	public void setAudio(File audio) {
		this.audio = audio;
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

}
