/**
 * 
 */
package com.clementscode.mmi.data;

import java.util.EnumMap;

import com.clementscode.mmi.data.SessionDataCollector.RespType;

/**
 * @author bclement
 * 
 */
public class Stats {

	protected String name;

	protected EnumMap<RespType, Double> respMap = new EnumMap<RespType, Double>(
			RespType.class);

	protected double percentAttending;

	/**
	 * 
	 */
	public Stats() {
	}

	/**
	 * @param name
	 * @param percentAttending
	 * @param percentIndep
	 * @param percentVerbal
	 * @param percentModel
	 */
	public Stats(String name) {
		super();
		this.name = name;
	}

	public void addStat(RespType r, double percentage) {
		respMap.put(r, percentage);
	}

	public double getStat(RespType r) {
		Double rval = respMap.get(r);
		if (rval == null) {
			return 0;
		}
		return rval;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the percentAttending
	 */
	public double getPercentAttending() {
		return percentAttending;
	}

	/**
	 * @param percentAttending
	 *            the percentAttending to set
	 */
	public void setPercentAttending(double percentAttending) {
		this.percentAttending = percentAttending;
	}

}
