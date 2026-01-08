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

import es.us.dp1.lIng_04_25_26.endofline.auth.payload.response.MessageResponse;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
class UserController {

	private final UserService userService;
	private final AuthorityService authorityService;
    private final GameService gameService;

    @Autowired
	public UserController(
        UserService userService,
        AuthorityService authorityService,
        GameService gameService
    ) {
		this.userService = userService;
        this.authorityService = authorityService;
        this.gameService = gameService;
    }

	@GetMapping
	public ResponseEntity<List<User>> findAll(@RequestParam(required = false) String authorityType) {
		List<User> res;
		if (authorityType != null) {
			res = (List<User>) userService.findAllByAuthority(authorityType);
		} else
			res = (List<User>) userService.findAll();
		return ResponseEntity.ok(res);
	}

	@GetMapping("authorities")
	public ResponseEntity<List<Authority>> findAllAuths() {
		List<Authority> res = authorityService.findAll();
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<User> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(userService.findUser(id));
	}

	@GetMapping(value = "username/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable String username) {
		return ResponseEntity.ok(userService.findUser(username));
	}

	@GetMapping(value = "myself")
	public ResponseEntity<User> findMySelf() {
		return ResponseEntity.ok(userService.findCurrentUser());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> create(@RequestBody @Valid User user) {
		User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@PutMapping(value = "{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<User> update(
        @PathVariable Integer userId,
        @RequestBody @Valid User newData
    ) {
        User userToUpdate = userService.findUser(userId);
        return ResponseEntity.ok(userService.updateUser(userToUpdate, newData));
	}

	@PutMapping(value = "myself")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<User> updateMyself(@RequestBody @Valid User newData) {
        User user = userService.findCurrentUser();
	    return ResponseEntity.ok(userService.updateUser(user, newData));
    }

	@DeleteMapping(value = "{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> delete(@PathVariable int userId) {
        User user = userService.findUser(userId);
		gameService.deleteUser(user);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(value = "myself")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> deleteMyself() {
		User user = userService.findCurrentUser();
		gameService.deleteUser(user);
		return ResponseEntity.ok().build();
	}

}
