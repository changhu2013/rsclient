package mobi.dadoudou.rsclient.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StoreMetaData {

	private boolean loadMetaData;

	private Map<String, String> paramNames;

	private Map<String, Object> data;

	private Log logger = LogFactory.getLog(StoreMetaData.class);

	public StoreMetaData() {
		this.data = new HashMap<String, Object>();
		this.initDefaultProperty(this.data);
	}

	public StoreMetaData(Map<String, Object> data) {
		this.data = data == null ? new HashMap<String, Object>() : data;
		this.initDefaultProperty(this.data);
	}

	@SuppressWarnings("unchecked")
	private void initDefaultProperty(Map<String, Object> data) {

		String loadMetaData = (String) data.get("loadMetaData");
		this.loadMetaData = "true".equals(loadMetaData) ? true : false;
		logger.info("loadMetaData : " + loadMetaData);

		this.setParamNames((Map<String, String>) data.get("paramNames"));
		this.setSortInfo((Map<String, String>) data.get("sortInfo"));
		this.setGroupInfo((Map<String, String>) data.get("groupInfo"));

		this.setRoot((String) data.get("root"));
		this.setIdProperty((String) data.get("idProperty"));
		this.setTotalProperty((String) data.get("totalProperty"));
		this.setSuccessProperty((String) data.get("successProperty"));
		this.setMessageProperty((String) data.get("messageProperty"));

		this.setFields((List<Map<String, Object>>) data.get("fields"));
	}

	/**
	 * 是否加载metaData
	 * 
	 * @return
	 */
	public boolean isLoad() {
		return this.loadMetaData;
	}

	/**
	 * 设置参数名字
	 * 
	 * @param {@link Map} paramsNames
	 */
	public void setParamNames(Map<String, String> paramNames) {

		paramNames = paramNames == null ? new HashMap<String, String>()
				: paramNames;
		String start = (String) paramNames.get("start");

		if (start == null || "".equals(start)) {
			paramNames.put("start", "start");
		}
		String limit = (String) paramNames.get("limit");
		if (limit == null || "".equals(limit)) {
			paramNames.put("limit", "limit");
		}
		String sort = (String) paramNames.get("sort");
		if (sort == null || "".equals(sort)) {
			paramNames.put("sort", "sort");
		}
		String dir = (String) paramNames.get("dir");
		if (dir == null || "".equals(dir)) {
			paramNames.put("dir", "dir");
		}
		this.paramNames = paramNames;
	}

	/**
	 * 设置排序信息
	 * 
	 * @param sortInfo
	 */
	public void setSortInfo(Map<String, String> sortInfo) {

		sortInfo = sortInfo == null ? new HashMap<String, String>() : sortInfo;

		String sortProperty = (String) this.paramNames.get("sort");
		String dirProperty = (String) this.paramNames.get("dir");

		if (sortInfo.get(sortProperty) != null
				&& sortInfo.get(dirProperty) != null) {
			this.data.put("sortInfo", sortInfo);
		}
	}

	/**
	 * 设置分组信息
	 * 
	 * @param groupInfo
	 */
	public void setGroupInfo(Map<String, String> groupInfo) {

		groupInfo = groupInfo == null ? new HashMap<String, String>()
				: groupInfo;

		String groupBy = (String) groupInfo.get("groupBy");
		String groupDir = (String) groupInfo.get("groupDir");

		if (groupBy != null && groupDir != null) {
			this.data.put("groupInfo", groupInfo);
		}
	}

	/**
	 * 将字段 field 进行 ASC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldASC(String field) {

		String sortProperty = (String) this.paramNames.get("sort");
		String dirProperty = (String) this.paramNames.get("dir");

		Map<String, String> sortInfo = new HashMap<String, String>();

		sortInfo.put(sortProperty, field);
		sortInfo.put(dirProperty, "ASC");

		this.data.put("sortInfo", sortInfo);
	}

	/**
	 * 将字段 field 进行 DESC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldDESC(String field) {

		String sortProperty = (String) this.paramNames.get("sort");
		String dirProperty = (String) this.paramNames.get("dir");

		Map<String, String> sortInfo = new HashMap<String, String>();

		sortInfo.put(sortProperty, field);
		sortInfo.put(dirProperty, "DESC");

		this.data.put("sortInfo", sortInfo);
	}

	public void setRoot(String root) {
		this.data.put("root", root == null || "".equals(root) ? "data" : root);
	}

	public String getRoot() {
		return (String) this.data.get("root");
	}

	public String getSummaryDataProperty() {
		return "summaryData";
	}

	public String getGroupSummaryDataProperty() {
		return "groupSummaryData";
	}

	public void setIdProperty(String idProperty) {
		this.data
				.put("idProperty",
						idProperty == null || "".equals(idProperty) ? "id"
								: idProperty);
	}

	public String getIdProperty() {
		return (String) this.data.get("idProperty");
	}

	public void setTotalProperty(String totalProperty) {
		this.data.put("totalProperty",
				totalProperty == null || "".equals(totalProperty) ? "total"
						: totalProperty);
	}

	public String getTotalProperty() {
		return (String) this.data.get("totalProperty");
	}

	public void setSuccessProperty(String successProperty) {
		this.data
				.put("successProperty",
						successProperty == null || "".equals(successProperty) ? "success"
								: successProperty);
	}

	public String getSuccessProperty() {
		return (String) this.data.get("successProperty");
	}

	public void setMessageProperty(String messageProperty) {
		this.data
				.put("messageProperty",
						messageProperty == null || "".equals(messageProperty) ? "message"
								: messageProperty);
	}

	public String getMessageProperty() {
		return (String) this.data.get("messageProperty");
	}

	public void setFields(List<Map<String, Object>> fields) {
		this.data.put("fields",
				fields == null ? new ArrayList<Map<String, Object>>() : fields);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getFields() {
		return (List<Map<String, Object>>) this.data.get("fields");
	}

	/**
	 * 设置查询头字段
	 * 
	 * @param fields
	 */
	public void setQueryField(List fields) {
		this.data.put("queryFields", fields == null ? new ArrayList() : fields);
	}

	/**
	 * 获取排序字段名称<br/>
	 * 如果没有排序字段则返回null。<br/>
	 * 
	 * @return {@link String} sortField
	 */
	@SuppressWarnings("unchecked")
	public String getSortField() {

		Map<String, String> sortInfo = (Map<String, String>) this.data
				.get("sortInfo");

		if (sortInfo != null) {

			String sortProperty = (String) this.paramNames.get("sort");
			String sort = (String) sortInfo.get(sortProperty);

			return sort == null || "".equals(sort) ? null : sort;
		} else {
			return null;
		}
	}

	/**
	 * 获取排序方式,返回值为 ASC 或 DESC 。
	 * 
	 * @return {@link String} dir
	 */
	@SuppressWarnings("unchecked")
	public String getSortDir() {

		Map<String, String> sortInfo = (Map<String, String>) this.data
				.get("sortInfo");

		if (sortInfo != null) {

			String dirProperty = (String) this.paramNames.get("dir");
			String dir = (String) sortInfo.get(dirProperty);
			if (dir != null
					&& ("ASC".equals(dir.toUpperCase()) || "DESC".equals(dir
							.toUpperCase()))) {
				return dir;
			} else {
				return "ASC";
			}
		} else {
			return "ASC";
		}
	}

	/**
	 * 获取分组字段名称
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getGroupBy() {

		Map<String, String> groupInfo = (Map<String, String>) this.data
				.get("groupInfo");

		if (groupInfo != null) {

			String groupBy = groupInfo.get("groupBy");
			return groupBy == null || "".equals(groupBy) ? null : groupBy;
		} else {
			return null;
		}
	}

	/**
	 * 获取分组排序类型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getGroupDir() {
		Map<String, String> groupInfo = (Map<String, String>) this.data
				.get("groupInfo");
		if (groupInfo != null) {
			String dir = groupInfo.get("groupDir");
			if (dir != null
					&& ("ASC".equals(dir.toUpperCase()) || "DESC".equals(dir
							.toUpperCase()))) {
				return dir;
			} else {
				return "ASC";
			}
		} else {
			return "ASC";
		}
	}

	/**
	 * 获取要查询数据开始位置
	 * 
	 * @return
	 */
	public Integer getStart() {

		String startProperty = (String) this.paramNames.get("start");
		Object start = this.data.get(startProperty);

		return start == null ? new Integer(0) : Integer.valueOf("" + start);
	}

	/**
	 * 设置要查询数据开始位置
	 * 
	 */
	public void setStart(Object start) {

		String startProperty = (String) this.paramNames.get("start");

		if (start == null) {
			this.data.remove(startProperty);
		} else {
			this.data.put(startProperty, start);
		}
	}

	/**
	 * 获取要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 * @return
	 */
	public Integer getLimit() {

		String limitProperty = (String) this.paramNames.get("limit");
		Object limit = this.data.get(limitProperty);
		return limit == null ? null : Integer.valueOf("" + limit);
	}

	/**
	 * 设置要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 */
	public void setLimit(Object limit) {
		String limitProperty = (String) this.paramNames.get("limit");
		if (limit == null) {
			this.data.remove(limitProperty);
		} else {
			this.data.put(limitProperty, limit);
		}
	}

	/**
	 * 根据键值获取参数
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.data.get(key);
	}

	/**
	 * 获取 metaData 数据Map
	 * 
	 * @return
	 */
	public Map<String, Object> getDataMap() {

		this.data.remove("loadMetaData");
		this.data.remove("paramNames");

		return this.data;
	}
}
