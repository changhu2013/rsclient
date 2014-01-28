package mobi.dadoudou.rsclient.core;

import java.util.HashMap;
import java.util.Set;

public class ServiceMethodMap {

	private HashMap<String, ServiceMethod> methodMap;

	public ServiceMethodMap() {
		this.methodMap = new HashMap<String, ServiceMethod>();
	}

	public ServiceMethod add(String name) throws Exception {
		ServiceMethod method = new ServiceMethod(name);
		this.methodMap.put(name, method);
		return method;
	}

	public Set<String> keySet() {
		return this.methodMap.keySet();
	}

	public ServiceMethod getMethod(String name) throws Exception {
		ServiceMethod method = this.methodMap.get(name);
		if (method == null) {
			throw new Exception("未能找到方法" + name);
		} else {
			return method;
		}
	}
}
