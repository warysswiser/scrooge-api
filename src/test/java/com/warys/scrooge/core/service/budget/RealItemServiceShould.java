package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.model.budget.RealItems;
import com.warys.scrooge.core.model.builder.RealItemsBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.RealItemRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RealItemServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final String INVALID_PLANNED_ITEMS_ID = "invalidRealItemId";
    private static final String REAL_ITEMS_ID = "realItemId";

    private RealItemRepository realItemRepository = mock(RealItemRepository.class);
    private RealItemService tested = new RealItemService(realItemRepository);
    private SessionUser user = mock(SessionUser.class);

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_plannedItemId_is_set_on_retrieve() throws ApiException {
        tested.retrieve(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_creation() {
        tested.create(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_plannedItemId_is_set_on_remove() throws ApiException {
        tested.retrieve(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_plannedItemId_is_set_on_update() {
        tested.update(user, "", null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_update() {
        tested.update(user, null, new RealItems());
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_plannedItemId_is_set_on_partial_update() throws ApiException {
        tested.partialUpdate(user, "", null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_partial_update() throws ApiException {
        tested.partialUpdate(user, null, new RealItems());
    }

    @Test(expected = ElementNotFoundException.class)
    public void throws_ElementNotFoundException_when_invalid_userId_is_set_on_retrieve() throws ApiException {
        tested.retrieve(user, "invalid_id");
    }

    @Test(expected = ElementNotFoundException.class)
    public void retrieve_throw_ElementNotFoundException_user_when_invalid_plannedItemId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(realItemRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.empty());
        tested.retrieve(user, INVALID_PLANNED_ITEMS_ID);
    }

    @Test
    public void retrieve_plannedItem_when_valid_plannedItemId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        final RealItems expected = new RealItemsBuilder().with(o -> o.id = "REAL_ITEMS_ID").build();
        when(realItemRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.of(expected));

        final RealItems actual = tested.retrieve(user, REAL_ITEMS_ID);

        assertThat(actual).isSameAs(expected);

    }

    @Test
    public void create_user_when_valid_userId_is_set() {
        final RealItems expected = new RealItemsBuilder().with(o -> o.id = REAL_ITEMS_ID).build();
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(realItemRepository.insert(expected)).thenReturn(expected);

        final RealItems actual = tested.create(user, expected);

        assertThat(actual.getId()).isSameAs(REAL_ITEMS_ID);
        assertThat(actual.getOwnerId()).isSameAs(CURRENT_USER_ID);
    }

    @Test
    public void remove_user_when_valid_userId_is_set() throws ApiException {
        final RealItems plannedItemToRemove = new RealItemsBuilder()
                .with(o -> {
                    o.id = REAL_ITEMS_ID;
                    o.ownerId = CURRENT_USER_ID;
                }).build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(realItemRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(plannedItemToRemove));

        tested.remove(user, REAL_ITEMS_ID);

        verify(realItemRepository).delete(plannedItemToRemove);
    }

    @Test(expected = ElementNotFoundException.class)
    public void throws_ElementNotFoundException_invalid_plannedItem_id_is_given() throws ApiException {

        when(user.getId()).thenReturn(CURRENT_USER_ID);

        tested.remove(user, "anId");
    }

    @Test
    public void get_all_plannedItem_for_user() {
        final List<RealItems> expected = List.of(
                new RealItemsBuilder().with(o -> {
                    o.id = REAL_ITEMS_ID;
                    o.ownerId = CURRENT_USER_ID;
                }).build());
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(realItemRepository.findByOwnerId(anyString())).thenReturn(Optional.of(expected));

        final List<RealItems> actual = tested.getAll(user);

        assertThat(actual).hasSameElementsAs(expected);
    }


    @Test
    public void make_a_partial_copy_of_plannedItem() throws ApiException {
        final RealItems oldRealItem = new RealItemsBuilder()
                .with(o -> {
                    o.id = REAL_ITEMS_ID;
                    o.ownerId = CURRENT_USER_ID;
                    o.budgetId = "old budget id";
                })
                .build();

        final RealItems newRealItem = new RealItemsBuilder()
                .with(o -> {
                    o.ownerId = CURRENT_USER_ID;
                    o.budgetId = "new budget id";
                })
                .build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(realItemRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(oldRealItem));

        tested.partialUpdate(user, "anId", newRealItem);

        ArgumentCaptor<RealItems> argumentCaptor = ArgumentCaptor.forClass(RealItems.class);
        verify(realItemRepository).save(argumentCaptor.capture());
        RealItems capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument).isEqualToComparingFieldByField(
                new RealItemsBuilder()
                        .with(o -> {
                            o.id = REAL_ITEMS_ID;
                            o.ownerId = CURRENT_USER_ID;
                            o.budgetId = "new budget id";
                        })
                        .build()
        );

    }

    @Test
    public void update_user_when_payload_without_id_is_set() {
        final RealItems expected = new RealItemsBuilder().with(o -> {
            o.id = REAL_ITEMS_ID;
            o.budgetId = "new budget id";
            o.ownerId = CURRENT_USER_ID;
        }).build();

        final RealItems newOne = new RealItemsBuilder().with(o -> o.budgetId = "new budget id").build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);

        tested.update(user, REAL_ITEMS_ID, newOne);

        ArgumentCaptor<RealItems> argumentCaptor = ArgumentCaptor.forClass(RealItems.class);
        verify(realItemRepository).save(argumentCaptor.capture());
        RealItems capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument).isEqualToComparingFieldByField(expected);
    }
}