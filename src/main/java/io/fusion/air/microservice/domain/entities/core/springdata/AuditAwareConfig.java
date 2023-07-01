/**
 * (C) Copyright 2023 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.domain.entities.core.springdata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
@EnableJpaAuditing
public class AuditAwareConfig {

    /**
     * Application Startup: The auditorProvider() method is called and creates a singleton AuditorAware
     * bean. The lambda function is stored but not executed.
     *
     * Each Request: User is Extracted from the JWT Token and Set in Spring Security Context.
     *
     * During JPA Operations: When a JPA operation occurs, Spring Data calls AuditorAware.getCurrentAuditor(),
     * which executes the lambda function and retrieves the current user from Spring Security Context.
     *
     * After Each Request: The filter or interceptor clears the username from Spring Security Context.
     *
     * As a result, even though the AuditorAware bean is a singleton, it provides a request-scoped auditor
     * (or current user) as expected.
     *
     * @return
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        //  This would return the current logged in user.
        // return () -> Optional.ofNullable(MDC.get("user"));
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            return Optional.ofNullable(authentication.getName());
        };
    }
}
