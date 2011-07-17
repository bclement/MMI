/**
 * 
 */
package com.clementscode.mmi.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clementscode.mmi.res.CategoryItem;

/**
 * @author bclement
 * 
 */
public class SessionDataCollector {

	public enum RespType {
		INDEPENDENT, VERBAL, MODEL, NONE, NO_RESPONSE
	};

	protected static class Counts {
		EnumMap<RespType, Integer> respMap = new EnumMap<RespType, Integer>(
				RespType.class);
		int attendings = 0;
		int totalCount = 0;
		int totalErrors = 0;
	}

	public static class Response {
		CategoryItem item;
		boolean attending;
		int errors;
		RespType type;

		public Response(CategoryItem item, boolean attending, RespType type,
				int errors) {
			super();
			this.item = item;
			this.attending = attending;
			this.type = type;
			this.errors = errors;
		}

	}

	protected String configName;

	protected String sessionName;

	protected List<Response> responses = new ArrayList<Response>();

	protected Map<String, Counts> perItemMap = new HashMap<String, Counts>();

	protected Counts overallCounts = new Counts();

	/**
	 * @param configName
	 * @param sessionName
	 */
	public SessionDataCollector(String configName, String sessionName) {
		super();
		this.configName = configName;
		this.sessionName = sessionName;
	}

	public void addResponse(CategoryItem item, boolean attending,
			RespType response, int errors) {

		Counts itemCounts = perItemMap.get(item.toString());
		if (itemCounts == null) {
			itemCounts = new Counts();
			perItemMap.put(item.toString(), itemCounts);
		}
		inc(response, itemCounts.respMap);
		inc(response, overallCounts.respMap);
		if (attending) {
			++overallCounts.attendings;
			++itemCounts.attendings;
		}
		++itemCounts.totalCount;
		itemCounts.totalErrors += errors;
		overallCounts.totalErrors += errors;
		responses.add(new Response(item, attending, response, errors));
	}

	/**
	 * @param resp
	 * @return
	 */
	public SessionData getData() {
		int totalNumber = responses.size();
		if (totalNumber < 1) {
			return new SessionData(sessionName, null, null, null);
		}
		Stats overall = parseTotals(configName, totalNumber, overallCounts);
		List<Stats> perItems = new ArrayList<Stats>(perItemMap.size());
		for (String name : perItemMap.keySet()) {
			Counts counts = perItemMap.get(name);
			perItems.add(parseTotals(name, counts.totalCount, counts));
		}

		return new SessionData(sessionName, overall, perItems, responses);
	}

	protected Stats parseTotals(String name, int total, Counts counts) {
		Stats rval = new Stats();
		rval.setName(name);
		EnumMap<RespType, Integer> map = counts.respMap;
		for (RespType r : RespType.values()) {
			int i = getIntSafe(map.get(r));
			rval.addStat(r, getPercent(i, total));
		}
		rval.setPercentAttending(getPercent(counts.attendings, total));
		rval.setErrors(counts.totalErrors);
		return rval;
	}

	protected double getPercent(int i, int total) {
		return (((double) i) / total) * 100;
	}

	protected int getIntSafe(Integer i) {
		if (i == null) {
			return 0;
		}
		return i;
	}

	protected <T> void inc(T type, Map<T, Integer> map) {
		Integer i = map.get(type);
		if (i == null) {
			i = new Integer(0);
		}
		map.put(type, i + 1);
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName
	 *            the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
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
