package mobi.dadoudou.rsclient.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mobi.dadoudou.rsclient.web.parse.SuperParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.riambsoft.rsclient.core.Parameter;
import com.riambsoft.rsclient.core.Variable;
import com.riambsoft.rsclient.core.VariablePool;

public class RSVariablePool extends VariablePool {

	private Map<String, Variable> sessionVariableMap;

	private Map<String, Variable> parameterVariableMap;

	private Map<String, Variable> requestVariableMap;

	private String rsDataType;

	private static Log logger = LogFactory.getLog(RSVariablePool.class);

	/**
	 * 存入变量池中的session的变量名
	 */
	public static final String PARAMETER_HTTP_SESSION = "__parameter_sesssion_key";

	/**
	 * 存入变量池中的request的变量名
	 */
	public static final String PARAMETER_HTTP_REQUEST = "__parameter_request_key";

	/**
	 * 存入变量池中的response的变量名
	 */
	public static final String PARAMETER_HTTP_RESPONSE = "__parameter_response_key";

	/**
	 * 构造方法
	 * 
	 * @param request
	 *            HTTP请求对象
	 * @param response
	 */
	public RSVariablePool(HttpServletRequest request,
			HttpServletResponse response) {
		super();

		this.sessionVariableMap = new HashMap<String, Variable>();
		this.requestVariableMap = new HashMap<String, Variable>();
		this.parameterVariableMap = new HashMap<String, Variable>();
		this.bindParameters(request, response);
	}

	public RSVariablePool(HttpServletRequest request,
			HttpServletResponse response, String rsDataType) {

		super();
		this.rsDataType = rsDataType;
		this.sessionVariableMap = new HashMap<String, Variable>();
		this.requestVariableMap = new HashMap<String, Variable>();
		this.parameterVariableMap = new HashMap<String, Variable>();
		this.bindParameters(request, response);
	}

	/**
	 * 获取变量值
	 * 
	 * @param valirable
	 *            变量
	 * @param parameter
	 *            参数
	 * @return value 值
	 */
	public Object getValue(Variable variable, Parameter parameter) {
		Object value = variable.getValue();
		if (variable.getClazz().isPrimitive()) {
			return value;
		} else {
			try {
				if (this.rsDataType != null) {
					return SuperParser.marshal(this.rsDataType, value,
							parameter.getClazz());
				} else {
					return SuperParser.marshal(value, parameter.getClazz());
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
				return value;
			}
		}
	}

	/**
	 * 添加Session中的可用变量
	 * 
	 * @param variableName
	 * @param value
	 */
	public void addSessionVariable(String variableName, Object value) {
		Variable variable = this.add(variableName, value);
		this.sessionVariableMap.put(variableName, variable);
	}

	/**
	 * 添加Request中的可用变量
	 * 
	 * @param variableName
	 * @param value
	 */
	public void addRequestVariable(String variableName, Object value) {
		this.requestVariableMap
				.put(variableName, this.add(variableName, value));
	}

	/**
	 * 添加请求参数
	 * 
	 * @param variableName
	 * @param value
	 */
	public void addParameterVariable(String variableName, Object value) {
		this.parameterVariableMap.put(variableName,
				this.add(variableName, value));
	}

	/**
	 * 将请求中的可用参数添加到变量池中
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void bindParameters(HttpServletRequest request,
			HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		this.add(PARAMETER_HTTP_SESSION, HttpSession.class, session);
		this.add(PARAMETER_HTTP_REQUEST, HttpServletRequest.class, request);		
		this.add(PARAMETER_HTTP_RESPONSE, HttpServletResponse.class, response);
		
		Enumeration<String> sessEnum = session.getAttributeNames();
		while (sessEnum.hasMoreElements()) {
			String variableName = sessEnum.nextElement();
			this.addSessionVariable(variableName, request.getSession()
					.getAttribute(variableName));
		}
		Enumeration<String> attrEnum = request.getAttributeNames();
		while (attrEnum.hasMoreElements()) {
			String variableName = (String) attrEnum.nextElement();
			this.addRequestVariable(variableName,
					request.getAttribute(variableName));
		}
		Enumeration<String> paramEnum = request.getParameterNames();
		while (paramEnum.hasMoreElements()) {
			String variableName = (String) paramEnum.nextElement();
			String parameter = request.getParameter(variableName);

			try {
				variableName = URLDecoder.decode(variableName,
						ContentType.getEncoding());
				parameter = URLDecoder.decode(parameter,
						ContentType.getEncoding());
				logger.debug(variableName + "=" + parameter);

				this.addParameterVariable(variableName, parameter);
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
			}
		}
	};

}
