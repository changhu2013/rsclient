package mobi.dadoudou.rsclient.web;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RSConfigParser {

	private Map<String, Object> services;

	private Map<String, Object> plugins;

	private Log logger = LogFactory.getLog(RSConfigParser.class);

	public RSConfigParser(String config) {

		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(config);

		this.plugins = initPlugins(url);
		this.services = initServices(url);
	}

	private Map<String, Object> initPlugins(URL url) {
		Map<String, Object> plugins = new HashMap<String, Object>();
		try {
			InputStream stream = url.openStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document config = builder.parse(stream);

			NodeList psnl = config.getElementsByTagName("plugins");
			if (psnl != null && psnl.getLength() > 0) {
				Node psn = psnl.item(0);
				NodeList pnl = psn.getChildNodes();

				for (int i = 0, l = pnl.getLength(); i < l; i++) {
					Node pn = pnl.item(i);
					if ("plugin".equals(pn.getNodeName())) {
						NamedNodeMap attrs = pn.getAttributes();
						String id = attrs.getNamedItem("id").getNodeValue();
						String className = attrs.getNamedItem("class")
								.getNodeValue();
						Class<?> clazz = Class.forName(className);
						Object plugin = clazz.newInstance();
						NodeList ppnl = pn.getChildNodes();

						for (int j = 0, k = ppnl.getLength(); j < k; j++) {
							Node ppn = ppnl.item(j);
							String ppnn = ppn.getNodeName();
							if ("property".equals(ppnn)) {
								NamedNodeMap ppnnm = ppn.getAttributes();
								String ppnnn = ppnnm.getNamedItem("name")
										.getNodeValue().trim();
								String ppnnv = ppnnm.getNamedItem("value")
										.getNodeValue().trim();
								String methodName = "set"
										+ ppnnn.substring(0, 1).toUpperCase()
										+ ppnnn.substring(1);
								Method setter = clazz.getMethod(methodName,
										new Class[] { String.class });
								setter.invoke(plugin, ppnnv);
							}
						}
						plugins.put(id, plugin);
					}
				}
			}
			stream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return plugins;
	}

	private Map<String, Object> initServices(URL url) {

		Map<String, Object> services = new HashMap<String, Object>();
		try {
			InputStream stream = url.openStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document config = builder.parse(stream);
			NodeList ssnl = config.getElementsByTagName("services");
			if (ssnl != null && ssnl.getLength() > 0) {
				Node ssn = ssnl.item(0);
				NodeList snl = ssn.getChildNodes();
				for (int i = 0, l = snl.getLength(); i < l; i++) {
					Node sn = snl.item(i);
					if ("service".equals(sn.getNodeName())) {
						NamedNodeMap snm = sn.getAttributes();
						String id = snm.getNamedItem("id").getNodeValue();
						Object service = null;
						NodeList cnl = sn.getChildNodes();
						for (int j = 0, k = cnl.getLength(); j < k; j++) {
							Node bn = cnl.item(j);
							if ("bean".equals(bn.getNodeName())) {
								NamedNodeMap bnas = bn.getAttributes();
								Node temp = bnas.getNamedItem("class");
								if (temp != null) {
									// 自主创建实例
									String className = temp.getNodeValue()
											.trim();
									Class<?> clazz = Class.forName(className);
									service = clazz.newInstance();
								} else {
									temp = bnas.getNamedItem("ref");
									if (temp != null) {
										// 通过插件获取实例
										String ref = bnas.getNamedItem("ref")
												.getNodeValue().trim();
										String[] path = ref.split(":");
										if (path.length >= 2) {
											String pluginName = path[0];
											String methodName = path[1];
											Object plugin = this.getPlugins()
													.get(pluginName);
											if (plugin != null) {
												Class<?> pluginClass = plugin
														.getClass();
												Class<?>[] argsClass = new Class[path.length - 2];
												Object[] args = new Object[path.length - 2];
												for (int m = 0, n = argsClass.length; m < n; m++) {
													argsClass[m] = String.class;
													args[m] = path[m + 2];
												}

												Method method = pluginClass
														.getMethod(methodName,
																argsClass);

												service = method.invoke(plugin,
														args);
											} else {
												logger.error("未找到插件:"
														+ pluginName);
											}
										}

									}
								}
							}
						}

						if (service != null) {
							services.put(id, service);
						}
					}
				}
			}
			stream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return services;
	}

	public Map<String, Object> getPlugins() {
		return plugins;
	}

	public Map<String, Object> getServices() {
		return services;
	}

}
