/**
 * 
 */
package com.clementscode.mmi.res;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author bclement
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SessionConfig {

	@XmlElement
	protected String version;

	@XmlElement
	protected String name;

	@XmlElement
	protected int shuffleCount;

	@XmlElement
	protected String itemBase;

	@XmlElement
	protected ItemConfig[] items;

	@XmlElement
	/** time between start and "what is it" **/
	protected int timeDelayAudioSD;

	@XmlElement
	/** time between "what is it" and "it's a _____" **/
	protected int timeDelayAudioPrompt;

	@XmlElement
	/** time between "it's a _____" and the InterTrial interval. -1 means never auto advance**/
	protected int timeDelayAutoAdvance = -1;

	@XmlElement
	/** time between end of previous item and start of the next **/
	protected int timeDelayInterTrial = 3;

	/**
	 * 
	 */
	public SessionConfig() {
	}

	/**
	 * @param name
	 * @param shuffleCount
	 * @param itemBase
	 * @param items
	 * @param timeDelayAudioSD
	 * @param timeDelayAudioPrompt
	 * @param timeDelayAutoAdvance
	 * @param timeDelayInterTrial
	 */
	public SessionConfig(String name, int shuffleCount, String itemBase,
			ItemConfig[] items, int timeDelayAudioSD, int timeDelayAudioPrompt,
			int timeDelayAutoAdvance, int timeDelayInterTrial) {
		super();
		this.name = name;
		this.shuffleCount = shuffleCount;
		this.itemBase = itemBase;
		this.items = items;
		this.timeDelayAudioSD = timeDelayAudioSD;
		this.timeDelayAudioPrompt = timeDelayAudioPrompt;
		this.timeDelayAutoAdvance = timeDelayAutoAdvance;
		this.timeDelayInterTrial = timeDelayInterTrial;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shuffleCount
	 */
	public int getShuffleCount() {
		return shuffleCount;
	}

	/**
	 * @param shuffleCount
	 *            the shuffleCount to set
	 */
	public void setShuffleCount(int shuffleCount) {
		this.shuffleCount = shuffleCount;
	}

	/**
	 * @return the itemBase
	 */
	public String getItemBase() {
		return itemBase;
	}

	/**
	 * @param itemBase
	 *            the itemBase to set
	 */
	public void setItemBase(String itemBase) {
		this.itemBase = itemBase;
	}

	/**
	 * @return the items
	 */
	public ItemConfig[] getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(ItemConfig[] items) {
		this.items = items;
	}

	/**
	 * @return the timeDelayAudioSD
	 */
	public int getTimeDelayAudioSD() {
		return timeDelayAudioSD;
	}

	/**
	 * @param timeDelayAudioSD
	 *            the timeDelayAudioSD to set
	 */
	public void setTimeDelayAudioSD(int timeDelayAudioSD) {
		this.timeDelayAudioSD = timeDelayAudioSD;
	}

	/**
	 * @return the timeDelayAudioPrompt
	 */
	public int getTimeDelayAudioPrompt() {
		return timeDelayAudioPrompt;
	}

	/**
	 * @param timeDelayAudioPrompt
	 *            the timeDelayAudioPrompt to set
	 */
	public void setTimeDelayAudioPrompt(int timeDelayAudioPrompt) {
		this.timeDelayAudioPrompt = timeDelayAudioPrompt;
	}

	/**
	 * @return the timeDelayAutoAdvance
	 */
	public int getTimeDelayAutoAdvance() {
		return timeDelayAutoAdvance;
	}

	/**
	 * @param timeDelayAutoAdvance
	 *            the timeDelayAutoAdvance to set
	 */
	public void setTimeDelayAutoAdvance(int timeDelayAutoAdvance) {
		this.timeDelayAutoAdvance = timeDelayAutoAdvance;
	}

	/**
	 * @return the timeDelayInterTrial
	 */
	public int getTimeDelayInterTrial() {
		return timeDelayInterTrial;
	}

	/**
	 * @param timeDelayInterTrial
	 *            the timeDelayInterTrial to set
	 */
	public void setTimeDelayInterTrial(int timeDelayInterTrial) {
		this.timeDelayInterTrial = timeDelayInterTrial;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
