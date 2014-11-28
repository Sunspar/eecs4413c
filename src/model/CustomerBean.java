package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"account", "name"})
public class CustomerBean {
	@XmlAttribute
	private String account;
	
	@XmlElement
	private String name;
	
	public CustomerBean(String account, String name) {
		this.account = account;
		this.name = name;
	}
	
	private CustomerBean() {} // For XML marshalling, do not use this constructor
	
	public String getAccount() {
		return this.account;
	}
	
	public String getName() {
		return this.name;
	}
}
