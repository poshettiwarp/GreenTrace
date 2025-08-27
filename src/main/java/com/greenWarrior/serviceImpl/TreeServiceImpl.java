package com.greenWarrior.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.enums.ErrorCodes;
import com.greenWarrior.enums.Role;
import com.greenWarrior.exception.DuplicateResourceException;
import com.greenWarrior.exception.InvalidRequestException;
import com.greenWarrior.exception.ResourceNotFoundException;
import com.greenWarrior.exception.UserNotFoundException;
import com.greenWarrior.mapper.TreeMapper;
import com.greenWarrior.model.Tree;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.TreeRepository;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.service.TreeService;
import com.greenWarrior.utility.UserAuth;

@Service
public class TreeServiceImpl implements TreeService {

	@Autowired
	private TreeRepository treeRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public TreeResponseDTO createTree(TreeRequestDTO requestDTO) {

		String userName = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(userName);

		if (user != null) {

			if ((treeRepository.existsByLongitudeAndLatitude(requestDTO.getLongitude(), requestDTO.getLatitude()))) {

				throw new DuplicateResourceException(ErrorCodes.DUPLICATE_RESOURCE.getCode(),
						ErrorCodes.DUPLICATE_RESOURCE.getMessage() + " tree already exist on location ");

			} else {
				Tree tree = new Tree();
				tree.setPlantedBy(user.getUsername());
				tree.setLocalName(requestDTO.getLocalName());
				tree.setSpecies(requestDTO.getSpecies());
				tree.setDateOfPlantation(LocalDate.now());
				tree.setLongitude(requestDTO.getLongitude());
				tree.setLatitude(requestDTO.getLatitude());
				tree.setNearByLocation(requestDTO.getNearByLocation());
				tree.setGrowthStage(requestDTO.getGrowthStage());
				tree.setUser(user);

				Tree savedTree = treeRepository.save(tree);

				TreeResponseDTO response = new TreeResponseDTO();
				response.setId(savedTree.getId());
				response.setPlantedBy(savedTree.getPlantedBy());
				response.setLocalName(savedTree.getLocalName());
				response.setSpecies(savedTree.getSpecies());
				response.setDateOfPlantation(savedTree.getDateOfPlantation());
				response.setLongitude(savedTree.getLongitude());
				response.setLatitude(savedTree.getLatitude());
				response.setNearByLocation(savedTree.getNearByLocation());
				response.setGrowthStage(savedTree.getGrowthStage());

				return response;
			}

		} else {
			throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
					ErrorCodes.USER_NOT_FOUND.getMessage() + " with user name " + userName);
		}

	}

	@Override
	public TreeResponseDTO getTreeById(long id) {

		Optional<Tree> exist = treeRepository.findById(id);

		TreeResponseDTO response = new TreeResponseDTO();
		if (exist.isPresent()) {

			Tree existingTree = exist.get();

			String loggedInUser = UserAuth.getCurrentLoggedInUser();

			Optional<Tree> existByIdAndUserName = treeRepository.findByIdAndUserUserName(existingTree.getId(),
					loggedInUser);

			if (existByIdAndUserName.isPresent()) {

				Tree savedTree = existByIdAndUserName.get();

				response.setId(savedTree.getId());
				response.setPlantedBy(savedTree.getPlantedBy());
				response.setLocalName(savedTree.getLocalName());
				response.setSpecies(savedTree.getSpecies());
				response.setDateOfPlantation(savedTree.getDateOfPlantation());
				response.setLongitude(savedTree.getLongitude());
				response.setLatitude(savedTree.getLatitude());
				response.setNearByLocation(savedTree.getNearByLocation());
				response.setGrowthStage(savedTree.getGrowthStage());

				return response;
			} else {
				throw new AccessDeniedException(ErrorCodes.ACCESS_DENIED_EXCEPTION.getCode());
			}

		} else {
			throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
					ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - tree with id " + id + " is not present.");
		}

	}

	// Both Admin and User
	@Override
	public List<TreeResponseDTO> getAllTrees() {

		String userName = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(userName);
		List<TreeResponseDTO> listOfTrees = null;

		if (user.getRole().equals(Role.ADMIN)) {

			List<Tree> savedTree = treeRepository.findAll();
			if (!savedTree.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(savedTree);
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}
		} else {
			List<Tree> savedTree = treeRepository.findAllByUserUserName(user.getUserName());
			if (!savedTree.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(savedTree);
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}
		}

		return listOfTrees;

	}

	@Override
	public List<TreeResponseDTO> getAllTreesBySorting(String field) {
		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		if (user.getRole().equals(Role.ADMIN)) {

			List<Tree> trees = treeRepository.findAll(Sort.by(Sort.Direction.DESC, field));

			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees);
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}

		} else if (user.getRole().equals(Role.USER)) {
			List<Tree> trees = treeRepository.findAllByUserUserName(loggedInUser, Sort.by(Sort.Direction.DESC, field));

			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees);
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}
		}

		return listOfTrees;
	}

	// Both Admin and User
	@Override
	public Page<TreeResponseDTO> getAllTrees(int page, int size) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		Page<Tree> trees = null;

		Pageable pageable = PageRequest.of(page, size);

		if (user.getRole().equals(Role.ADMIN)) {

			trees = treeRepository.findAll(pageable);

			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees.getContent());
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree found.");
			}

		} else {

			trees = treeRepository.findAllByUserUserName(user.getUserName(), pageable);
			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees.getContent());
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}
		}

		Page<TreeResponseDTO> pageResponse = new PageImpl<>(listOfTrees, pageable, trees.getTotalElements());

		return pageResponse;
	}

	// Both Admin and User

	@Override
	public Page<TreeResponseDTO> getAllTrees(int page, int size, String field) {
		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		Page<Tree> trees = null;

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, field));

		if (user.getRole().equals(Role.ADMIN)) {

			trees = treeRepository.findAll(pageable);

			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees.getContent());
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree found.");
			}

		} else {

			trees = treeRepository.findAllByUserUserName(user.getUserName(), pageable);
			if (!trees.isEmpty()) {
				listOfTrees = TreeMapper.getListOfTrees(trees.getContent());
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted yet.");
			}
		}

		Page<TreeResponseDTO> pageResponse = new PageImpl<>(listOfTrees, pageable, trees.getTotalElements());

		return pageResponse;
	}

	// Only User
	@Override
	public TreeResponseDTO updateTreeDetails(long id, TreeRequestDTO request) {
		Optional<Tree> tree = treeRepository.findById(id);

		if (tree.isPresent()) {
			Tree existingTree = tree.get();

			String userName = UserAuth.getCurrentLoggedInUser();

			Tree savedTree = treeRepository.findByIdAndUserUserName(existingTree.getId(), userName).get();

			if (savedTree != null) {

				if (request.getLocalName() != null) {
					savedTree.setLocalName(request.getLocalName());
				}

				if (request.getSpecies() != null) {
					savedTree.setSpecies(request.getSpecies());
				}

				if (request.getNearByLocation() != null) {
					savedTree.setNearByLocation(request.getNearByLocation());
				}

				if (request.getGrowthStage() != null) {
					savedTree.setGrowthStage(request.getGrowthStage());
				}

				treeRepository.save(savedTree);
				TreeResponseDTO response = new TreeResponseDTO();
				response.setId(savedTree.getId());
				response.setPlantedBy(savedTree.getPlantedBy());
				response.setLocalName(savedTree.getLocalName());
				response.setSpecies(savedTree.getSpecies());
				response.setDateOfPlantation(savedTree.getDateOfPlantation());
				response.setLongitude(savedTree.getLongitude());
				response.setLatitude(savedTree.getLatitude());
				response.setNearByLocation(savedTree.getNearByLocation());
				response.setGrowthStage(savedTree.getGrowthStage());
				return response;

			} else {
				throw new AccessDeniedException("You Can Only Update Your Data.");
			}
		} else {
			throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
					ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " with id " + id);
		}

	}

	// Both User And Admin
	@Override
	public void deleteById(long id) {

		Optional<Tree> exist = treeRepository.findById(id);

		if (exist.isPresent()) {
			String userName = UserAuth.getCurrentLoggedInUser();
			Optional<Tree> existingTree = treeRepository.findByIdAndUserUserName(id, userName);
			if (existingTree.isPresent()) {
				treeRepository.deleteByIdAndUserUserName(id, userName);
			} else {
				throw new AccessDeniedException("You Can Only Delete Your Data.");
			}
		} else {
			throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
					ErrorCodes.INVALID_REQUEST.getMessage() + " - no resource found with id " + id
							+ " for delete operation");
		}
	}

	// Only Admin Can Access this method
	@Override
	public List<TreeResponseDTO> getTreePlantedBy(String userName) {

		String loggedInUserName = UserAuth.getCurrentLoggedInUser();

		User loggedInUser = userRepository.findByUserName(loggedInUserName);

		if (loggedInUser.getRole().equals(Role.USER)) {
			throw new AccessDeniedException("No Access.");
		}

		User user = userRepository.findByUserName(userName);

		if (user != null) {

			List<Tree> savedTrees = treeRepository.findByPlantedBy(userName);

			List<TreeResponseDTO> listOfTrees = new ArrayList<>();

			if (!savedTrees.isEmpty()) {
				ListIterator<Tree> itr = savedTrees.listIterator();

				System.out.println(savedTrees);
				System.out.println(itr != null);

				while (itr.hasNext()) {
					Tree savedTree = itr.next();
					TreeResponseDTO response = new TreeResponseDTO();
					response.setId(savedTree.getId());
					response.setPlantedBy(savedTree.getPlantedBy());
					response.setLocalName(savedTree.getLocalName());
					response.setSpecies(savedTree.getSpecies());
					response.setDateOfPlantation(savedTree.getDateOfPlantation());
					response.setLongitude(savedTree.getLongitude());
					response.setLatitude(savedTree.getLatitude());
					response.setNearByLocation(savedTree.getNearByLocation());
					response.setGrowthStage(savedTree.getGrowthStage());

					listOfTrees.add(response);
				}

				return listOfTrees;
			} else {
				throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
						ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " planted by " + userName);

			}
		} else {
			throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
					ErrorCodes.USER_NOT_FOUND.getMessage() + " with user name " + userName);
		}
	}

	// Both admin and logged-in user can get tree

	@Override
	public List<TreeResponseDTO> getTreeByLocalName(String name) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		if (user != null) {
			List<TreeResponseDTO> listOfTrees = null;
			if (user.getRole().equals(Role.ADMIN)) {

				List<Tree> savedTree = treeRepository.findByLocalName(name);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " planted with local name " + name);
				}
			} else {
				List<Tree> savedTree = treeRepository.findByLocalNameAndUserUserName(name, user.getUserName());
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " planted with local name " + name);
				}
			}

			return listOfTrees;

		} else {
			throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
					ErrorCodes.USER_NOT_FOUND.getMessage() + " with user name " + loggedInUser);
		}

	}

	// Both admin and logged-in user can get tree

	@Override
	public List<TreeResponseDTO> getTreeBySpeciesName(String name) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		if (user != null) {

			if (user.getRole().equals(Role.ADMIN)) {
				List<Tree> savedTree = treeRepository.findBySpecies(name);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " planted with local name " + name);
				}
			} else {

				List<Tree> savedTree = treeRepository.findBySpeciesAndUserUserName(name, user.getUserName());
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " planted with local name " + name);
				}

			}

		}
		return listOfTrees;
	}

	@Override
	public List<TreeResponseDTO> getTreeByDate(LocalDate date) {
		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		if (user != null) {

			if (user.getRole().equals(Role.ADMIN)) {
				List<Tree> savedTree = treeRepository.findByDateOfPlantation(date);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree planted on date " + date);
				}
			} else {

				List<Tree> savedTree = treeRepository.findByDateOfPlantation(date, user.getUserName());
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree planted on date " + date);
				}

			}

		}
		return listOfTrees;
	}

	@Override
	public List<TreeResponseDTO> getTreeByDateOfPlantationBetween(LocalDate startDate, LocalDate endDate) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		if (user != null) {

			if (user.getRole().equals(Role.ADMIN)) {
				List<Tree> savedTree = treeRepository.findByDateOfPlantationBetween(startDate, endDate);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree found.");
				}
			} else {

				List<Tree> savedTree = treeRepository.findByDateOfPlantationBetween(startDate, endDate, loggedInUser);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " - no tree planted between date " + startDate
									+ " and " + endDate);
				}

			}

		}
		return listOfTrees;
	}

	@Override
	public List<TreeResponseDTO> getTreeByGrowthStage(String stage) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		List<TreeResponseDTO> listOfTrees = null;

		if (user != null) {

			if (user.getRole().equals(Role.ADMIN)) {
				List<Tree> savedTree = treeRepository.findByGrowthStage(stage);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree found.");
				}
			} else {

				List<Tree> savedTree = treeRepository.findByGrowthStage(stage, loggedInUser);
				if (!savedTree.isEmpty()) {
					listOfTrees = TreeMapper.getListOfTrees(savedTree);
				} else {
					throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
							ErrorCodes.RESOURCE_NOT_FOUND.getMessage() + " no tree found.");
				}

			}

		}
		return listOfTrees;
	}

}
