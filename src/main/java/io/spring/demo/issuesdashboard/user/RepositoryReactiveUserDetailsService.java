package io.spring.demo.issuesdashboard.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class RepositoryReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository users;

    public RepositoryReactiveUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.users.findByFirstName(username)
                .map(CustomUserDetails::new);
    }

    static class CustomUserDetails extends User implements UserDetails {
        public CustomUserDetails(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_ACTUATOR", "ROLE_USER");
        }

        @Override
        public String getUsername() {
            return getFirstName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}