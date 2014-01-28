package mobi.dadoudou.rsclient.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreData {

	private Map<String, Object> data;

	private StoreMetaData metaData;

	@SuppressWarnings("unchecked")
	public StoreData(Map<String, Object> data) {
		this.data = data == null ? new HashMap<String, Object>() : data;
		Map<String, Object> metaData = (Map<String, Object>) data
				.get("metaData");
		if (metaData != null) {
			this.setMetaData(new StoreMetaData(metaData));
		}
	}

	public StoreMetaData getMetaData() {
		return metaData;
	}

	/**
	 * 获取数据List <br/>
	 * read 操作 StoreData 中不包含数据，则返回null <br/>
	 * create update destroy 操作 StoreData 中包含要操作的数据，通过该方法获取这些数据 <br/>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getData() {
		return (List<Object>) data.get(metaData.getRoot());
	}

	/**
	 * 根据键值获取data中数据。
	 * 
	 * @param key
	 * @return {@link Object} object
	 */
	public Object get(String key) {
		return data.get(key);
	}

	/**
	 * 获取排序字段名称<br/>
	 * 如果没有排序字段则返回null。<br/>
	 * 
	 * @return {@link String} sortField
	 */
	public String getSortField() {
		return metaData.getSortField();
	}

	/**
	 * 获取排序方式,返回值为 ASC 或 DESC 。
	 * 
	 * @return {@link String} dir
	 */
	public String getSortDir() {
		return metaData.getSortDir();
	}

	/**
	 * 获取分组字段名称<br/>
	 * 如果没有分组字段则返回null。<br/>
	 * 
	 * @return
	 */
	public String getGroupBy() {
		return metaData.getGroupBy();
	}

	/**
	 * 获取分组字段排序方式
	 * 
	 * @return
	 */
	public String getGroupDir() {
		return metaData.getGroupDir();
	}

	/**
	 * 获取排序信息
	 * 
	 * @return {@link String} sortInfo
	 */
	public String getSortInfo() {
		String sortInfo = null;
		String groupBy = getGroupBy();
		String field = getSortField();
		if (groupBy != null && !groupBy.equals(field)) {
			String groupDir = getGroupDir();
			sortInfo = groupBy + " " + groupDir;
		}
		if (field != null) {
			return (sortInfo != null ? sortInfo + ", " : "") + field + " "
					+ getSortDir();
		} else {
			return null;
		}
	}

	/**
	 * 获取要查询数据开始位置
	 * 
	 * @return
	 */
	public Integer getStart() {
		return metaData.getStart();
	}

	/**
	 * 设置要查询数据开始位置
	 * 
	 */
	public void setStart(Object start) {
		metaData.setStart(start);
	}

	/**
	 * 获取要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 * @return
	 */
	public Integer getLimit() {
		return metaData.getLimit();
	}

	/**
	 * 设置要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 */
	public void setLimit(Object limit) {
		metaData.setLimit(limit);
	}

	/**
	 * 设置metaData
	 * 
	 * @param metaData
	 */
	public void setMetaData(StoreMetaData metaData) {
		this.metaData = metaData;
	}

	/**
	 * 设置数据List
	 * 
	 * @param data
	 */
	public void setData(List<?> data) {
		this.data.put(metaData.getRoot(), data);
	}

	/**
	 * 设置合计数据Map
	 * 
	 * @param summaryData
	 */
	public void setSummaryData(Map<String, Object> summaryData) {
		data.put(metaData.getSummaryDataProperty(), summaryData);
	}

	/**
	 * 获取合计数据Map
	 * 
	 * @param summaryData
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSummaryData() {
		return (Map<String, Object>) data
				.get(metaData.getSummaryDataProperty());
	}

	/**
	 * 设置分组合计数据
	 * 
	 * @param groupSummaryData
	 */
	public void setGroupSummaryData(Map<String, Object> groupSummaryData) {
		data.put(metaData.getGroupSummaryDataProperty(), groupSummaryData);
	}

	/**
	 * 设置数据总数
	 * 
	 * @param total
	 */
	public void setTotal(long total) {
		data.put(metaData.getTotalProperty(), Long.valueOf(total));
	}

	/**
	 * 设置成功标记
	 */
	public void setSuccess() {
		data.put(metaData.getSuccessProperty(), Boolean.valueOf(true));
	}

	/**
	 * 设置失败标记
	 */
	public void setFailure() {
		data.put(metaData.getSuccessProperty(), Boolean.valueOf(false));
	}

	/**
	 * 设置信息内容
	 */
	public void setMessage(String message) {
		data.put(metaData.getMessageProperty(), message);
	}

	/**
	 * 设置metaData fields
	 * 
	 * @param fields
	 */
	public void setMetaDataFields(List<Map<String, Object>> fields) {
		metaData.setFields(fields);
	}

	/**
	 * 设置查询头字段
	 * 
	 * @param fields
	 */
	// TODO 确认查询头字段的处理逻辑
	public void setMetaDataQeuryFields(List fields) {
		this.metaData.setQueryField(fields);
	}

	/**
	 * 将字段 field 进行 ASC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldASC(String field) {
		this.metaData.setSortFieldASC(field);
	}

	/**
	 * 将字段 field 进行 DESC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldDESC(String field) {
		this.metaData.setSortFieldDESC(field);
	}

	/**
	 * 返回数据Map,该Map中包含所有要返回的数据。<br/>
	 * 其中包括元数据metaData。
	 * 
	 * @return {@link Map} data
	 */
	public Map<String, Object> getDataMap() {
		List<Object> data = getData();
		if (data == null) {
			setData(new ArrayList<Object>());
		}
		if (metaData.isLoad() != true) {
			this.data.remove("metaData");
		} else {
			this.data.put("metaData", metaData.getDataMap());
		}
		return this.data;
	}

}
