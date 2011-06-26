/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author bclement
 * 
 */
public class LegacyConfigParser implements VersionConfigParser {

	protected String[] sndExtentions;

	public LegacyConfigParser(String[] sndExtentions) {
		this.sndExtentions = sndExtentions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clementscode.mmi.res.VersionConfigParser#getVersion()
	 */
	public String getVersion() {
		// there were no versions in the legacy config
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.clementscode.mmi.res.VersionConfigParser#parse(org.codehaus.jackson
	 * .map.ObjectMapper, java.util.Map)
	 */
	public SessionConfig parse(ObjectMapper mapper, Map<?, ?> config)
			throws Exception {
		LegacySessionConfig legacy = mapper.convertValue(config,
				LegacySessionConfig.class);
		SessionConfig rval = new SessionConfig();

		rval.setName(legacy.getName());
		rval.setShuffleCount(legacy.getShuffleCount());
		rval.setTimeDelayAudioSD(legacy.getTimeDelayPrompt());
		rval.setTimeDelayAudioPrompt(legacy.getTimeDelayAnswer());
		rval.setTimeDelayInterTrial(legacy.getTimeDelayBetweenItems());

		String base = legacy.getItemBase();
		String[] items = legacy.getItems();
		String prompt = legacy.getPrompt();

		getItems(rval, base, prompt, items);

		return rval;
	}

	protected void getItems(SessionConfig config, String base, String prompt,
			String[] items) {
		if (items == null) {
			config.setItems(new ItemConfig[0]);
			config.setItemBase(base);
			return;
		}
		ItemConfig[] rval = new ItemConfig[items.length];
		if (base != null && !base.isEmpty()) {
			if (prompt.startsWith(base)) {
				// prompt is in base subtree - sweet!
				prompt = prompt.substring(base.length());
			} else {
				// sorry, your prompt is in another castle
				// absolute... path... EVERYTHING!
				for (int i = 0; i < items.length; ++i) {
					items[i] = base + items[i];
				}
				base = null;
			}
		}
		for (int i = 0; i < rval.length; ++i) {
			String img = items[i];
			String answer = getAudioString(base, img);
			// don't let the naming switch trick you!
			rval[i] = new ItemConfig(img, prompt, answer);
		}
		config.setItemBase(base);
		config.setItems(rval);
	}

	protected String getAudioString(String base, String imgStr) {
		File imgFile = new File(base, imgStr);
		String rval = null;
		File audioFile = getAudio(imgFile);
		if (audioFile != null) {
			String absPath = audioFile.getAbsolutePath();
			if (base != null) {
				rval = absPath.substring(base.length());
			}
		}
		return rval;
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

}
