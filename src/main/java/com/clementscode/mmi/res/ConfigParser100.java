/**
 * 
 */
package com.clementscode.mmi.res;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author bclement
 * 
 */
public class ConfigParser100 implements VersionConfigParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clementscode.mmi.res.VersionConfigParser#getVersion()
	 */
	public String getVersion() {
		return "1.0.0";
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
		return mapper.convertValue(config, SessionConfig.class);
	}

}
