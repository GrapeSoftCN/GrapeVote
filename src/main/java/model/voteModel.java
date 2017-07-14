package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import JGrapeSystem.jGrapeFW_Message;
import apps.appsProxy;
import check.formHelper;
import check.formHelper.formdef;
import database.DBHelper;
import database.db;
import nlogger.nlogger;

public class voteModel {
	private static DBHelper vote;
	private static formHelper _form;
	private JSONObject _obj = new JSONObject();

	static {
		vote = new DBHelper(appsProxy.configValue().get("db").toString(), "vote");
		_form = vote.getChecker();
	}

	private db bind() {
		return vote.bind(String.valueOf(appsProxy.appid()));
	}

	public voteModel() {
		_form.putRule("name", formdef.notNull);
		_form.putRule("vote", formdef.notNull);
	}

	public String AddVote(JSONObject object) {
		String info = "";
		if (object != null) {
			if (!_form.checkRule(object)) {
				return resultMessage(1, "");
			}
			info = bind().data(object).insertOnce().toString();
		}
		if (("").equals(info)) {
			resultMessage(99);
		}
		return resultMessage(find(info));
	}

	@SuppressWarnings("unchecked")
	public int updateVote(String mid, JSONObject object) {
		int code = 99;
		if (object != null) {
			try {
				if (object.containsKey("vote")) {
					object.put("vote", object.get("vote").toString());
				}
				code = bind().eq("_id", new ObjectId(mid)).data(object).update() != null ? 0 : 99;
			} catch (Exception e) {
				nlogger.logout(e);
				code = 99;
			}
		}
		return code;
	}

	public int deleteVote(String mid) {
		int code = 99;
		try {
			JSONObject object = bind().eq("_id", new ObjectId(mid)).delete();
			code = (object != null ? 0 : 99);
		} catch (Exception e) {
			nlogger.logout(e);
			code = 99;
		}
		return code;
	}

	public int deleteVote(String[] mids) {
		int code = 99;
		try {
			bind().or();
			for (int i = 0, len = mids.length; i < len; i++) {
				bind().eq("_id", new ObjectId(mids[i]));
			}
			long codes = bind().deleteAll();
			code = (Integer.parseInt(String.valueOf(codes)) == mids.length ? 0 : 99);
		} catch (Exception e) {
			nlogger.logout(e);
			code = 99;
		}
		return code;
	}

	public String find(JSONObject fileInfo) {
		JSONArray array = null;
		if (fileInfo != null) {
			try {
				array = new JSONArray();
				for (Object object2 : fileInfo.keySet()) {
					bind().eq(object2.toString(), fileInfo.get(object2.toString()));
				}
				array = bind().limit(30).select();
			} catch (Exception e) {
				nlogger.logout(e);
				array = null;
			}
		}
		return resultMessage(array);
	}

	public JSONObject find(String vid) {
		JSONObject object = null;
		try {
			object = new JSONObject();
			object = bind().eq("_id", new ObjectId(vid)).find();
		} catch (Exception e) {
			nlogger.logout(e);
			object = null;
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize) {
		JSONObject object = null;
		try {
			JSONArray array = bind().page(idx, pageSize);
			object = new JSONObject();
			object.put("totalSize", (int) Math.ceil((double) bind().count() / pageSize));
			object.put("currentPage", idx);
			object.put("pageSize", pageSize);
			object.put("data", array);
		} catch (Exception e) {
			nlogger.logout(e);
			object = null;
		}
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize, JSONObject fileInfo) {
		JSONObject object = null;
		if (fileInfo != null) {
			try {
				for (Object object2 : fileInfo.keySet()) {
					if ("_id".equals(object2.toString())) {
						bind().eq("_id", new ObjectId(fileInfo.get("_id").toString()));
					}
					bind().eq(object2.toString(), fileInfo.get(object2.toString()));
				}
				JSONArray array = bind().dirty().page(idx, pageSize);
				object = new JSONObject();
				object.put("totalSize", (int) Math.ceil((double) bind().count() / pageSize));
				object.put("currentPage", idx);
				object.put("pageSize", pageSize);
				object.put("data", array);
			} catch (Exception e) {
				object = null;
			}finally {
				bind().clear();
			}
		}
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public int votes(String vid, JSONObject object) {
		int code = 99;
		if (object != null) {
			try {
				JSONObject objects = new JSONObject();
				JSONArray newarray = new JSONArray();
				// 获取当前投票
				JSONObject _obj = find(vid);
				String votes = _obj.get("vote").toString();
				JSONArray array = JSONArray.toJSONArray(votes);
				for (int i = 0; i < array.size(); i++) {
					JSONObject object2 = (JSONObject) array.get(i);
					if (object2.get("itemid").toString().equals(object.get("itemid"))) {
						object2.put("count", Integer.parseInt(object2.get("count").toString()) + 1);
					}
					newarray.add(object2);
				}
				objects.put("vote", newarray.toString());
				code = bind().eq("_id", new ObjectId(vid)).data(objects).update() != null ? 0 : 99;
			} catch (Exception e) {
				nlogger.logout(e);
				code = 99;
			}
		}
		return code;
	}

	/**
	 * 将map添加至JSONObject中
	 * 
	 * @param map
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject AddMap(HashMap<String, Object> map, JSONObject object) {
		if (object!=null) {
			if (map.entrySet() != null) {
				Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
					if (!object.containsKey(entry.getKey())) {
						object.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		return object;
	}

	public String resultMessage(int num) {
		return resultMessage(num, "");
	}

	@SuppressWarnings("unchecked")
	public String resultMessage(JSONObject object) {
		if (object == null) {
			object = new JSONObject();
		}
		_obj.put("records", object);
		return resultMessage(0, _obj.toString());
	}

	@SuppressWarnings("unchecked")
	public String resultMessage(JSONArray array) {
		if (array == null) {
			array = new JSONArray();
		}
		_obj.put("records", array);
		return resultMessage(0, _obj.toString());
	}

	public String resultMessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		case 1:
			msg = "必填项没有填";
			break;
		default:
			msg = "其它异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
