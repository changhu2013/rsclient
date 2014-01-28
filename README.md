==============================================================

 rsclient 是一个轻量级的后端服务编写框架，用于编写后端服务API

==============================================================

如何使用该框架：


1 创建一个Mavne WEB项目

2 添加对rsclient的依赖

		<dependency>
			<groupId>mobi.dadoudou</groupId>
			<artifactId>rsclient.core</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

		<dependency>
			<groupId>mobi.dadoudou</groupId>
			<artifactId>rsclient.web</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

		<dependency>
			<groupId>mobi.dadoudou</groupId>
			<artifactId>rsclient.plugin.spring</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

3 修改web.xml

	<servlet>
		<servlet-name>RSSERVICE_CONTROLLER</servlet-name>
		<servlet-class>mobi.dadoudou.rsclient.web.RSServiceController</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>RSSERVICE_CONTROLLER</servlet-name>
		<url-pattern>*.rsc</url-pattern>
	</servlet-mapping>

4 编写spring.xml文件，spring.xml文件示例如下

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="
	       http://www.springframework.org/schema/beans 
	       http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
		<bean id="dataSource" destroy-method="close"
			class="org.apache.commons.dbcp.BasicDataSource">
			<property name="driverClassName" value="oracle.jdbc.driver.OracleDriver">
			</property>
			<property name="url" value="jdbc:oracle:thin:@192.168.168.144:1521:HPORA">
			</property>
			<property name="username" value="muapp10g">
			</property>
			<property name="password" value="ceshiku">
			</property>
		</bean>
	
		<bean id="templateDao" class="com.riambsoft.kanban.dao.impl.TemplateDaoImpl">
			<property name="dataSource" ref="dataSource"></property>
		</bean>
	
		<bean id="templateService" class="com.riambsoft.kanban.service.TemplateService">
			<property name="dao" ref="templateDao"></property>
		</bean>
	
	</beans>


5 编写rsclient.xml文件，示例文件如下

	<?xml version="1.0" encoding="UTF-8"?>
	<rsclient>
	
		<plugins>
			<plugin id="spring"
				class="com.riambsoft.rsclient.plugin.spring.SpringObjectFactory">
				<property name="contextConfigLocation" value="spring.xml"></property>
			</plugin>
		</plugins>
	
		<services>
			
			<!-- 发布服务,服务的地址为  kanban.rsc -->
			<service id="kanban">
				<bean class="mobi.dadoudou.kanban.service.KanbanService" />
			</service>
			
		</services>
	
	</rsclient>


==============================================================

