package mobi.dadoudou.rsclient.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariablePool {

	private Map<String, Variable> variableMap;

	private VariableCreator variableCreator;

	public VariablePool() {
		this.variableMap = new HashMap<String, Variable>();
		this.variableCreator = new VariableCreator();
	}

	public Set<String> variableNameSet() {
		return this.variableMap.keySet();
	}

	public Variable add(String name, Object value) {
		return this.add(name, (value == null) ? null : value.getClass(), value);
	}

	public Variable add(String name, Class<?> clazz, Object value) {
		Variable variable = this.variableCreator.createVariable(name, clazz,
				value);
		if (name != null && !"".equals(name)) {
			this.variableMap.put(name, variable);
		}
		return variable;
	}

	public Variable getVariable(String name) {
		return this.variableMap.get(name);
	}

	public Variable getVariable(Parameter parameter, Object value) {
		return this.add(parameter.getName(), parameter.getClazz(), value);
	}

	public Object[] getValues(List<Parameter> parameters) throws Exception {
		int size = parameters.size();
		Object[] values = new Object[size];
		for (int i = 0; i < size; i++) {
			Parameter parameter = parameters.get(i);
			String name = parameter.getName();
			Variable variable = this.getVariable(name);
			if (variable != null) {
				values[i] = this.getValue(variable, parameter);
			} else {
				values[i] = null;
			}
		}
		return values;
	}

	public Object getValue(Variable variable, Parameter parameter) {
		Object value = variable.getValue();
		if (variable.getClazz().isPrimitive()) {
			return value;
		} else {
			// TODO: 处理传入参数到特定数据类型的转换
			return value;
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<?> i = this.variableMap.entrySet().iterator(); i
				.hasNext();) {
			sb.append(i.next() + "\t");
		}
		return sb.toString();
	}
}
