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
public interface VersionConfigParser {

	public String getVersion();

	public SessionConfig parse(ObjectMapper mapper, Map<?, ?> config)
			throws Exception;
}
