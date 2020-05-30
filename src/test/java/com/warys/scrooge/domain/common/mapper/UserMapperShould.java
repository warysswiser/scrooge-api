package com.warys.scrooge.domain.common.mapper;

import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserMapper.class})
public class UserMapperShould {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private BeanMapper<UserDocument, User> tested = new UserMapper();

    @Test
    public void map_user_to_command() {
        UserDocument orig = new UserBuilder()
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

        final User dest = Optional.of(orig).map(tested.mapToInput()).get();

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    public void map_command_to_user() {
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
                .buildCommand();

        final UserDocument dest = Optional.of(orig).map(tested.mapToOutput()).get();

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }
}