/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple JavaBean domain object representing a rehab.
 *
 * @author Ken Krebs
 */
@Entity
@Table(name = "rehab")
public class Rehab extends BaseEntity {

	/**
	 * Holds value of property date.
	 */
	@Column(name = "rehab_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate	date;

	@Column(name = "rehab_time")
	@NotNull
	private Integer		time;

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet			pet;

	@OneToOne(cascade = CascadeType.ALL)
	private Trainer		trainer;


	public Rehab() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public Integer getTime() {
		return this.time;
	}

	public void setTime(final Integer time) {
		this.time = time;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Pet getPet() {
		return this.pet;
	}

	public void setPet(final Pet pet) {
		this.pet = pet;
	}

	public void setTrainer(final Trainer trainer) {
		this.trainer = trainer;
	}
}
