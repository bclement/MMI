/**
 * 
 */
package com.clementscode.mmi.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.clementscode.mmi.res.CategoryItem;

/**
 * @author bclement
 * 
 */
public class SessionDataCollector {

	public enum RespType {
		INDEPENDANT, VERBAL, MODEL, NONE
	};

	protected static class Response {
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

	public SessionDataCollector(String session, String description) {
		this.session = session;
		this.description = description;
	}

	public void addResponse(CategoryItem item, boolean attending,
			RespType response) {
		responses.add(new Response(item, attending, response));
	}

	/**
	 * @param resp
	 * @return
	 */
	public SessionData getData() {
		int count = responses.size();
		if (count < 1) {
			return new SessionData(session, description, false, 0d, 0d, 0d,
					null);
		}
		EnumMap<RespType, Integer> totalMap = new EnumMap<RespType, Integer>(
				RespType.class);
		// Map<CategoryItem, EnumMap<RespType, Integer>> itemMap = new
		// HashMap<CategoryItem, EnumMap<RespType, Integer>>();
		for (Response r : responses) {
			inc(r.type, totalMap);
			// TODO stats by item
			// EnumMap<RespType, Integer> m = itemMap.get(r.item);
			// if (m == null) {
			// m = new EnumMap<RespType, Integer>(RespType.class);
			// }
			// inc(r.type, m);
		}
		SessionData rval = parseTotals(count, totalMap);
		rval.setName(session);
		rval.setDescription(description);
		return rval;
	}

	protected SessionData parseTotals(int count, EnumMap<RespType, Integer> map) {
		SessionData rval = new SessionData();
		int i = getIntSafe(map.get(RespType.INDEPENDANT));
		rval.setPercentIndep(getPercent(i, count));
		i = getIntSafe(map.get(RespType.VERBAL));
		rval.setPercentVerbal(getPercent(i, count));
		i = getIntSafe(map.get(RespType.MODEL));
		rval.setPercentModel(getPercent(i, count));
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

	protected void inc(RespType type, EnumMap<RespType, Integer> map) {
		Integer i = map.get(type);
		if (i == null) {
			i = new Integer(0);
		}
		map.put(type, i + 1);
	}
}
