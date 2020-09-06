package com.warys.scrooge.domain.service.budget;

import com.warys.scrooge.domain.exception.ElementNotFoundException;
import com.warys.scrooge.domain.model.builder.OutflowBuilder;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.OutflowRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.OutflowDocument;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OutflowServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final String INVALID_INFLOW_ID = "invalidOutflowDocumentId";
    private static final String INFLOW_ID = "outflowId";

    private OutflowRepository outflowRepository = mock(OutflowRepository.class);
    private OutflowService tested = new OutflowService(outflowRepository);
    private Session user = mock(Session.class);

    @Test
    void throws_NullPointerException_when_null_outflowId_is_set_on_retrieve() {
        assertThrows(NullPointerException.class, () -> tested.retrieve(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_creation() {
        assertThrows(NullPointerException.class, () -> tested.create(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_outflowId_is_set_on_remove() {
        assertThrows(NullPointerException.class, () -> tested.remove(user, null));
    }


    @Test
    void throws_NullPointerException_when_null_outflowId_is_set_on_update() {
        assertThrows(NullPointerException.class, () -> tested.update(user, "", null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_update() {
        assertThrows(NullPointerException.class, () -> tested.update(user, null, new OutflowDocument()));
    }

    @Test
    void throws_NullPointerException_when_null_outflowId_is_set_on_partial_update() {
        assertThrows(NullPointerException.class, () -> tested.partialUpdate(user, "", null));
    }


    @Test
    void throws_NullPointerException_when_null_payload_is_set_for_partial_update() {
        assertThrows(NullPointerException.class, () -> tested.partialUpdate(user, null, new OutflowDocument()));
    }

    @Test
    void throws_ElementNotFoundException_when_invalid_userId_is_set_on_retrieve() {
        assertThrows(ElementNotFoundException.class, () -> tested.retrieve(user, "invalid_id"));
    }

    @Test
    void retrieve_throw_ElementNotFoundException_user_when_invalid_outflowId_is_set() {
        assertThrows(ElementNotFoundException.class, () -> {
            when(user.getId()).thenReturn(CURRENT_USER_ID);
            when(outflowRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.empty());
            tested.retrieve(user, INVALID_INFLOW_ID);
        });
    }

    @Test
    void retrieve_outflow_when_valid_outflowId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        final OutflowDocument expected = new OutflowBuilder().with(o -> o.id = "INFLOW_ID").build();
        when(outflowRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.of(expected));

        final OutflowDocument actual = tested.retrieve(user, INFLOW_ID);

        assertThat(actual).isSameAs(expected);

    }

    @Test
    void create_user_when_valid_userId_is_set() {
        final OutflowDocument expected = new OutflowBuilder().with(o -> o.id = INFLOW_ID).build();
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.insert(expected)).thenReturn(expected);

        final OutflowDocument actual = tested.create(user, expected);

        assertThat(actual.getId()).isSameAs(INFLOW_ID);
        assertThat(actual.getOwnerId()).isSameAs(CURRENT_USER_ID);
    }

    @Test
    void remove_user_when_valid_userId_is_set() throws ApiException {
        final OutflowDocument outflowToRemove = new OutflowBuilder()
                .with(o -> {
                    o.id = INFLOW_ID;
                    o.ownerId = CURRENT_USER_ID;
                }).build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(outflowToRemove));

        tested.remove(user, INFLOW_ID);

        verify(outflowRepository).delete(outflowToRemove);
    }

    @Test
    void throws_ElementNotFoundException_invalid_outflow_id_is_given() {
        assertThrows(ElementNotFoundException.class, () -> {
            when(user.getId()).thenReturn(CURRENT_USER_ID);

            tested.remove(user, "anId");
        });
    }

    @Test
    void get_all_outflow_for_user() {
        final OutflowDocument outflow = new OutflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();
        final List<OutflowDocument> expected = List.of(outflow);
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.findByOwnerId(anyString())).thenReturn(Optional.of(expected));

        final List<OutflowDocument> actual = tested.getAll(user);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void get_paged_outflow_for_user_with_valid_dates() {
        final OutflowDocument outflow = new OutflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();

        final LocalDate from = LocalDate.of(2020, 8, 23);
        final LocalDate to = LocalDate.of(2020, 9, 23);
        final List<OutflowDocument> expected = List.of(outflow);

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.findByOwnerIdAndExecutionDateGreaterThanEqualAndExecutionDateLessThanEqual(CURRENT_USER_ID, from, to)).thenReturn(Optional.of(expected));

        final List<OutflowDocument> actual = tested.getPagedData(user, from, to);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void get_paged_outflow_for_user_with_null_dates() {
        final OutflowDocument outflow = new OutflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();
        final List<OutflowDocument> expected = List.of(outflow);
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.findByOwnerIdAndExecutionDateGreaterThanEqualAndExecutionDateLessThanEqual(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Optional.of(expected));

        final List<OutflowDocument> actual = tested.getPagedData(user, null, null);

        assertThat(actual).hasSameElementsAs(expected);
    }


    @Test
    void make_a_partial_update_of_outflow() throws ApiException {
        final OutflowDocument oldOutflowDocument = new OutflowBuilder()
                .with(o -> {
                    o.id = INFLOW_ID;
                    o.label = "old name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        final OutflowDocument newOutflowDocument = new OutflowBuilder()
                .with(o -> {
                    o.label = "new name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(oldOutflowDocument));

        tested.partialUpdate(user, "anId", newOutflowDocument);

        ArgumentCaptor<OutflowDocument> argumentCaptor = ArgumentCaptor.forClass(OutflowDocument.class);
        verify(outflowRepository).save(argumentCaptor.capture());
        OutflowDocument capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument).isEqualToComparingFieldByField(
                new OutflowBuilder()
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
        final OutflowDocument expected = new OutflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();

        final OutflowDocument newOne = new OutflowBuilder().with(o -> o.label = "new a name").build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(outflowRepository.save(newOne)).thenReturn(newOne);

        OutflowDocument actual = tested.update(user, INFLOW_ID, newOne);


        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}