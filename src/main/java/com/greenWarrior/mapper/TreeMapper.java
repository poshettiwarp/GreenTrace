package com.greenWarrior.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.model.Tree;

public class TreeMapper {

	public static TreeResponseDTO getTreeResponse(Tree tree) {
		TreeResponseDTO response = new TreeResponseDTO();
		response.setId(tree.getId());
		response.setPlantedBy(tree.getPlantedBy());
		response.setLocalName(tree.getLocalName());
		response.setSpecies(tree.getSpecies());
		response.setDateOfPlantation(tree.getDateOfPlantation());
		response.setLongitude(tree.getLongitude());
		response.setLatitude(tree.getLatitude());
		response.setNearByLocation(tree.getNearByLocation());
		response.setGrowthStage(tree.getGrowthStage());
		return response;
	}

	public static Tree toEntity(TreeRequestDTO treeRequestDTO) {
		Tree tree = new Tree();

		tree.setPlantedBy(tree.getUser().getUsername());
		tree.setLocalName(treeRequestDTO.getLocalName());
		tree.setSpecies(treeRequestDTO.getSpecies());
		tree.setDateOfPlantation(LocalDate.now());
		tree.setLongitude(treeRequestDTO.getLongitude());
		tree.setLatitude(treeRequestDTO.getLatitude());
		tree.setNearByLocation(treeRequestDTO.getNearByLocation());
		tree.setGrowthStage(treeRequestDTO.getGrowthStage());
		return tree;
	}

	public static List<TreeResponseDTO> getListOfTrees(List<Tree> listOfTrees) {
		List<TreeResponseDTO> savedTree = new ArrayList<>();

		for (Tree tree : listOfTrees) {
			TreeResponseDTO response = new TreeResponseDTO();
			response.setId(tree.getId());
			response.setPlantedBy(tree.getPlantedBy());
			response.setLocalName(tree.getLocalName());
			response.setSpecies(tree.getSpecies());
			response.setDateOfPlantation(tree.getDateOfPlantation());
			response.setLongitude(tree.getLongitude());
			response.setLatitude(tree.getLatitude());
			response.setNearByLocation(tree.getNearByLocation());
			response.setGrowthStage(tree.getGrowthStage());

			savedTree.add(response);
		}
		return savedTree;
	}

	
}
