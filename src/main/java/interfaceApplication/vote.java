package interfaceApplication;

import java.util.HashMap;

import org.json.simple.JSONObject;

import json.JSONHelper;
import model.voteModel;
import time.TimeHelper;

public class vote {
	private voteModel model = new voteModel();
	private HashMap<String, Object> map = new HashMap<>();

	public vote() {
		map.put("timediff", 0);
		map.put("ismulti", 0); // 是否多选 0：单选；1：多选
		map.put("isenable", 0); // 是否启用 0：不启用；1：启用
		map.put("starttime", TimeHelper.nowMillis() + "");
	}

	public String VoteAdd(String info) {
		JSONObject object = model.AddMap(map, JSONHelper.string2json(info));
		return model.AddVote(object);
	}

	// 修改投票
	public String VoteUpdate(String vid, String info) {
		return model.resultMessage(model.updateVote(vid, JSONHelper.string2json(info)), "投票修改成功");
	}

	// 删除投票
	public String VoteDelete(String vid) {
		return model.resultMessage(model.deleteVote(vid), "投票删除成功");
	}

	// 批量删除投票
	public String VoteBatchDelete(String vid) {
		return model.resultMessage(model.deleteVote(vid.split(",")), "投票删除成功");
	}

	// 搜索投票
	public String VoteSearch(String info) {
		return model.find(JSONHelper.string2json(info));
	}

	// 分页
	public String VotePage(int idx, int pageSize) {
		return model.page(idx, pageSize);
	}

	// 条件分页
	public String VotePageBy(int idx, int pageSize, String info) {
		return model.page(idx, pageSize, JSONHelper.string2json(info));
	}

	/**
	 * 投票（增加vote中的count）
	 * 
	 * @param vid
	 *            _id
	 * @param info
	 *            投票项 {"itemid":"","itemname":"","count":""}
	 * 
	 * @return
	 */
	public String VoteSet(String vid, String info) {
		return model.resultMessage(model.votes(vid, JSONHelper.string2json(info)), "投票成功");
	}

	// 查看投票
	public String VoteCount(String _id) {
		JSONObject object = model.find(_id);
		if (object==null) {
			model.resultMessage(0, "暂无投票信息");
		}
		return model.resultMessage(JSONHelper.string2array(object.get("vote").toString()));
	}
}
