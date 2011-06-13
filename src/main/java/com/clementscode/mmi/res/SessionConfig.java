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
	protected String name;

	@XmlElement
	@Deprecated
	// only here for backward compatibility in configs - not used by programs
	protected String description;

	@XmlElement
	protected int shuffleCount;

	@XmlElement
	protected String itemBase;

	@XmlElement
	protected String[] items;

	@XmlElement
	protected String prompt;

	@XmlElement
	protected int timeDelayPrompt;

	@XmlElement
	protected int timeDelayAnswer;

	@XmlElement
	protected int timeDelayBetweenItems = 3;

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
	public String[] getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(String[] items) {
		this.items = items;
	}

	/**
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * @param prompt
	 *            the prompt to set
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	 * @return the timeDelayPrompt
	 */
	public int getTimeDelayPrompt() {
		return timeDelayPrompt;
	}

	/**
	 * @param timeDelayPrompt
	 *            the timeDelayPrompt to set
	 */
	public void setTimeDelayPrompt(int timeDelayPrompt) {
		this.timeDelayPrompt = timeDelayPrompt;
	}

	/**
	 * @return the timeDelayAnswer
	 */
	public int getTimeDelayAnswer() {
		return timeDelayAnswer;
	}

	/**
	 * @param timeDelayAnswer
	 *            the timeDelayAnswer to set
	 */
	public void setTimeDelayAnswer(int timeDelayAnswer) {
		this.timeDelayAnswer = timeDelayAnswer;
	}

	/**
	 * @return the timeDelayBetweenItem
	 */
	public int getTimeDelayBetweenItems() {
		return timeDelayBetweenItems;
	}

	/**
	 * @param timeDelayBetweenItem
	 *            the timeDelayBetweenItem to set
	 */
	public void setTimeDelayBetweenItems(int timeDelayBetweenItem) {
		this.timeDelayBetweenItems = timeDelayBetweenItem;
	}

	/**
	 * @return the description
	 */
	@Deprecated
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Deprecated
	public void setDescription(String description) {
		this.description = description;
	}

}
