/**
 * 
 */
package com.clementscode.mmi.data;

/**
 * @author bclement
 * 
 */
public class Stats {

	protected String name;

	protected double percentAttending;

	protected double percentIndep;

	protected double percentVerbal;

	protected double percentModel;

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
	public Stats(String name, double percentAttending, double percentIndep,
			double percentVerbal, double percentModel) {
		super();
		this.name = name;
		this.percentAttending = percentAttending;
		this.percentIndep = percentIndep;
		this.percentVerbal = percentVerbal;
		this.percentModel = percentModel;
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

	/**
	 * @return the percentIndep
	 */
	public double getPercentIndep() {
		return percentIndep;
	}

	/**
	 * @param percentIndep
	 *            the percentIndep to set
	 */
	public void setPercentIndep(double percentIndep) {
		this.percentIndep = percentIndep;
	}

	/**
	 * @return the percentVerbal
	 */
	public double getPercentVerbal() {
		return percentVerbal;
	}

	/**
	 * @param percentVerbal
	 *            the percentVerbal to set
	 */
	public void setPercentVerbal(double percentVerbal) {
		this.percentVerbal = percentVerbal;
	}

	/**
	 * @return the percentModel
	 */
	public double getPercentModel() {
		return percentModel;
	}

	/**
	 * @param percentModel
	 *            the percentModel to set
	 */
	public void setPercentModel(double percentModel) {
		this.percentModel = percentModel;
	}

}
