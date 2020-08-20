package com.warys.scrooge.domain.model.user;

import com.warys.scrooge.domain.model.GenericModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class User extends GenericModel implements Session {

    @Size(min = 5, max = 15, message = "username must be between 5 and 15 characters")
    private String username;
    @Email(message = "Email should be valid")
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
