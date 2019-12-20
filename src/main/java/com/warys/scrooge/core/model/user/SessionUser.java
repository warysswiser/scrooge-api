package com.warys.scrooge.core.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface SessionUser extends UserDetails {

    String getId();

    void setId(String id);

    default void setAuthorities(Collection<? extends GrantedAuthority> authorities) {}

    void setAccountNonExpired(boolean accountNonExpired);

    void setAccountNonLocked(boolean accountNonLocked);

    void setCredentialsNonExpired(boolean credentialsNonExpired);

    void setEnabled(boolean enabled);

}
