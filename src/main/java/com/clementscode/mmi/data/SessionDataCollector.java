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
		INDEPENDANT, VERBAL, MODEL, NONE
	};

	protected static class Counts {
		EnumMap<RespType, Integer> respMap = new EnumMap<RespType, Integer>(
				RespType.class);
		int attendings = 0;
		int totalCount = 0;
	}

	public static class Response {
		CategoryItem item;
		boolean attending;
		RespType type;

		public Response(CategoryItem item, boolean attending, RespType type) {
			super();
			this.item = item;
			this.attending = attending;
			this.type = type;
		}

	}

	protected String session;

	protected String description;

	protected List<Response> responses = new ArrayList<Response>();

	protected Map<String, Counts> perItemMap = new HashMap<String, Counts>();

	protected Counts overallCounts = new Counts();

	public SessionDataCollector(String session, String description) {
		this.session = session;
		this.description = description;
	}

	public void addResponse(CategoryItem item, boolean attending,
			RespType response) {

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
		responses.add(new Response(item, attending, response));
	}

	/**
	 * @param resp
	 * @return
	 */
	public SessionData getData() {
		int totalNumber = responses.size();
		if (totalNumber < 1) {
			return new SessionData(null, null, null);
		}
		Stats overall = parseTotals(session, totalNumber, overallCounts);
		List<Stats> perItems = new ArrayList<Stats>(perItemMap.size());
		for (String name : perItemMap.keySet()) {
			Counts counts = perItemMap.get(name);
			perItems.add(parseTotals(name, counts.totalCount, counts));
		}

		return new SessionData(overall, perItems, responses);
	}

	protected Stats parseTotals(String name, int total, Counts counts) {
		Stats rval = new Stats();
		rval.setName(name);
		EnumMap<RespType, Integer> map = counts.respMap;
		int i = getIntSafe(map.get(RespType.INDEPENDANT));
		rval.setPercentIndep(getPercent(i, total));
		i = getIntSafe(map.get(RespType.VERBAL));
		rval.setPercentVerbal(getPercent(i, total));
		i = getIntSafe(map.get(RespType.MODEL));
		rval.setPercentModel(getPercent(i, total));
		rval.setPercentAttending(getPercent(counts.attendings, total));
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
}
