package com.warys.scrooge.core.common.util;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.GenericModel;
import com.warys.scrooge.core.model.budget.*;
import com.warys.scrooge.core.model.builder.BudgetBuilder;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanUtilShould {

    private static final String DESTINATION_ID = "destinationId";
    private static final String SOURCE_ID = "sourceId";
    private static final String ORIG_OWNER_ID = "111111";
    private static final String DEST_OWNER_ID = "222222";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Test
    public void make_a_complete_copy_of_origin_budget_when_destination_is_empty() {

        Budget orig = new BudgetBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.name = "myBudget";
                            o.ownerId = ORIG_OWNER_ID;
                            o.startDate = LocalDate.now().minusMonths(1);
                            o.endDate = LocalDate.now().plusMonths(1);
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        var dest = new Budget();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    public void make_a_complete_copy_of_origin_budget_when_destination_is_empty_and_origin_has_nested_beans() {

        Budget orig = new BudgetBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.name = "myBudget";
                            o.ownerId = ORIG_OWNER_ID;
                            o.startDate = LocalDate.now().minusMonths(1);
                            o.endDate = LocalDate.now().plusMonths(1);
                            o.plannedItems = new PlannedItems();
                            o.realItems = new RealItems();
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = null;
                        })
                .build();

        var dest = new BudgetBuilder().with(o -> o.realItems = new RealItems()).build();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).isEqualToComparingFieldByFieldRecursively(orig);
    }


    @Test
    public void make_a_complete_copy_of_origin_user_when_destination_is_empty() {

        var orig = new UserBuilder()
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

        var dest = new UserCommand();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    public void make_a_partial_copy_of_origin_when_destination_is_empty() {

        Budget orig = new BudgetBuilder()
                .with(
                        o -> {
                            o.name = "myBudget";
                            o.endDate = LocalDate.now().plusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        Budget dest = new BudgetBuilder()
                .with(
                        o -> {
                            o.id = DESTINATION_ID;
                            o.ownerId = DEST_OWNER_ID;
                            o.startDate = null;
                            o.creationDate = NOW;
                        })
                .build();

        BeanUtil.copyBean(orig, dest);

        var fieldsToIgnore = new String[]{"name", "endDate", "updateDate", "deletionDate"};

        assertThat(dest.getId()).isEqualTo(DESTINATION_ID);
        assertThat(dest.getOwnerId()).isEqualTo(DEST_OWNER_ID);
        assertThat(dest.getStartDate()).isNull();
        assertThat(dest.getCreationDate()).isEqualTo(NOW);
        assertThat(dest).isEqualToComparingOnlyGivenFields(orig, fieldsToIgnore);
    }

    @Test
    public void make_a_partial_copy_when_origin_and_destination_are_not_semantically_the_same() {

        Budget orig = new BudgetBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.name = "myBudget";
                            o.ownerId = ORIG_OWNER_ID;
                            o.startDate = LocalDate.now().minusMonths(1);
                            o.endDate = LocalDate.now().plusMonths(1);
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        User dest = new User();

        BeanUtil.copyBean(orig, dest);

        var fieldsToIgnore = new String[]{"creationDate", "updateDate", "deletionDate", "id"};
        assertThat(dest).isEqualToComparingOnlyGivenFields(orig, fieldsToIgnore);
    }


    @Test
    public void throw_InstantiationException_when_object_contains_abstract_type_as_attribute() {
        GenericModelWrapper orig = new GenericModelWrapper("name", new Budget());
        GenericModelWrapper dest = new GenericModelWrapper();

        BeanUtil.copyBean(orig, dest);

        assertThat(orig.getName()).isEqualTo(dest.getName());
        assertThat(dest.getModel()).isNull();
    }

    public class GenericModelWrapper {
        private String name;
        private GenericModel model;

        public GenericModelWrapper() {
        }

        public GenericModelWrapper(String name, GenericModel model) {
            this.name = name;
            this.model = model;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public GenericModel getModel() {
            return model;
        }

        public void setModel(GenericModel model) {
            this.model = model;
        }
    }
}