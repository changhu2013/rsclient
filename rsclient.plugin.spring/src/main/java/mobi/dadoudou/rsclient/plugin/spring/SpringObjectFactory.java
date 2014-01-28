package mobi.dadoudou.rsclient.plugin.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringObjectFactory {

	private AbstractApplicationContext context;

	public SpringObjectFactory() {
		super();
	}

	public void setContextConfigLocation(String contextConfigLocation) {
		context = new ClassPathXmlApplicationContext(contextConfigLocation);
	}

	public Object getBean(String id) {
		if (context != null) {
			return context.getBean(id);
		}
		return null;
	}

}
