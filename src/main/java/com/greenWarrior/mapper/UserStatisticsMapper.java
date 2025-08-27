package com.greenWarrior.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.greenWarrior.dto.response.UserStatisticsResponseDTO;
import com.greenWarrior.enums.ErrorCodes;
import com.greenWarrior.exception.ResourceNotFoundException;
import com.greenWarrior.model.Tree;
import com.greenWarrior.model.User;

public class UserStatisticsMapper {

	public static UserStatisticsResponseDTO getUserStatistics(User user) {

		UserStatisticsResponseDTO statistics = new UserStatisticsResponseDTO();
		
		List<Tree> trees = user.getTrees();

		if (!trees.isEmpty()) {

			List<Tree> sortedTrees = trees.stream()
					.sorted((x1, x2) -> x1.getDateOfPlantation().compareTo(x2.getDateOfPlantation()))
					.collect(Collectors.toList());

			Set<String> uniqueSpecies = new HashSet<>();
			
			int seedlingTrees = 0;
			int youngTrees = 0;
			int matureTrees = 0;
			int deadTrees = 0;

			for (Tree tree : sortedTrees) {
				uniqueSpecies.add(tree.getSpecies());

				if ("seedling".equalsIgnoreCase(tree.getGrowthStage().toString())) {
					seedlingTrees++;
				} else if ("young".equalsIgnoreCase(tree.getGrowthStage().toString())) {
					youngTrees++;
				} else if ("mature".equalsIgnoreCase(tree.getGrowthStage().toString())) {
					matureTrees++;
				} else if ("dead".equalsIgnoreCase(tree.getGrowthStage().toString())) {
					deadTrees++;
				}
			}

			int aliveTrees = seedlingTrees + youngTrees + matureTrees;

			statistics.setUserName(user.getUserName());
			statistics.setFirstPlantationDate(sortedTrees.get(0).getDateOfPlantation());
			statistics.setRecentPlantationDate(sortedTrees.get(sortedTrees.size() - 1).getDateOfPlantation());
			statistics.setTotalTreesPlanted(user.getTrees().size());
			statistics.setAliveTrees(aliveTrees);
			statistics.setTotalSpeciesPlanted(uniqueSpecies.size());
			statistics.setSeedlingTrees(seedlingTrees);
			statistics.setYoungTrees(youngTrees);
			statistics.setMatureTrees(matureTrees);
			statistics.setDeadTrees(deadTrees);

			return statistics;
		}else {
			throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
					ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
		}
		
	}

}
