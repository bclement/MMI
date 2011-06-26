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

	/**
	 * @param config
	 * @throws Exception
	 */
	public Session(SessionConfig config) throws Exception {
		super();
		this.configName = config.getName();
		this.config = config;
		this.items = parseItems(config);

	}

	/**
	 * @param config
	 * @return
	 * @throws IOException
	 */
	protected CategoryItem[] parseItems(SessionConfig config)
			throws IOException {
		if (config == null) {
			return new CategoryItem[0];
		}
		ItemConfig[] confItems = config.getItems();
		CategoryItem[] rval = new CategoryItem[confItems.length];
		String base = config.getItemBase();
		for (int i = 0; i < rval.length; ++i) {
			File imgFile = new File(base, confItems[i].visualSD);
			File audioSD = null;
			if (confItems[i].audioSD != null) {
				audioSD = new File(base, confItems[i].audioSD);
			}
			File audioPrompt = null;
			if (confItems[i].audioPrompt != null) {
				audioPrompt = new File(base, confItems[i].audioPrompt);
			}
			rval[i] = new CategoryItem(imgFile, audioSD, audioPrompt);
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

}
