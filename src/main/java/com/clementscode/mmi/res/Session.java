/**
 * 
 */
package com.clementscode.mmi.res;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

/**
 * @author bclement
 * 
 */
public class Session {

	protected String configName;

	protected String sessionName;

	protected File sessionDataFile;

	protected CategoryItem[] items;

	protected SessionConfig config;

	protected String[] sndExtentions;

	protected File prompt;

	protected int timeDelayPrompt;

	protected int timeDelayAnswer;

	protected int timeDelayBetweenItems;

	protected int shuffleCount;

	/**
	 * @param name
	 * @param items
	 * @throws Exception
	 */
	public Session(SessionConfig config, String[] sndExtentions)
			throws Exception {
		super();
		this.sndExtentions = sndExtentions;
		this.configName = config.getName();
		this.config = config;
		this.items = parseItems(config);
		this.prompt = parsePrompt(config);
		this.shuffleCount = config.getShuffleCount();
		this.timeDelayAnswer = config.getTimeDelayAnswer();
		this.timeDelayBetweenItems = config.getTimeDelayBetweenItems();
		timeDelayPrompt = config.getTimeDelayPrompt();

	}

	/**
	 * @param config2
	 * @return
	 * @throws Exception
	 */
	protected File parsePrompt(SessionConfig config) throws Exception {
		String str = config.getPrompt();
		if (str == null) {
			return null;
		}
		File rval = new File(str);
		if (!rval.exists()) {
			throw new Exception("Audio prompt doesn't exist: " + rval);
		}
		if (!CategoryItemScanner.isOneOf(rval.getName(), sndExtentions)) {
			throw new Exception("Audio prompt format not recognized: " + rval);
		}
		return rval;
	}

	/**
	 * @param sndExtentions
	 * @param config2
	 * @return
	 * @throws IOException
	 */
	protected CategoryItem[] parseItems(SessionConfig config)
			throws IOException {
		String base = config.getItemBase();
		String[] paths = config.getItems();
		if (paths == null || paths.length < 1) {
			return new CategoryItem[0];
		}
		CategoryItem[] rval = new CategoryItem[paths.length];
		for (int i = 0; i < paths.length; ++i) {
			File img = new File(base, paths[i]);
			rval[i] = new CategoryItem(img, getAudio(img));
		}
		return rval;
	}

	public Dimension getMaxDimensions() {
		Dimension dim = null;
		int mh = 0, mw = 0;
		for (CategoryItem item : items) {
			int h = item.getImg().getHeight();
			int w = item.getImg().getWidth();
			mh = mh < h ? h : mh;
			mw = mw < w ? w : mw;
		}
		int tp = (int) (mh * 0.10);
		mh += tp;
		tp = (int) (mw * 0.10);
		mw += tp;
		dim = new Dimension(mw, mh);
		return dim;
	}

	protected File getAudio(File img) {
		File parent = img.getParentFile();
		if (parent != null) {
			File[] snds = CategoryItemScanner
					.getFileType(parent, sndExtentions);
			if (snds != null && snds.length > 0) {
				return snds[0];
			}
		}
		return null;
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName
	 *            the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * @return the items
	 */
	public CategoryItem[] getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(CategoryItem[] items) {
		this.items = items;
	}

	/**
	 * @return the config
	 */
	public SessionConfig getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(SessionConfig config) {
		this.config = config;
	}

	/**
	 * @return the sndExtentions
	 */
	public String[] getSndExtentions() {
		return sndExtentions;
	}

	/**
	 * @param sndExtentions
	 *            the sndExtentions to set
	 */
	public void setSndExtentions(String[] sndExtentions) {
		this.sndExtentions = sndExtentions;
	}

	/**
	 * @return the prompt
	 */
	public File getPrompt() {
		return prompt;
	}

	/**
	 * @param prompt
	 *            the prompt to set
	 */
	public void setPrompt(File prompt) {
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

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public File getSessionDataFile() {
		return sessionDataFile;
	}

	public void setSessionDataFile(File sessionDataFile) {
		this.sessionDataFile = sessionDataFile;
	}

	/**
	 * @return the timeDelayBetweenItems
	 */
	public int getTimeDelayBetweenItems() {
		return timeDelayBetweenItems;
	}

	/**
	 * @param timeDelayBetweenItems
	 *            the timeDelayBetweenItems to set
	 */
	public void setTimeDelayBetweenItems(int timeDelayBetweenItems) {
		this.timeDelayBetweenItems = timeDelayBetweenItems;
	}

}
