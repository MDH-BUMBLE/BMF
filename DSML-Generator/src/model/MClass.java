package model;

import java.util.List;

public class MClass {
	public Boolean isAbstract = false;
	public String name;
	public List<Inheritance> inheritances;
	public List<Containment> containments;
	public List<Association> associations;
	public List<Attribute> attributes;
	public List<Operation> operations;
}
