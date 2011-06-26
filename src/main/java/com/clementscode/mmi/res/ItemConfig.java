/**
 * 
 */
package com.clementscode.mmi.res;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author bclement
 * 
 */
public class ItemConfig {

	@XmlElement
	/** path to image file **/
	protected String visualSD;

	@XmlElement
	/** path to "what is it" file **/
	protected String audioSD;

	@XmlElement
	/** path to "it's a _____" file **/
	protected String audioPrompt;

	/**
	 * 
	 */
	public ItemConfig() {

	}

	/**
	 * @param visualSD
	 * @param audioSD
	 * @param audioPrompt
	 */
	public ItemConfig(String visualSD, String audioSD, String audioPrompt) {
		super();
		this.visualSD = visualSD;
		this.audioSD = audioSD;
		this.audioPrompt = audioPrompt;
	}

	/**
	 * @return the visualSD
	 */
	public String getVisualSD() {
		return visualSD;
	}

	/**
	 * @param visualSD
	 *            the visualSD to set
	 */
	public void setVisualSD(String visualSD) {
		this.visualSD = visualSD;
	}

	/**
	 * @return the audioSD
	 */
	public String getAudioSD() {
		return audioSD;
	}

	/**
	 * @param audioSD
	 *            the audioSD to set
	 */
	public void setAudioSD(String audioSD) {
		this.audioSD = audioSD;
	}

	/**
	 * @return the audioPrompt
	 */
	public String getAudioPrompt() {
		return audioPrompt;
	}

	/**
	 * @param audioPrompt
	 *            the audioPrompt to set
	 */
	public void setAudioPrompt(String audioPrompt) {
		this.audioPrompt = audioPrompt;
	}

}
