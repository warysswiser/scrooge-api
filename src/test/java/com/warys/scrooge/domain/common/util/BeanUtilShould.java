package com.warys.scrooge.domain.common.util;

import com.warys.scrooge.domain.model.budget.Cashflow;
import com.warys.scrooge.domain.model.builder.InflowBuilder;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BeanUtilShould {

    private static final String DESTINATION_ID = "destinationId";
    private static final String SOURCE_ID = "sourceId";
    private static final String ORIG_OWNER_ID = "111111";
    private static final String DEST_OWNER_ID = "222222";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Test
    void make_a_complete_copy_of_origin_flow_when_destination_is_empty() {

        Cashflow orig = new InflowBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.label = "my flow";
                            o.amount = 10;
                            o.ownerId = ORIG_OWNER_ID;
                            o.category = "category";
                            o.frequency = "frquency";
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        var dest = new Cashflow();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    void make_a_complete_copy_of_origin_flow_when_destination_is_empty_and_origin_has_nested_beans() {

        Cashflow orig = new InflowBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.label = "my flow";
                            o.amount = 10;
                            o.ownerId = ORIG_OWNER_ID;
                            o.category = "category";
                            o.frequency = "frquency";
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = null;
                        })
                .build();

        var dest = new InflowBuilder().build();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).usingRecursiveComparison().isEqualTo(orig);
    }


    @Test
    void make_a_complete_copy_of_origin_user_when_destination_is_empty() {

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

        var dest = new User();

        BeanUtil.copyBean(orig, dest);

        assertThat(dest).doesNotHaveSameClassAs(orig);
        assertThat(dest).isEqualToComparingFieldByField(orig);
    }

    @Test
    void make_a_partial_copy_of_origin_when_destination_is_empty() {

        Cashflow orig = new InflowBuilder()
                .with(
                        o -> {
                            o.label = "myFlow";
                            o.category = "category";
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        Cashflow dest = new InflowBuilder()
                .with(
                        o -> {
                            o.id = DESTINATION_ID;
                            o.ownerId = DEST_OWNER_ID;
                            o.category = null;
                            o.creationDate = NOW;
                        })
                .build();

        BeanUtil.copyBean(orig, dest);

        var fieldsToIgnore = new String[]{"label", "category", "updateDate", "deletionDate"};

        assertThat(dest.getId()).isEqualTo(DESTINATION_ID);
        assertThat(dest.getOwnerId()).isEqualTo(DEST_OWNER_ID);
        assertThat(dest.getCategory()).isEqualTo("category");
        assertThat(dest.getCreationDate()).isEqualTo(NOW);
        assertThat(dest).isEqualToComparingOnlyGivenFields(orig, fieldsToIgnore);
    }

    @Test
    void make_a_partial_copy_when_origin_and_destination_are_not_semantically_the_same() {

        Cashflow orig = new InflowBuilder()
                .with(
                        o -> {
                            o.id = SOURCE_ID;
                            o.label = "myFlow";
                            o.ownerId = ORIG_OWNER_ID;
                            o.creationDate = NOW.minusMonths(1);
                            o.updateDate = NOW.plusWeeks(2);
                            o.deletionDate = NOW.plusWeeks(3);
                        })
                .build();

        UserDocument dest = new UserDocument();

        BeanUtil.copyBean(orig, dest);

        var fieldsToIgnore = new String[]{"creationDate", "updateDate", "deletionDate", "id"};
        assertThat(dest).isEqualToComparingOnlyGivenFields(orig, fieldsToIgnore);
    }


    @Test
    void should_not_map_or_throw_exception_when_origin_has_empty_nested_field() {
        GenericModelWrapper orig = new GenericModelWrapper("name", new Cashflow());
        GenericModelWrapper dest = new GenericModelWrapper();

        BeanUtil.copyBean(orig, dest);

        assertThat(orig.getName()).isEqualTo(dest.getName());
        assertThat(dest.getModel()).isNotNull();
    }

    @Test
    void should_not_map_or_throw_exception_when_origin_has_filled_nested_field() {
        final Cashflow model = new Cashflow();
        model.setCategory("category");
        model.setAmount(12);
        GenericModelWrapper orig = new GenericModelWrapper("name", model);
        GenericModelWrapper dest = new GenericModelWrapper();

        BeanUtil.copyBean(orig, dest);

        assertThat(orig.getName()).isEqualTo(dest.getName());
        assertThat(dest.getModel()).isEqualToComparingFieldByField(model);
    }


    @Test
    void should_not_map_or_throw_exception_when_origin_has_partial_filled_nested_field() {
        final Cashflow origModel = new Cashflow();
        origModel.setCategory("category");
        origModel.setAmount(12);

        final Cashflow destModel = new Cashflow();
        destModel.setAmount(8);
        destModel.setLabel("label");

        GenericModelWrapper orig = new GenericModelWrapper("name", origModel);
        GenericModelWrapper dest = new GenericModelWrapper(null, destModel);

        BeanUtil.copyBean(orig, dest);

        assertThat(orig.getName()).isEqualTo(dest.getName());
        assertThat(dest.getModel()).isNotNull();
        assertThat(dest.getModel().getCategory()).isEqualTo(origModel.getCategory());
        assertThat(dest.getModel().getAmount()).isEqualTo(origModel.getAmount());
        assertThat(dest.getModel().getLabel()).isEqualTo("label");
    }

    public static class GenericModelWrapper {
        private String name;
        private Cashflow model;

        public GenericModelWrapper() {
        }

        public GenericModelWrapper(String name, Cashflow model) {
            this.name = name;
            this.model = model;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Cashflow getModel() {
            return model;
        }

        public void setModel(Cashflow model) {
            this.model = model;
        }
    }
}