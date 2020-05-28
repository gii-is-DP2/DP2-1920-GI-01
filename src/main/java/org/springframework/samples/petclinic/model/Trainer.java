package org.springframework.samples.petclinic.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "trainers")
public class Trainer extends Person{
	
	//Attributes
	
	@Column(name = "email")
	@NotEmpty
	private String email;
	
	@Column(name = "phone")
	@NotEmpty
	private String phone;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trainer", fetch = FetchType.LAZY)
	private Set<Rehab>	rehabs;
	
	//Getters and Setters
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void addRehab(Rehab rehab) {
		this.rehabs.add(rehab);
	}
	
	public void removeRehab(Rehab rehab) {
		this.rehabs.remove(rehab);
	}
	
	public Set<Rehab> getRehabs(){
		return this.rehabs;
	}
	
	public void setRehabs(Set<Rehab> rehabs) {
		this.rehabs = rehabs;
	}
	
}
