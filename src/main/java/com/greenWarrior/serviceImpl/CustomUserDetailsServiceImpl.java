
package com.greenWarrior.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.greenWarrior.enums.ErrorCodes;
import com.greenWarrior.exception.UserNotFoundException;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.service.CustomUserDetailsService;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
					ErrorCodes.USER_NOT_FOUND.getMessage() + " with user name : " + userName);
		}

		return org.springframework.security.core.userdetails.User.withUsername(user.getUserName())
				.password(user.getPassword()).roles(user.getRole().name()).build();
	}

}
