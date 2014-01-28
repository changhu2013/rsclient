package mobi.dadoudou.rsclient.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mobi.dadoudou.rsclient.core.exception.MethodException;


public abstract class Service {

	private ServiceMethodMap methodMap;

	public Map<Thread, VariablePool> localVariablePool = Collections
			.synchronizedMap(new HashMap<Thread, VariablePool>());

	public abstract void registerMethods(ServiceMethodMap mm) throws Exception;

	public Service() {
		methodMap = new ServiceMethodMap();
		try {
			registerMethods(methodMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final ServiceMethod getMethod(String methodName)
			throws MethodException {
		try {
			return methodMap.getMethod(methodName);
		} catch (Exception e) {
			throw new MethodException("未能找到业务方法" + methodName);
		}
	}

	public Variable run(String methodName, VariablePool variablePool)
			throws MethodException {

		localVariablePool.put(Thread.currentThread(), variablePool);

		Variable variable = invoke(getMethod(methodName), this, variablePool);

		localVariablePool.remove(Thread.currentThread());

		return variable;
	}

	private Variable invoke(ServiceMethod method, Service service,
			VariablePool pool) throws MethodException {
		try {

			// 执行Before方法
			beforeInvoke(method, service, pool);

			// 执行当前方法
			java.lang.reflect.Method rfMethod = service.getClass().getMethod(
					method.getName(), method.getParameterClasses());

			Object value = rfMethod.invoke(service,
					pool.getValues(method.getParameters()));

			Variable result = pool.getVariable(method.getReturnValue(), value);

			// 执行After方法
			afterInvoke(method, service, pool);

			// 返回当前方法执行结果
			return result;
		} catch (Exception e) {
			throw new MethodException("执行方法异常", e);
		}
	}

	private void beforeInvoke(ServiceMethod method, Service service,
			VariablePool pool) throws MethodException {
		List<String> list = method.getBeforeMethod();
		for (Iterator<String> i = list.iterator(); i.hasNext();) {
			invoke(service.getMethod(i.next()), service, pool);
		}
	}

	private void afterInvoke(ServiceMethod method, Service service,
			VariablePool pool) throws MethodException {
		List<String> list = method.getAfterMethod();
		for (Iterator<String> i = list.iterator(); i.hasNext();) {
			invoke(service.getMethod((String) i.next()), service, pool);
		}
	}

	public final VariablePool getCurrentVariablePool() {
		return (VariablePool) localVariablePool.get(Thread.currentThread());
	}

	@Override
	public int hashCode() {
		return getClass().getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return getClass().getName().equals(obj.getClass().getName());
	}
}
