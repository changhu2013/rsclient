package mobi.dadoudou.rsclient.web.parse;

import java.util.List;

public class Person {

	private String id;

	private String name;

	private List<Child> children;

	private Child child;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(List<Child> children) {
		this.children = children;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return getId() + " " + getName() + " " + getChildren() + " "
				+ getClass();
	}

}
