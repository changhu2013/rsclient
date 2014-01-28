package mobi.dadoudou.rsclient.core;

import java.util.ArrayList;
import java.util.List;

import mobi.dadoudou.rsclient.core.exception.MethodException;


public class ServiceMethod {

	private String name;

	private List<Parameter> parameters;

	private Parameter returnValue;

	private List<String> beforeMethods = new ArrayList<String>();

	private List<String> afterMethods = new ArrayList<String>();

	private ParameterCreator parameterCreator;

	public ServiceMethod(String name) throws MethodException {
		this.setName(name);
		this.parameterCreator = new ParameterCreator();
		this.parameters = new ArrayList<Parameter>();
		this.setReturnValue(this.parameterCreator.createObjectParameter(null,
				Void.TYPE));
	}

	public void setName(String name) throws MethodException {
		if (name == null || "".equals(name.trim())) {
			throw new MethodException("method name is null or blank!");
		}
		this.name = name.trim();
	}

	public String getName() {
		return this.name;
	}

	public Class<?>[] getParameterClasses() {
		int size = this.parameters.size();
		Class<?>[] clazz = new Class[size];
		for (int i = 0; i < size; i++) {
			Parameter parameter = (Parameter) this.parameters.get(i);
			clazz[i] = parameter.getClazz();
		}
		return clazz;
	}

	public String[] getParameterNames() {
		int size = this.parameters.size();
		String[] names = new String[size];
		for (int i = 0; i < size; i++) {
			Parameter parameter = (Parameter) this.parameters.get(i);
			names[i] = parameter.getName();
		}
		return names;
	}

	public ServiceMethod addParameter(Parameter parameter) {
		this.parameters.add(parameter);
		return this;
	}

	public List<Parameter> getParameters() {
		return this.parameters;
	}

	public ServiceMethod setReturnValue(Parameter returnValue) {
		this.returnValue = returnValue;
		return this;
	}

	public Parameter getReturnValue() {
		return this.returnValue;
	}

	public ServiceMethod addBeforeMethod(String name) {
		this.beforeMethods.add(name);
		return this;
	};

	public List<String> getBeforeMethod() {
		return this.beforeMethods;
	}

	public ServiceMethod addAfterMethod(String name) {
		this.afterMethods.add(name);
		return this;
	}

	public List<String> getAfterMethod() {
		return this.afterMethods;
	}

	public void setParameterCreator(ParameterCreator parameterCreator) {
		this.parameterCreator = parameterCreator;
	}

	public ServiceMethod addCharacterParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createCharacterParameter(parameterName));
	}

	public ServiceMethod addByteParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createByteParameter(parameterName));
	}

	public ServiceMethod addShortParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createShortParameter(parameterName));
	}

	public ServiceMethod addLongParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createLongParameter(parameterName));
	}

	public ServiceMethod addFloatParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createFloatParameter(parameterName));
	}

	public ServiceMethod addDoubleParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createDoubleParameter(parameterName));
	}

	public ServiceMethod addIntegerParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createIntegerParameter(parameterName));
	}

	public ServiceMethod addBooleanParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createBooleanParameter(parameterName));
	}

	public ServiceMethod addStringParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createStringParameter(parameterName));
	}

	public ServiceMethod addMapParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createMapParameter(parameterName));
	}

	public ServiceMethod addListParameter(String parameterName) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createListParameter(parameterName));
	}

	public ServiceMethod addObjectParameter(String parameterName,
			Class<?> parameterClass) {
		return (ServiceMethod) this.addParameter(this.parameterCreator
				.createObjectParameter(parameterName, parameterClass));
	}

	public ServiceMethod setObjectReturnValue(Class<?> returnValueClass,
			String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createObjectParameter(returnValueName, returnValueClass));
	}

	public ServiceMethod setObjectReturnValue(Class<?> returnValueClass) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createObjectParameter(null, returnValueClass));
	}

	public ServiceMethod setCharacterReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createCharacterParameter(returnValueName));
	}

	public ServiceMethod setCharacterReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createCharacterParameter(null));
	}

	public ServiceMethod setByteReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createByteParameter(returnValueName));
	}

	public ServiceMethod setByteReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createByteParameter(null));
	}

	public ServiceMethod setShortReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createShortParameter(returnValueName));
	}

	public ServiceMethod setShortReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createShortParameter(null));
	}

	public ServiceMethod setFloatReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createFloatParameter(returnValueName));
	}

	public ServiceMethod setFloatReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createFloatParameter(null));
	}

	public ServiceMethod setDoubleReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createDoubleParameter(returnValueName));
	}

	public ServiceMethod setDoubleReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createDoubleParameter(null));
	}

	public ServiceMethod setLongReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createLongParameter(returnValueName));
	}

	public ServiceMethod setLongReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createLongParameter(null));
	}

	public ServiceMethod setIntegerReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createIntegerParameter(returnValueName));
	}

	public ServiceMethod setIntegerReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createIntegerParameter(null));
	}

	public ServiceMethod setBooleanReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createBooleanParameter(returnValueName));
	}

	public ServiceMethod setBooleanReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createBooleanParameter(null));
	}

	public ServiceMethod setStringReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createStringParameter(returnValueName));
	}

	public ServiceMethod setStringReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createStringParameter(null));
	}

	public ServiceMethod setMapReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createMapParameter(returnValueName));
	}

	public ServiceMethod setMapReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createMapParameter(null));
	}

	public ServiceMethod setListReturnValue(String returnValueName) {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createListParameter(returnValueName));
	}

	public ServiceMethod setListReturnValue() {
		return (ServiceMethod) this.setReturnValue(this.parameterCreator
				.createListParameter(null));
	}

}
