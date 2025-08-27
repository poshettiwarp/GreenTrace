package com.greenWarrior.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.service.TreeService;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

	@Autowired
	private TreeService treeService;

	/* Create tree */

	@PostMapping("/")
	public ResponseEntity<TreeResponseDTO> createTree(@RequestBody TreeRequestDTO treeRequest) {

		return ResponseEntity.created(null).body(treeService.createTree(treeRequest));
	}

	/* Get Tree by id */

	@GetMapping("/{id}")
	public ResponseEntity<TreeResponseDTO> getPlantById(@PathVariable int id) {

		return ResponseEntity.ok(treeService.getTreeById(id));
	}

	/* Get All Trees planted By user */

	@GetMapping("/")
	public ResponseEntity<List<TreeResponseDTO>> getAllTrees() {

		return ResponseEntity.ok(treeService.getAllTrees());
	}

	/* Get all Trees by Sorting */
	@GetMapping("/sort")
	public ResponseEntity<List<TreeResponseDTO>> getAllTreesBySorting(@RequestParam String field) {

		return ResponseEntity.ok(treeService.getAllTreesBySorting(field));
	}

	/* Get all Trees by Pagination */
	@GetMapping("/all")
	public ResponseEntity<Page<TreeResponseDTO>> getAllTrees(@RequestParam int page, @RequestParam int size) {

		return ResponseEntity.ok(treeService.getAllTrees(page, size));
	}

	
	/* Get all Trees by Pagination with Sorting Order*/
	@GetMapping("/all-trees")
	public ResponseEntity<Page<TreeResponseDTO>> getAllTrees(@RequestParam int page, @RequestParam int size,@RequestParam String field) {

		return ResponseEntity.ok(treeService.getAllTrees(page, size,field));
	}

	
	/* Delete Tree by id */
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable int id) {

		treeService.deleteById(id);
	}

	/* Update Tree by id */

	@PatchMapping("/{id}")
	public ResponseEntity<TreeResponseDTO> updateTreeDetails(@PathVariable int id,
			@RequestBody TreeRequestDTO request) {

		return ResponseEntity.created(null).body(treeService.updateTreeDetails(id, request));
	}

}
