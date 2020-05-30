package com.warys.scrooge.domain.model.builder;

import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Consumer;

public final class UserBuilder implements ModelBuilder<UserBuilder, UserDocument> {

    public String id;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public Collection<? extends GrantedAuthority> authorities;
    public boolean accountNonExpired;
    public boolean accountNonLocked;
    public boolean credentialsNonExpired;
    public boolean enabled;

    public UserBuilder with(Consumer<UserBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public UserDocument build() {
        UserDocument user = new UserDocument();
        user.setId(id);
        user.setCreationDate(creationDate);
        user.setUpdateDate(updateDate);
        user.setDeletionDate(deletionDate);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public User buildCommand() {
        User userCommand = new User();
        userCommand.setId(id);
        userCommand.setCreationDate(creationDate);
        userCommand.setUpdateDate(updateDate);
        userCommand.setDeletionDate(deletionDate);
        userCommand.setUsername(username);
        userCommand.setEmail(email);
        userCommand.setFirstName(firstName);
        userCommand.setLastName(lastName);
        userCommand.setAuthorities(authorities);
        userCommand.setAccountNonExpired(accountNonExpired);
        userCommand.setAccountNonLocked(accountNonLocked);
        userCommand.setCredentialsNonExpired(credentialsNonExpired);
        userCommand.setEnabled(enabled);
        return userCommand;
    }
}
