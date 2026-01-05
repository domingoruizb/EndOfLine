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
package es.us.dp1.lx_xy_24_25.endofline.user;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.user.UserNotAuthenticatedException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.user.UserNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

    @Lazy
	@Autowired
	private GameService gameService;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public User saveUser(User user) throws DataAccessException {
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public User findUser(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}

	@Transactional(readOnly = true)
	public User findUser(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
            throw new UserNotAuthenticatedException();
        } else {
            return userRepository
                .findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        }
	}

	public Boolean existsUser(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public Iterable<User> findAllByAuthority(String auth) {
		return userRepository.findAllByAuthority(auth);
	}

	@Transactional
	public User updateUser(@Valid User user, Integer idToUpdate) {
		User toUpdate = findUser(idToUpdate);
		BeanUtils.copyProperties(user, toUpdate, "id");
		userRepository.save(toUpdate);

		return toUpdate;
	}

	@Transactional
	public User updateCurrentUser(@Valid User user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			throw new UserNotAuthenticatedException();
		} else {
			User currentUser = userRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new UserNotFoundException(auth.getName()));
			User toUpdate = findUser(currentUser.getId());
			BeanUtils.copyProperties(user, toUpdate, "id");
			userRepository.save(toUpdate);

			return toUpdate;
		}

	}

	@Transactional
	public void deleteUser(Integer id) {
		User toDelete = findUser(id);

		User deletedUser = userRepository.findByUsername("Deleted user").get();
		 List<Game> hostedGames = gameService.getGamesByHost(deletedUser);
    	for (Game g : hostedGames) {
        	g.setHost(deletedUser);
    	}

		List<Game> wonGames = gameService.getGamesByWinner(deletedUser);
    	for (Game g : wonGames) {
        	g.setWinner(deletedUser);
    	}

		gameService.saveGames(hostedGames);
    	gameService.saveGames(wonGames);

		this.userRepository.delete(toDelete);
	}


}
