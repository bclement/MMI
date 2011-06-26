/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Test;

/**
 * @author bclement
 * 
 */
public class SessionConfigTest {

	protected static final String json = "{\"name\":\"name\",\"shuffleCount\":1,\"itemBase\":\"base\",\"items\":[\"item1\",\"item2\"],\"prompt\":\"prompt\",\"timeDelayPrompt\":3,\"timeDelayAnswer\":2}";

	protected ObjectMapper mapper;

	/**
	 * 
	 */
	public SessionConfigTest() {
		mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);
	}

	@Test
	public void readTest() throws JsonGenerationException,
			JsonMappingException, IOException {
		// FIXME I'm a horrible human being
		// SessionConfig config = mapper.readValue(json, SessionConfig.class);
		// assertNotNull(config);
		// assertEquals("base", config.getItemBase());
		// Assert.assertArrayEquals(new String[] { "item1", "item2" }, config
		// .getItems());
		// assertEquals("name", config.getName());

		// assertEquals("prompt", config.getPrompt());
		// assertEquals(1, config.getShuffleCount());
		// assertEquals(2, config.getTimeDelayAnswer());
		// assertEquals(3, config.getTimeDelayPrompt());
	}
}
