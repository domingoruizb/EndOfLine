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

import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.AuthoritiesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthoritiesService {

	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	public AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
	}

	@Transactional(readOnly = true)
	public List<Authorities> findAll() {
		return (List<Authorities>) authoritiesRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Authorities findAuthorityByName(String authority) {
		return authoritiesRepository.findByName(authority)
				.orElseThrow(() -> new AuthoritiesNotFoundException(authority));
	}

	@Transactional
	public void saveAuthorities(Authorities authorities) {
		authoritiesRepository.save(authorities);
	}

}
