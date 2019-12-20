package com.warys.scrooge.command.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warys.scrooge.core.model.GenericModel;
import com.warys.scrooge.core.model.user.SessionUser;
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
public class UserCommand extends GenericModel implements SessionUser {

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
}
