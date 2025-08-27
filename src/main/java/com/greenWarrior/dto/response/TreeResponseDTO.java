package com.greenWarrior.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenWarrior.enums.GrowthStage;


public class TreeResponseDTO {

	private Long id;
	private String plantedBy;
	private String localName;
	private String species;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfPlantation;
	private double longitude;
	private double latitude;
	private String nearByLocation;
	private GrowthStage growthStage;
	

	public TreeResponseDTO() {

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

	
	
}
