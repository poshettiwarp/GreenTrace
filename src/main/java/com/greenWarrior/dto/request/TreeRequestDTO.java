package com.greenWarrior.dto.request;

import com.greenWarrior.enums.GrowthStage;

public class TreeRequestDTO {

	private String plantedBy;
	private String localName;
	private String species;
	private double longitude;
	private double latitude;
	private String nearByLocation;
	private GrowthStage growthStage;

	public TreeRequestDTO() {

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
