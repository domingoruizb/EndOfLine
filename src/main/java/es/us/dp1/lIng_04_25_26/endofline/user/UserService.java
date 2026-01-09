/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.us.dp1.lIng_04_25_26.endofline.user;

import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.UserNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.UserUnauthorizedException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(
        UserRepository userRepository
    ) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public User findUser(String username) {
		return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
	}

	@Transactional(readOnly = true)
	public User findUserRegistering(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Transactional(readOnly = true)
	public User findUser(Integer id) {
		return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
	}

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
            throw new UserUnauthorizedException();
        } else {
            return userRepository
                .findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        }
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAllExceptMyself() {
		User currentUser = findCurrentUser();
		return userRepository.findAllExceptMySelf(currentUser.getId());
	}

	public Iterable<User> findAllByAuthority(String authorityType) {
		return userRepository.findAllByAuthorityType(authorityType);
	}

    @Transactional
    public User saveUser(User user) throws DataAccessException {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		return userRepository.save(user);
    }

	@Transactional
	public User updateUser(User userToUpdate, UserDTO newData) {
		if (!userToUpdate.getUsername().equals(newData.getUsername()) && userRepository.existsByUsername(newData.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		BeanUtils.copyProperties(newData, userToUpdate, "id", "gamePlayer", "password");
		return userRepository.save(userToUpdate);
	}

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
