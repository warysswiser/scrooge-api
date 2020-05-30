package com.warys.scrooge.domain.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warys.scrooge.domain.model.GenericModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class User extends GenericModel implements Session {

    @NotNull
    @Size(min = 5, max = 15, message = "username must be between 5 and 15 characters")
    private String username;
    @NotNull
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private boolean accountNonExpired = true;
    @JsonIgnore
    private boolean accountNonLocked = true;
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
    }
}
