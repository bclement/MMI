/**
 * 
 */
package com.clementscode.mmi.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.clementscode.mmi.data.SessionDataCollector.RespType;
import com.clementscode.mmi.res.CategoryItem;

/**
 * @author bclement
 * 
 */
public class SessionDataCollectorTest {

	protected static final String session = "session";

	protected static final String desc = "description";

	protected final File tmp;

	protected final File tmp1;

	protected final CategoryItem item1;

	protected CategoryItem item2;

	/**
	 * @throws IOException
	 * 
	 */
	public SessionDataCollectorTest() throws IOException {
		tmp = File.createTempFile("foo", "bar");
		tmp1 = File.createTempFile("foo", "bar");
		// item1 = new CategoryItem(tmp, null, tmp);
		// item2 = new CategoryItem(tmp1, null, tmp1);
		item1 = new CategoryItem();
		item2 = new CategoryItem();
		item1.setImgFile(tmp);
		item2.setImgFile(tmp1);
		item1.setAudioPrompt(tmp);
		item2.setAudioPrompt(tmp1);
	}

	@Test
	public void percentTest() {
		SessionDataCollector collector = new SessionDataCollector(session, desc);
		collector.addResponse(item1, true, RespType.INDEPENDENT, 0);
		collector.addResponse(item1, false, RespType.MODEL, 0);
		collector.addResponse(item2, true, RespType.VERBAL, 0);
		collector.addResponse(item2, false, RespType.NONE, 0);
		SessionData data = collector.getData();
		assertNotNull(data);
		Stats overall = data.getOverall();
		assertEquals(25, overall.getStat(RespType.INDEPENDENT), 0);
		assertEquals(25, overall.getStat(RespType.VERBAL), 0);
		assertEquals(25, overall.getStat(RespType.MODEL), 0);
		assertEquals(session, overall.getName());
	}
}
