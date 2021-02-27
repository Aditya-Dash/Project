/**
 * Employee Data Inventory Application - Model class for employee data.
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi.model;

import java.io.Serializable;

import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "Employee")
public class EmployeeData implements Serializable{

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(name = "Name")
	//@Size(min=2, message="Name should have atleast 2 characters")
	private String name;
	
	@Column(name = "Age")
	//@Size(min=15, max=70 message="Passport should have atleast 2 characters")
	private String age;
	
	public EmployeeData() {
		super();
	}

	public EmployeeData(String name, String age) {		
		super();
		this.name = name;
		this.age = age;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "EmployeeData [name=" + name + ", age=" + age + "]";
	}
}
