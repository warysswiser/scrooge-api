package com.warys.scrooge.domain.service.budget;

import com.warys.scrooge.domain.exception.ElementNotFoundException;
import com.warys.scrooge.domain.model.builder.InflowBuilder;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.InflowRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InflowServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final String INVALID_INFLOW_ID = "invalidInflowDocumentId";
    private static final String INFLOW_ID = "inflowId";

    private InflowRepository inflowRepository = mock(InflowRepository.class);
    private InflowService tested = new InflowService(inflowRepository);
    private Session user = mock(Session.class);

    @Test
    void throws_NullPointerException_when_null_inflowId_is_set_on_retrieve() {
        assertThrows(NullPointerException.class, () -> tested.retrieve(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_creation() {
        assertThrows(NullPointerException.class, () -> tested.create(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_inflowId_is_set_on_remove() {
        assertThrows(NullPointerException.class, () -> tested.remove(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_inflowId_is_set_on_update() {
        assertThrows(NullPointerException.class, () -> tested.update(user, "", null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_update() {
        assertThrows(NullPointerException.class, () -> tested.update(user, null, new InflowDocument()));
    }

    @Test
    void throws_NullPointerException_when_null_inflowId_is_set_on_partial_update() {
        assertThrows(NullPointerException.class, () -> tested.partialUpdate(user, "", null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_partial_update() {
        assertThrows(NullPointerException.class, () -> tested.partialUpdate(user, null, new InflowDocument()));
    }

    @Test
    void throws_ElementNotFoundException_when_invalid_userId_is_set_on_retrieve() {
        assertThrows(ElementNotFoundException.class, () -> tested.retrieve(user, "invalid_id"));
    }

    @Test
    void retrieve_throw_ElementNotFoundException_user_when_invalid_inflowId_is_set() {
        assertThrows(ElementNotFoundException.class, () -> {
            when(user.getId()).thenReturn(CURRENT_USER_ID);
            when(inflowRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.empty());
            tested.retrieve(user, INVALID_INFLOW_ID);
        });
    }

    @Test
    void retrieve_inflow_when_valid_inflowId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        final InflowDocument expected = new InflowBuilder().with(o -> o.id = "INFLOW_ID").build();
        when(inflowRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.of(expected));

        final InflowDocument actual = tested.retrieve(user, INFLOW_ID);

        assertThat(actual).isSameAs(expected);

    }

    @Test
    void create_user_when_valid_userId_is_set() {
        final InflowDocument expected = new InflowBuilder().with(o -> o.id = INFLOW_ID).build();
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(inflowRepository.insert(expected)).thenReturn(expected);

        final InflowDocument actual = tested.create(user, expected);

        assertThat(actual.getId()).isSameAs(INFLOW_ID);
        assertThat(actual.getOwnerId()).isSameAs(CURRENT_USER_ID);
    }

    @Test
    void remove_user_when_valid_userId_is_set() throws ApiException {
        final InflowDocument inflowToRemove = new InflowBuilder()
                .with(o -> {
                    o.id = INFLOW_ID;
                    o.ownerId = CURRENT_USER_ID;
                }).build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(inflowRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(inflowToRemove));

        tested.remove(user, INFLOW_ID);

        verify(inflowRepository).delete(inflowToRemove);
    }

    @Test
    void throws_ElementNotFoundException_invalid_inflow_id_is_given() {
        assertThrows(ElementNotFoundException.class, () -> {
            when(user.getId()).thenReturn(CURRENT_USER_ID);

            tested.remove(user, "anId");
        });
    }

    @Test
    void get_all_inflow_for_user() {
        final InflowDocument inflow = new InflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();
        final List<InflowDocument> expected = List.of(inflow);
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(inflowRepository.findByOwnerId(anyString())).thenReturn(Optional.of(expected));

        final List<InflowDocument> actual = tested.getAll(user);

        assertThat(actual).hasSameElementsAs(expected);
    }


    @Test
    void make_a_partial_update_of_inflow() throws ApiException {
        final InflowDocument oldInflowDocument = new InflowBuilder()
                .with(o -> {
                    o.id = INFLOW_ID;
                    o.label = "old name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        final InflowDocument newInflowDocument = new InflowBuilder()
                .with(o -> {
                    o.label = "new name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(inflowRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(oldInflowDocument));

        tested.partialUpdate(user, "anId", newInflowDocument);

        ArgumentCaptor<InflowDocument> argumentCaptor = ArgumentCaptor.forClass(InflowDocument.class);
        verify(inflowRepository).save(argumentCaptor.capture());
        InflowDocument capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument).isEqualToComparingFieldByField(
                new InflowBuilder()
                        .with(o -> {
                            o.id = INFLOW_ID;
                            o.label = "new name";
                            o.ownerId = CURRENT_USER_ID;
                        })
                        .build()
        );

    }

    @Test
    void update_user_when_payload_without_id_is_set() {
        final InflowDocument expected = new InflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();

        final InflowDocument newOne = new InflowBuilder().with(o -> o.label = "new a name").build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(inflowRepository.save(newOne)).thenReturn(newOne);

        InflowDocument actual = tested.update(user, INFLOW_ID, newOne);


        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}