/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import java.sql.Time;
import java.time.LocalDate;

/**
 * Simple JavaBean domain object representing a rehab.
 *
 * @author Ken Krebs
 */
@Entity
@Table(name = "rehab")
public class Rehab extends BaseEntity {

	@Column(name = "rehab_date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate date;

	@Column(name = "rehab_time")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Time time1;

	@NotEmpty
	@Column(name = "description")
	private String description;

	/**
	 * Holds value of property pet.
	 */
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	private Time time;

	
	public Rehab() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	
	public Time getStartTime() {
		return this.time1;
			}

	public void setStartTime(Time time1) {
		this.time = time1;
	}
		
	
	public String getDescription() {
		return this.description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	
	public Pet getPet() {
		return this.pet;
	}

	
	public void setPet(Pet pet) {
		this.pet = pet;
	}

}

