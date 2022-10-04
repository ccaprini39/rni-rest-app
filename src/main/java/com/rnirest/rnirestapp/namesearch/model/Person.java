package com.rnirest.rnirestapp.namesearch.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Collin Louis Caprini
 */

@Entity
public class Person {

    private @Id @GeneratedValue Long id; // <2>
	private String name;
	private String dob;

	private Person() {}

	public Person(String name, String rni_name, String dob, String rni_dob) {
		this.name = name;
        this.dob = dob;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDob() {
		return dob;
	}

	public void setDescription(String dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "Person{" + 
			"name='" + name + '\'' +
			", dob='" + dob.toString() + '\'' +
			'}';
	}
    
}
