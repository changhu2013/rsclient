package mobi.dadoudou.rsclient.web.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

public class SuperParserTest {

	@Test
	public void a() throws ParserException {
		String json = "{'metaData':{'paramNames':{'start':'start','limit':'limit','sort':'sort','dir':'dir'},'idProperty':'kanban_id','root':'data','totalProperty':'total','successProperty':'success','messageProperty':'message','limit':20},'xaction':'read'}";
		Map obj = (Map) SuperParser.marshal(json, HashMap.class);
		printMap(obj);
	}

	private void printMap(Map map) {
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			Object value = map.get(key);
			System.out.println(key + " : " + value + " " + value.getClass());
		}
	}

	@Test
	public void b() throws ParserException {
		String json = "[{id : 100, name:'tiger'}]";
		List list = (List) SuperParser.marshal(json, List.class);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			System.out.println(obj + " " + obj.getClass());
		}
	}

	@Test
	public void c() throws ParserException {

		String json = "{id : 100, name:'tiger'}";

		JSONObject jo = JSONObject.fromObject(json);

		Map cm = new HashMap();
		cm.put("children", Child.class);

		Person p = (Person) JSONObject.toBean(jo, Person.class);

		System.out.println(p);
	}

	@Test
	public void d() {

		String json = "{id : 100, name:'tiger'}";
		JSONObject jo = JSONObject.fromObject(json);

		Object obj = JSONObject.toBean(jo, Person.class);

		System.out.println(obj.getClass());
	}

	@Test
	public void e() throws ParserException {

		List list = new ArrayList();
		for (int i = 0; i < 10; i++) {

			Person p = new Person();
			p.setId("100");
			p.setName("name" + i);

			Child c = new Child();
			c.setId("100" + i);
			c.setName("child_" + i);

			Child c1 = new Child();
			c1.setId("100" + i);
			c1.setName("child_" + i);

			Child c2 = new Child();
			c2.setId("100" + i);
			c2.setName("child_" + i);

			List cs = new ArrayList();
			cs.add(c1);
			cs.add(c2);

			p.setChild(c);
			p.setChildren(cs);
			list.add(p);

		}

		String result = SuperParser.unmarshal(list);

		System.out.println(result);
	}

}
