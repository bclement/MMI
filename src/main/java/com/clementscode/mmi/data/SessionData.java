/**
 * 
 */
package com.clementscode.mmi.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.clementscode.mmi.data.SessionDataCollector.Response;

/**
 * @author bclement
 * 
 */
public class SessionData {

	protected SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	protected Stats overall;

	protected String sessionName;

	protected List<Stats> perItem;

	protected List<Response> respones;

	/**
	 * @param overall
	 * @param perItem
	 */
	public SessionData(String sessionName, Stats overall, List<Stats> perItem,
			List<Response> responses) {
		super();
		this.sessionName = sessionName;
		this.overall = overall;
		this.perItem = perItem;
		this.respones = responses;
	}

	public void writeSummaryHeader(CSVWriter writer) {
		String[] header = { "Date", "Session", "Therapist", "Condition",
				"targets", "% independent", "% verbal", "% model", "% attend" };
		writer.writeNext(header);
	}

	public void writeSummary(CSVWriter writer) {
		if (overall == null) {
			return;
		}
		String date = format.format(new Date());
		String session = overall.getName() + " " + sessionName;
		String therapist = "";
		String condition = "";
		String targets = getUniqueTargets(perItem);
		String ind = Double.toString(overall.getPercentIndep());
		String verb = Double.toString(overall.percentVerbal);
		String model = Double.toString(overall.percentModel);
		String attend = Double.toString(overall.percentAttending);
		String[] rval = { date, session, therapist, condition, targets, ind,
				verb, model, attend };
		writer.writeNext(rval);
	}

	public void writeSessionFile(CSVWriter writer) {
		String[] date = { "date", format.format(new Date()) };
		String[] config = { "config", overall.getName() };
		String[] session = { "session", sessionName };
		writer.writeNext(date);
		writer.writeNext(config);
		writer.writeNext(session);
		writer.writeNext(new String[] {});
		writer.writeNext(new String[] { "Session responses" });
		for (Response r : respones) {
			writeResponse(r, writer);
		}
		writer.writeNext(new String[] {});
		writer.writeNext(new String[] { "Item stats" });
		for (Stats s : perItem) {
			writeItemStats(s, writer);
		}
	}

	/**
	 * @param s
	 * @param writer
	 */
	private void writeItemStats(Stats s, CSVWriter writer) {
		String[] name = { "name", s.getName() };
		String[] ind = { "% independant", Double.toString(s.getPercentIndep()) };
		String[] verb = { "% verbal", Double.toString(s.getPercentVerbal()) };
		String[] model = { "% model", Double.toString(s.getPercentModel()) };
		String[] attend = { "% attending",
				Double.toString(s.getPercentAttending()) };
		writer.writeNext(name);
		writer.writeNext(ind);
		writer.writeNext(verb);
		writer.writeNext(model);
		writer.writeNext(attend);
	}

	/**
	 * @param r
	 * @param writer
	 */
	private void writeResponse(Response r, CSVWriter writer) {
		String[] name = { "name", r.item.getImgFile().getAbsolutePath() };
		String[] attend = { "attend", Boolean.toString(r.attending) };
		String[] response = { "response", r.type.toString() };
		writer.writeNext(name);
		writer.writeNext(attend);
		writer.writeNext(response);
	}

	private String getUniqueTargets(List<Stats> stats) {
		if (stats == null || stats.isEmpty()) {
			return "";
		}
		Iterator<Stats> i = stats.iterator();
		StringBuilder sb = new StringBuilder(i.next().getName());
		while (i.hasNext()) {
			sb.append(", ").append(i.next().getName());
		}
		return sb.toString();
	}

	/**
	 * @return the overall
	 */
	public Stats getOverall() {
		return overall;
	}

	/**
	 * @param overall
	 *            the overall to set
	 */
	public void setOverall(Stats overall) {
		this.overall = overall;
	}

	/**
	 * @return the perItem
	 */
	public List<Stats> getPerItem() {
		return perItem;
	}

	/**
	 * @param perItem
	 *            the perItem to set
	 */
	public void setPerItem(List<Stats> perItem) {
		this.perItem = perItem;
	}

	/**
	 * @return the respones
	 */
	public List<Response> getRespones() {
		return respones;
	}

	/**
	 * @param respones
	 *            the respones to set
	 */
	public void setRespones(List<Response> respones) {
		this.respones = respones;
	}

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

	/**
	 * @return the sessionName
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * @param sessionName
	 *            the sessionName to set
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

}
