package com.greenWarrior.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import com.greenWarrior.model.Tree;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {

	List<Tree> findByPlantedBy(String userName);

	List<Tree> findByLocalName(String name); // for admin

	List<Tree> findByLocalNameAndUserUserName(String localName, String userName); // for Specific user

	List<Tree> findBySpecies(String name);

	List<Tree> findBySpeciesAndUserUserName(String speciesName, String userName);

	@Query(value = "SELECT * FROM Tree WHERE DATE(date_of_plantation)=:date", nativeQuery = true)
	List<Tree> findByDateOfPlantation(@Param("date") LocalDate date);

	@Query(value = "SELECT * FROM Tree INNER JOIN USER ON TREE.USER_ID=USER.ID WHERE DATE(date_of_plantation)=:date AND USER.USER_NAME=:userName", nativeQuery = true)
	List<Tree> findByDateOfPlantation(@Param("date") LocalDate date, @Param("userName") String userName);

	@Query(value = "SELECT * FROM Tree WHERE DATE(date_of_plantation) BETWEEN :startDate AND :endDate", nativeQuery = true)
	List<Tree> findByDateOfPlantationBetween(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = "SELECT * FROM TREE INNER JOIN USER ON TREE.USER_ID=USER.ID WHERE DATE(date_of_plantation) BETWEEN :startDate AND :endDate AND USER.USER_NAME=:userName", nativeQuery = true)
	List<Tree> findByDateOfPlantationBetween(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("userName") String userName);

	@Query(value = "SELECT * FROM TREE WHERE GROWTH_STAGE =:stage", nativeQuery = true)
	List<Tree> findByGrowthStage(@Param("stage") String stage);

	@Query(value = "SELECT * FROM TREE INNER JOIN USER ON TREE.USER_ID=USER.ID WHERE GROWTH_STAGE=:stage AND USER.USER_NAME=:userName", nativeQuery = true)
	List<Tree> findByGrowthStage(@Param("stage") String stage, @Param("userName") String userName);

	@Query(value = "SELECT * FROM TREE INNER JOIN USER ON TREE.USER_ID=USER.ID WHERE USER.USER_NAME=:userName", nativeQuery = true)
	List<Tree> findAllByUserUserName(@Param("userName") String userName);
	
	List<Tree> findAllByUserUserName(@Param("userName") String userName, Sort sort);
	
	Page<Tree> findAllByUserUserName(String userName, Pageable pageable);

	@Query(value = "SELECT * FROM TREE INNER JOIN USER ON TREE.USER_ID=USER.ID WHERE USER.USER_NAME=:userName ", nativeQuery = true)
	List<Tree> findAllAndUserUserNameAndSortByASC(@Param("userName") String userName);

	boolean existsByLongitudeAndLatitude(double longitude, double latitiude);

	boolean existsByLocalName(String name);

	boolean existsById(long id);

	@Query(value = "SELECT * FROM TREE U INNER JOIN USER U on T.user_id=U.id where u.id=:id", nativeQuery = true)
	List<Tree> getTreesPlantedByUser(@RequestParam long id);

	Optional<Tree> findByIdAndUserUserName(long id, String userName);

	Optional<Tree> deleteByIdAndUserUserName(long id, String userName);
}
