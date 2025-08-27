package com.greenWarrior.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenWarrior.enums.GrowthStage;



@Entity
public class Tree {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String plantedBy;
	private String localName;
	private String species;
	private LocalDate dateOfPlantation;
	private double longitude;
	private double latitude;
	private String nearByLocation;
	@Enumerated(EnumType.STRING)
	private GrowthStage growthStage;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="user_id")
	private User user;

	public Tree() {

	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlantedBy() {
		return plantedBy;
	}

	public void setPlantedBy(String plantedBy) {
		this.plantedBy = plantedBy;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public LocalDate getDateOfPlantation() {
		return dateOfPlantation;
	}

	public void setDateOfPlantation(LocalDate dateOfPlantation) {
		this.dateOfPlantation = dateOfPlantation;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getNearByLocation() {
		return nearByLocation;
	}

	public void setNearByLocation(String nearByLocation) {
		this.nearByLocation = nearByLocation;
	}

	public GrowthStage getGrowthStage() {
		return growthStage;
	}

	public void setGrowthStage(GrowthStage growthStage) {
		this.growthStage = growthStage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "Tree [id=" + id + ", plantedBy=" + plantedBy + ", localName=" + localName + ", species=" + species
				+ ", dateOfPlantation=" + dateOfPlantation + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", nearByLocation=" + nearByLocation + ", growthStage=" + growthStage + "]";
	}

	

}
