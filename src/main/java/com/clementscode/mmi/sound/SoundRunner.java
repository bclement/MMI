/**
 * 
 */
package com.clementscode.mmi.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bclement
 * 
 */
public class SoundRunner implements Runnable {

	protected File soundFile;

	protected Log log = LogFactory.getLog(this.getClass());

	protected Boolean go = true;

	/**
	 * @param soundFile
	 */
	public SoundRunner(File soundFile) {
		super();
		this.soundFile = soundFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			playSound(soundFile);
		} catch (Exception e) {
			log.error("Problem playing sound: " + soundFile, e);
		}

	}

	public void stop() {
		synchronized (go) {
			go = false;
		}
	}

	public void playSound(File audio) throws UnsupportedAudioFileException,
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
		byte[] abData = new byte[1024];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (!go) {
					break;
				}
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
