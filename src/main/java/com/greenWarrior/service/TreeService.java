package com.greenWarrior.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.response.TreeResponseDTO;

public interface TreeService {

	TreeResponseDTO createTree(TreeRequestDTO requestDTO);

	TreeResponseDTO getTreeById(long id);

	List<TreeResponseDTO> getAllTrees();

	List<TreeResponseDTO> getAllTreesBySorting(String field);

	Page<TreeResponseDTO> getAllTrees(int page, int size);

	Page<TreeResponseDTO> getAllTrees(int page, int size, String field);

	TreeResponseDTO updateTreeDetails(long id, TreeRequestDTO request);

	void deleteById(long id);

	List<TreeResponseDTO> getTreePlantedBy(String userName);

	List<TreeResponseDTO> getTreeByLocalName(String name);

	List<TreeResponseDTO> getTreeBySpeciesName(String name);

	List<TreeResponseDTO> getTreeByDate(LocalDate date);

	List<TreeResponseDTO> getTreeByDateOfPlantationBetween(LocalDate startDate, LocalDate endDate);

	List<TreeResponseDTO> getTreeByGrowthStage(String stage);

}
