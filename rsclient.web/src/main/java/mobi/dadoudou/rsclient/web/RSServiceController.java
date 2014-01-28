package mobi.dadoudou.rsclient.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mobi.dadoudou.rsclient.web.parse.ParserException;
import mobi.dadoudou.rsclient.web.parse.SuperParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.riambsoft.rsclient.core.Constants;
import com.riambsoft.rsclient.core.ServiceEngine;
import com.riambsoft.rsclient.core.Variable;
import com.riambsoft.rsclient.core.VariablePool;
import com.riambsoft.rsclient.core.exception.ServiceException;

public class RSServiceController extends HttpServlet {

	private static final long serialVersionUID = 300433319686308625L;

	private Log logger = LogFactory.getLog(RSServiceController.class);

	private ServletConfig config;

	private Map<String, Object> map;

	private ServiceEngine engine;

	private int contextPathLength = 0;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
		String contextPath =  config.getServletContext().getServletContextName();
		contextPath = "/" + contextPath;
		logger.debug("contextPath : " + contextPath); 
		
		this.contextPathLength = contextPath.length();
		this.map = Collections.synchronizedMap(new HashMap<String, Object>());
		this.engine = new ServiceEngine();
		String configLocation = config
				.getInitParameter(Constants.RSCLIENT_CONFIG_LOCATION);
		if (configLocation == null || "".equals(configLocation.trim())) {
			configLocation = Constants.RSCLIENT_DEFAULT_CONFIG_FILE;
		}
		RSConfigParser parser = new RSConfigParser(configLocation);
		Map<String, Object> services = parser.getServices();
		for (Iterator<String> iter = services.keySet().iterator(); iter
				.hasNext();) {
			String name = iter.next();
			addService(name, services.get(name));
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			resp.sendError(500, e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		super.destroy();
		for (Iterator<String> iter = this.map.keySet().iterator(); iter
				.hasNext();) {
			String name = iter.next();
			removeService(name);
		}
	}

	private void addService(String name, Object service) {
		synchronized (map) {
			Object temp = map.get(name);
			if (temp == null) {
				map.put(name, service);
				logger.info("注册服务:" + name + "  " + service);
			} else {
				logger.warn("控制器已注册服务 : " + name + "  " + temp);
			}
		}
	}

	private void removeService(String name) {
		synchronized (map) {
			Object service = map.get(name);
			if (service != null) {
				map.remove(name);
				logger.info("注销服务:" + name + "  " + service);
			} else {
				logger.warn("控制器无注册服务 : " + name);
			}
		}
	}

	private String getServiceMethodName(HttpServletRequest request) {
		String rsMethod = request.getHeader("Rs-method");
		if (rsMethod == null || "".equals(rsMethod.trim())) {
			rsMethod = request.getParameter("Rs-method");
		}
		return rsMethod;
	}

	private String getRsAccept(HttpServletRequest request) {
		String rsAccept = request.getHeader("Rs-accept");
		if (rsAccept == null || "".equals(rsAccept.trim())) {
			rsAccept = request.getParameter("Rs-accept");
		}
		return rsAccept == null ? "json" : rsAccept;
	}

	private String getRsDataType(HttpServletRequest request) {
		String rsDataType = request.getHeader("Rs-dataType");
		if (rsDataType == null || "".equals(rsDataType.trim())) {
			rsDataType = request.getParameter("Rs-dataType");
		}
		return rsDataType == null ? "json" : rsDataType;
	}

	private VariablePool getVariablePool(HttpServletRequest request,
			HttpServletResponse response, String rsDataType) {
		VariablePool pool = new RSVariablePool(request, response, rsDataType);
		// 将系统绝对路径放入变量池
		String contextRealPath = this.config.getServletContext().getRealPath(
				"/");
		pool.add(Constants.RSCLIENT_CONTEXT_REALPATH, contextRealPath);
		return pool;
	}

	private String getServiceName(HttpServletRequest req) {
		String uri = req.getRequestURI();
		
		logger.debug("get serviceName_uri:" + uri);
		logger.debug(" uri.substring(contextPathLength + 1, uri.lastIndexOf(.));" +  uri.substring(contextPathLength + 1, uri.lastIndexOf(".")));
		
		return uri.substring(contextPathLength + 1, uri.lastIndexOf("."));
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServiceException, ParserException, IOException {

		String serviceName = getServiceName(req);
		String methodName = getServiceMethodName(req);
		String accept = getRsAccept(req);
		String rsDataType = getRsDataType(req);
		VariablePool pool = getVariablePool(req, resp, rsDataType);

		Object service = null;
		synchronized (map) {
			service = map.get(serviceName);
		}
		if (service != null) {
			Variable result = engine.invoke(service, methodName, pool);
			String temp = SuperParser.unmarshal(result.getValue(),
					result.getName());
			logger.info("响应内容:" + temp);
			resp.setContentType(ContentType.get(accept) + "; charset="
					+ ContentType.getEncoding());
			resp.getWriter().append(temp);
			resp.getWriter().close();
		} else {
			resp.sendError(500, "未找到服务:" + serviceName);
		}
	}
}
