/**
 * 
 */
package com.clementscode.mmi.res;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import com.clementscode.mmi.TestUtils;

/**
 * @author bclement
 * 
 */
public class CategoryItemScannerTest {

	public static final String wav = "wAv";

	public static final String notAudio = "foobastic";

	public static final String[] audioExts = new String[] { "wav" };

	public static final String[] imgExts = new String[] { "jpg", "png", "gif" };

	@Test
	public void isOneOfTest() {
		assertTrue(CategoryItemScanner.isOneOf(wav, audioExts));
	}

	@Test
	public void isOneOfFalseTest() {
		assertFalse(CategoryItemScanner.isOneOf(notAudio, audioExts));
	}

	@Test
	public void getImgsTest() {
		CategoryItemScanner scanner = new CategoryItemScanner(audioExts,
				imgExts);
		File[] imgs = scanner.getImgFiles(new File(TestUtils.itempath));
		assertNotNull(imgs);
		Assert.assertArrayEquals(new String[] { TestUtils.gifName,
				TestUtils.jpgName, TestUtils.pngName }, getFileNames(imgs));
	}

	@Test
	public void getSndTest() {
		CategoryItemScanner scanner = new CategoryItemScanner(audioExts,
				imgExts);
		File[] snds = scanner.getAudioFiles(new File(TestUtils.itempath));
		assertNotNull(snds);
		assertEquals(1, snds.length);
		assertEquals(TestUtils.wavName, snds[0].getName());
	}

	public String[] getFileNames(File[] files) {
		String[] rval = new String[files.length];
		for (int i = 0; i < files.length; ++i) {
			rval[i] = files[i].getName();
		}
		Arrays.sort(rval, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return rval;
	}

	@Test
	public void readPngImgTest() throws IOException {
		assertImage(TestUtils.pngName, TestUtils.pngPath, 48, 48);
	}

	@Test
	public void readJpgImgTest() throws IOException {
		assertImage(TestUtils.jpgName, TestUtils.jpgPath, 48, 48);
	}

	@Test
	public void readGifImgTest() throws IOException {
		assertImage(TestUtils.gifName, TestUtils.gifPath, 48, 48);
	}

	@Test
	public void partialImgFailTest() throws IOException {
		CategoryItemScanner scanner = new CategoryItemScanner(audioExts,
				imgExts);
		CategoryItem[] res = scanner.processDirectory("category", new File[] {
				new File(TestUtils.jpgPath), new File("crap") }, null);
		assertNotNull(res);
		assertEquals(1, res.length);
	}

	public void assertImage(String name, String path, int h, int w)
			throws IOException {
		CategoryItemScanner scanner = new CategoryItemScanner(audioExts,
				imgExts);
		CategoryItem[] res = scanner.processDirectory("category",
				new File[] { new File(path) }, null);
		assertNotNull(res);
		assertEquals(1, res.length);
		CategoryItem item = res[0];
		BufferedImage buf = item.getImg();
		assertNotNull(buf);
		assertEquals(h, buf.getHeight());
		assertEquals(w, buf.getWidth());
	}
}
