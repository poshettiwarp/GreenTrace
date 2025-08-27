package com.greenWarrior.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.service.TreeService;

@RestController
@RequestMapping("/api/tree-search")
public class TreeSearchController {

	@Autowired
	private TreeService treeService;

	/* Search by Planter */

	@GetMapping("/by-planter")
	public ResponseEntity<List<TreeResponseDTO>> getTreePlantedBy(@RequestParam String name) {

		return ResponseEntity.ok(treeService.getTreePlantedBy(name));
	}

	/* Search by Local name of plant */

	@GetMapping("/by-local-name")
	public ResponseEntity<List<TreeResponseDTO>> getTreeByLocalName(@RequestParam String name) {

		return ResponseEntity.ok(treeService.getTreeByLocalName(name));
	}

	/* Search by species name of plant */

	@GetMapping("/by-species-name")
	public ResponseEntity<List<TreeResponseDTO>> getTreeByScientificName(@RequestParam String name) {

		return ResponseEntity.ok(treeService.getTreeBySpeciesName(name));
	}

	/* Search by date of Plantation */

	@GetMapping("/by-date")
	public ResponseEntity<List<TreeResponseDTO>> getTreeByDate(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {

		return ResponseEntity.ok(treeService.getTreeByDate(date));
	}

	/* Search by date range */

	@GetMapping("/by-date-range")
	public ResponseEntity<List<TreeResponseDTO>> getTreeByDateOfPlantationBetween(
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = ISO.DATE)LocalDate endDate) {
		return ResponseEntity.ok(treeService.getTreeByDateOfPlantationBetween(startDate, endDate));
	}

	/* Search by growth stage */

	@GetMapping("/by-stage")
	public ResponseEntity<List<TreeResponseDTO>> getTreeByGrowthStage(@RequestParam String stage) {
		return ResponseEntity.ok(treeService.getTreeByGrowthStage(stage));
	}


}
