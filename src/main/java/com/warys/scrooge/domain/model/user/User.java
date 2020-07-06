package com.warys.scrooge.domain.model.user;

import com.warys.scrooge.domain.model.GenericModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class User extends GenericModel implements Session {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
}
