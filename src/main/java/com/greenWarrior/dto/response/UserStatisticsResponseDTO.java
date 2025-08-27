package com.greenWarrior.dto.response;

import java.time.LocalDate;

public class UserStatisticsResponseDTO {

	private String userName;
	private LocalDate firstPlantationDate;
	private LocalDate recentPlantationDate;
	private int totalTreesPlanted;
	private int aliveTrees;
	private int totalSpeciesPlanted;
	private int seedlingTrees;
	private int youngTrees;
	private int matureTrees;
	private int deadTrees;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDate getFirstPlantationDate() {
		return firstPlantationDate;
	}

	public void setFirstPlantationDate(LocalDate firstPlantationDate) {
		this.firstPlantationDate = firstPlantationDate;
	}

	public LocalDate getRecentPlantationDate() {
		return recentPlantationDate;
	}

	public void setRecentPlantationDate(LocalDate recentPlantationDate) {
		this.recentPlantationDate = recentPlantationDate;
	}

	public int getTotalTreesPlanted() {
		return totalTreesPlanted;
	}

	public void setTotalTreesPlanted(int totalTreesPlanted) {
		this.totalTreesPlanted = totalTreesPlanted;
	}

	public int getAliveTrees() {
		return aliveTrees;
	}

	public void setAliveTrees(int aliveTrees) {
		this.aliveTrees = aliveTrees;
	}

	public int getTotalSpeciesPlanted() {
		return totalSpeciesPlanted;
	}

	public void setTotalSpeciesPlanted(int totalSpeciesPlanted) {
		this.totalSpeciesPlanted = totalSpeciesPlanted;
	}

	public int getSeedlingTrees() {
		return seedlingTrees;
	}

	public void setSeedlingTrees(int seedlingTrees) {
		this.seedlingTrees = seedlingTrees;
	}

	public int getYoungTrees() {
		return youngTrees;
	}

	public void setYoungTrees(int youngTrees) {
		this.youngTrees = youngTrees;
	}

	public int getMatureTrees() {
		return matureTrees;
	}

	public void setMatureTrees(int matureTrees) {
		this.matureTrees = matureTrees;
	}

	public int getDeadTrees() {
		return deadTrees;
	}

	public void setDeadTrees(int deadTrees) {
		this.deadTrees = deadTrees;
	}

}
