package com.greenWarrior.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.greenWarrior.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByIdAndUserName(long id,String userName);

	boolean existsByEmail(String email);

	boolean existsByUserName(String userName);

	boolean existsByPhone(String phone);

	@Query(value="SELECT * FROM USER U WHERE U.ROLE=:role", nativeQuery = true)
	List<User> findByRole(@Param("role") String role);

	User findByUserName(String userName);

	List<User> findByUserNameStartsWithIgnoreCase(String userName);
	
    User findByActive(boolean status);
    
	boolean existsById(long id);
}
