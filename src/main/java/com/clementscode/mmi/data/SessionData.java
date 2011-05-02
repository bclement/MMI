/**
 * 
 */
package com.clementscode.mmi.data;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author bclement
 * 
 */
public class SessionData {

	protected String name;

	protected String description;

	protected boolean attending;

	protected double percentIndep;

	protected double percentVerbal;

	protected double percentModel;

	protected String[][] itemStats;

	/**
	 * 
	 */
	public SessionData() {
	}

	/**
	 * @param name
	 * @param description
	 * @param attending
	 * @param percentIndep
	 * @param percentVerbal
	 * @param percentModel
	 * @param itemStats
	 */
	public SessionData(String name, String description, boolean attending,
			double percentIndep, double percentVerbal, double percentModel,
			String[][] itemStats) {
		super();
		this.name = name;
		this.description = description;
		this.attending = attending;
		this.percentIndep = percentIndep;
		this.percentVerbal = percentVerbal;
		this.percentModel = percentModel;
		this.itemStats = itemStats;
	}

	public void write(CSVWriter writer) {
		String[] rval = { name, description, String.valueOf(attending),
				Double.toString(percentIndep), Double.toString(percentVerbal),
				Double.toString(percentModel) };
		writer.writeNext(rval);
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the attending
	 */
	public boolean isAttending() {
		return attending;
	}

	/**
	 * @param attending
	 *            the attending to set
	 */
	public void setAttending(boolean attending) {
		this.attending = attending;
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

	/**
	 * @return the itemStats
	 */
	public String[][] getItemStats() {
		return itemStats;
	}

	/**
	 * @param itemStats
	 *            the itemStats to set
	 */
	public void setItemStats(String[][] itemStats) {
		this.itemStats = itemStats;
	}

}
