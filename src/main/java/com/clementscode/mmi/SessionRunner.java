/**
 * 
 */
package com.clementscode.mmi;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.util.Shuffler;

/**
 * @author bclement
 * 
 */
public class SessionRunner extends Component {

	private static final long serialVersionUID = 630033037171533048L;

	protected Log log = LogFactory.getLog(this.getClass());

	protected BufferedImage img;

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			return new Dimension(img.getWidth(null), img.getHeight(null));
		}
	}

	protected SessionRunner() {
		// unit tests
	}

	public void run(Session session) {
		CategoryItem[] copy = Arrays.copyOf(session.getItems(), session
				.getItems().length);
		for (int i = 0; i < session.getShuffleCount(); ++i) {
			Shuffler.shuffle(copy);
		}
		for (CategoryItem item : copy) {
			runItem(item, session.getTimeDelayPrompt(), session
					.getTimeDelayAnswer(), session.getPrompt());
		}
	}

	/**
	 * @param item
	 */
	protected void runItem(CategoryItem item, int time1, int time2, File prompt) {
		log.info("Running Item: " + item);
		JFrame f = new JFrame(item.toString());
		f.add(this);
		this.img = item.getImg();
		f.pack();
		f.setVisible(true);
		try {
			Thread.sleep(time1 * 1000);
			playSound(prompt);
			Thread.sleep(time2 * 1000);
			playSound(item.getAudio());
			// TODO this last sleep time is not spec. It needs to keep the image
			// up until the user presses one of the scoring buttons
			Thread.sleep(time2 * 1000);
		} catch (Exception e) {
			log.error(e);
		}
		f.setVisible(false);
	}

	protected void playSound(File audio) throws UnsupportedAudioFileException,
			IOException {
		// this probably isn't the best way to play sound in java, but it seems
		// to work
		// TODO use whatever media framework the GUI person wants to use
		if (audio == null) {
			return; // there are cases when this is not an error
		}
		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(audio);
		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[524288];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}

}
