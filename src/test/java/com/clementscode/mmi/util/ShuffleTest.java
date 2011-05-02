/**
 * 
 */
package com.clementscode.mmi.util;

import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

import com.clementscode.mmi.util.Shuffler;

/**
 * @author bclement
 * 
 */
public class ShuffleTest {

	protected boolean print = false;

	@Test
	public void shuffleTest() {
		final Integer[] start = { 0, 1, 2, 3, 4, 5 };
		Integer[] tmp;
		for (int i = 0; i < 5; ++i) {
			tmp = Arrays.copyOf(start, start.length);
			if (print) {
				print(tmp);
			}
			Shuffler.shuffle(start);
			assertFalse(same(start, tmp));
		}
	}

	protected <T> boolean same(T[] a, T[] b) {
		boolean rval = true;
		for (int i = 0; i < a.length; ++i) {
			if (!a[i].equals(b[i])) {
				rval = false;
				break;
			}
		}
		return rval;
	}

	protected <T> void print(T[] a) {
		for (T t : a) {
			System.out.print(t + " ");
		}
		System.out.println();
	}
}
