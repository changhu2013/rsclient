package mobi.dadoudou.rsclient.web.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSON解析器。继承自{@link Parser}
 */
public class JSONParser extends Parser {

	private Log logger = LogFactory.getLog(JSONParser.class);

	/**
	 * 将传入参数编码为JSON字符串
	 * 
	 * @param bean
	 *            值对象
	 * @return {@link String} 编码后的JSON字符串
	 */
	@SuppressWarnings("rawtypes")
	public String unmarshal(Object bean) throws ParserException {

		if (bean == null) {
			return null;
		}

		Class<?> clazz = bean.getClass();
		if (clazz.isPrimitive()) {

			return this.castPrimitive(bean, clazz).toString();
		} else if (this.isWrapperPrimitiveClass(clazz)) {

			return this.castWrapperPrimitive(bean, clazz).toString();
		} else if (String.class.isAssignableFrom(clazz)) {

			return bean.toString();
		} else if (StringBuffer.class.isAssignableFrom(clazz)) {

			return bean.toString();
		} else if (StringBuilder.class.isAssignableFrom(clazz)) {

			return bean.toString();
		} else if (Map.class.isAssignableFrom(clazz)) {

			org.json.JSONObject jo = this.mapToJSONObject((Map) bean);
			return jo != null ? jo.toString() : null;
		} else if (List.class.isAssignableFrom(bean.getClass())) {

			org.json.JSONArray ja = this.listToJSONArray((List) bean);
			return ja != null ? ja.toString() : null;
		} else {

			try {

				return JSONObject.fromObject(bean).toString();
			} catch (JSONException e) {

				try {

					return JSONArray.fromObject(bean).toString();
				} catch (JSONException ee) {
					throw new ParserException(ee.getMessage());
				}
			}
		}

	}

	/**
	 * 将传入对象编码为JSON字符串，并指定值对象名字
	 * 
	 * @param name
	 *            值对象名称
	 * @param bean
	 *            值对象
	 * @return {@link String} 编码后的JSON字符串
	 */
	public String unmarshal(Object bean, String name) throws ParserException {
		if (name != null && !"".equals(name)) {
			return "{" + name + ":" + this.unmarshal(bean) + "}";
		} else {
			return this.unmarshal(bean);
		}
	}

	/**
	 * 将Java bean转换为指定数据类型
	 * 
	 * @param bean
	 *            值对象
	 * 
	 * @param clazz
	 *            目标类
	 */
	public Object marshalToObj(Object bean, Class<?> clazz)
			throws ParserException {

		JSONObject jo = JSONObject.fromObject(bean);
		Object obj = JSONObject.toBean(jo, clazz);

		return obj;
	}

	/**
	 * 将一个JSON字符串转换成Map对象
	 */
	public Object marshalToMap(Object bean) throws ParserException {

		if (bean == null) {
			return bean;
		}

		org.json.JSONTokener jt = new org.json.JSONTokener(bean.toString());
		try {

			org.json.JSONObject jo = new org.json.JSONObject(jt);
			return this.jsonObjectToMap(jo);
		} catch (Exception e) {
			throw new ParserException("json to map exception:" + e.getMessage());
		}
	}

	/**
	 * 将一个JSON字符串转换成一个List对象
	 */
	public Object marshalToList(Object bean) throws ParserException {

		if (bean == null) {
			return bean;
		}

		org.json.JSONTokener jt = new org.json.JSONTokener(bean.toString());
		try {

			org.json.JSONArray ja = new org.json.JSONArray(jt);
			return this.jsonArrayToList(ja);
		} catch (Exception e) {

			throw new ParserException("json to list exception:"
					+ e.getMessage());
		}
	}

	private List<Object> jsonArrayToList(org.json.JSONArray ja)
			throws ParserException {

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < ja.length(); i++) {
			try {
				list.add(this.JSONToObj(ja.get(i)));
			} catch (org.json.JSONException e) {
				throw new ParserException(e.getMessage());
			}
		}
		return list;
	}

	private Object JSONToObj(Object obj) throws ParserException {

		if (obj instanceof org.json.JSONArray) {

			return this.jsonArrayToList((org.json.JSONArray) obj);
		} else if (obj instanceof org.json.JSONObject) {

			return this.jsonObjectToMap((org.json.JSONObject) obj);
		} else {

			return obj.toString();
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> jsonObjectToMap(org.json.JSONObject jo)
			throws ParserException {

		Map<String, Object> map = new HashMap<String, Object>();
		
		//Iterator<String> i = jo.sortedKeys();
		
		Iterator<String> i = jo.keys();
		while (i.hasNext()) {
			String key = i.next();
			try {
				Object obj = jo.get(key);
				map.put(key, this.JSONToObj(obj));
			} catch (org.json.JSONException e) {
				throw new ParserException(e.getMessage());
			}

		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private org.json.JSONObject mapToJSONObject(Map map) throws ParserException {

		org.json.JSONObject jo = new org.json.JSONObject();
		for (Iterator keys = map.keySet().iterator(); keys.hasNext();) {

			String key = (String) keys.next();
			Object obj = this.objToJSON(map.get(key));
			try {
				jo.put(key, obj);
			} catch (org.json.JSONException e) {
				throw new ParserException(e.getMessage());
			}
		}
		return jo;
	}

	@SuppressWarnings("rawtypes")
	private org.json.JSONArray listToJSONArray(List list)
			throws ParserException {

		org.json.JSONArray ja = new org.json.JSONArray();
		for (Iterator i = list.iterator(); i.hasNext();) {

			ja.put(this.objToJSON(i.next()));
		}
		return ja;
	}

	@SuppressWarnings("rawtypes")
	private Object objToJSON(Object obj) throws ParserException {

		if (obj == null) {

			return "undefined";
		} else {

			Class clazz = obj.getClass();

			if (clazz.isPrimitive()) {
				
				return this.castPrimitive(obj, clazz);
			} else if (this.isWrapperPrimitiveClass(clazz)) {

				return this.castWrapperPrimitive(obj, clazz);
			} else if (String.class.isAssignableFrom(clazz)) {

				return obj.toString();
			} else if (StringBuffer.class.isAssignableFrom(clazz)) {

				return obj.toString();
			} else if (StringBuilder.class.isAssignableFrom(clazz)) {

				return obj.toString();
			} else if (Map.class.isAssignableFrom(clazz)) {

				return this.mapToJSONObject((Map) obj);
			} else if (List.class.isAssignableFrom(clazz)) {

				return this.listToJSONArray((List) obj);
			} else {

				try {

					String json = JSONObject.fromObject(obj).toString();
					org.json.JSONTokener jt = new org.json.JSONTokener(json);
					org.json.JSONObject jo = new org.json.JSONObject(jt);
					return jo;
				} catch (Exception e) {

					try {
						String json = JSONArray.fromObject(obj).toString();
						org.json.JSONTokener jt = new org.json.JSONTokener(json);
						org.json.JSONArray ja = new org.json.JSONArray(jt);

						return ja;
					} catch (Exception ee) {

						throw new ParserException(ee.getMessage());
					}
				}
			}
		}
	}
}
