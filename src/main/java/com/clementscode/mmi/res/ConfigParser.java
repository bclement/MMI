/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

/**
 * @author bclement
 * 
 */
public class ConfigParser {

	protected ObjectMapper mapper;

	protected Map<String, VersionConfigParser> versionParsers = new HashMap<String, VersionConfigParser>();

	protected VersionConfigParser legacyParser;

	public ConfigParser(VersionConfigParser legacyParser) {
		this.legacyParser = legacyParser;
		mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);
	}

	public void registerVersionParser(VersionConfigParser parser) {
		versionParsers.put(parser.getVersion(), parser);
	}

	public SessionConfig parse(File f) throws Exception {
		return parse(new FileInputStream(f));
	}

	public void Serialize(OutputStream out, SessionConfig config)
			throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, config);
	}

	public SessionConfig parse(InputStream in) throws Exception {
		Map<?, ?> value = mapper.readValue(in, Map.class);
		Object version = value.get("version");
		if (version != null && version instanceof String) {
			VersionConfigParser parser = versionParsers.get((String) version);
			return parser.parse(mapper, value);
		} else {
			return legacyParser.parse(mapper, value);
		}
	}

}
