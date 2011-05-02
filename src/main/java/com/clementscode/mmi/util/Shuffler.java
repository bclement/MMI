/**
 * 
 */
package com.clementscode.mmi.util;

import java.util.Random;

/**
 * @author bclement
 * 
 */
public class Shuffler {

	protected static Random r = new Random(System.currentTimeMillis());

	public static <T> void shuffle(T[] array) {
		// simple Knuth shuffle
		T tmp;
		for (int i = array.length - 1; i > 0; --i) {
			int j = r.nextInt(i + 1);
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}
}
