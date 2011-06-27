/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

/**
 * @author bclement
 * 
 */
public class ConfigParser {

	protected ObjectMapper mapper;

	protected ObjectWriter writer;

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
		writer = mapper.writer();
		writer = writer.withPrettyPrinter(new DefaultPrettyPrinter());
	}

	public void registerVersionParser(VersionConfigParser parser) {
		versionParsers.put(parser.getVersion(), parser);
	}

	public SessionConfig parse(File f) throws Exception {
		return parse(new FileInputStream(f));
	}

	public void Serialize(OutputStream out, SessionConfig config)
			throws JsonGenerationException, JsonMappingException, IOException {
		writer.writeValue(out, config);
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

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, FileNotFoundException, IOException {
		SessionConfig config = new SessionConfig();
		config.setItemBase("src/test/resources/bc");
		ItemConfig[] items = new ItemConfig[7];
		items[0] = new ItemConfig("/animals/fooduck/dog.jpeg", "prompt.wav",
				"/animals/fooduck/answer.wav");
		items[1] = new ItemConfig("/animals/fooduck/fooduck.gif", "prompt.wav",
				"/animals/fooduck/answer.wav");
		items[2] = new ItemConfig("/animals/fooduck/fooduck.jpg", "prompt.wav",
				"/animals/fooduck/answer.wav");
		items[3] = new ItemConfig("/animals/fooduck/fooduck.png", "prompt.wav",
				"/animals/fooduck/answer.wav");
		items[4] = new ItemConfig("/food/foobar/bluebar.jpg", "prompt.wav",
				"/food/foobar/answer.wav");
		items[5] = new ItemConfig("/food/foobar/greenbar.jpg", "prompt.wav",
				"/food/foobar/answer.wav");
		items[6] = new ItemConfig("/food/foobar/redbar.jpg", "prompt.wav",
				"/food/foobar/answer.wav");
		config.setItems(items);
		config.setVersion("1.0.0");
		config.setName("Test Config");
		config.setShuffleCount(1);
		config.setTimeDelayAudioPrompt(5);
		config.setTimeDelayAudioSD(5);
		config.setTimeDelayAutoAdvance(3);
		ConfigParser parser = new ConfigParser(new LegacyConfigParser(
				new String[0]));
		parser.Serialize(new FileOutputStream(new File(
				"src/test/resources/config1.0.0.txt")), config);
	}

}
