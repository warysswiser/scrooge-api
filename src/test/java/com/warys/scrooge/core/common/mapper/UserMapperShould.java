package com.warys.scrooge.core.common.mapper;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserMapper.class})
public class UserMapperShould {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private UserMapper tested = new UserMapper();

    @Test
    public void map_user_to_command() {
        User orig = new UserBuilder()
                .with(
                        o -> {
                            o.id = "userId";
                            o.firstName = "First";
                            o.lastName = "Last";
                            o.email = "email@go.com";
                            o.password = "12345678";
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        final UserCommand dest = Optional.of(orig).map(tested.mapToInput()).get();

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    public void map_command_to_user() {
        UserCommand orig = new UserBuilder()
                .with(
                        o -> {
                            o.id = "userId";
                            o.firstName = "First";
                            o.lastName = "Last";
                            o.email = "email@go.com";
                            o.password = "12345678";
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .buildCommand();

        final User dest = Optional.of(orig).map(tested.mapToOutput()).get();

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }
}