package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.builder.BudgetBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.BudgetRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BudgetServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final String INVALID_BUDGET_ID = "invalidBudgetId";
    private static final String BUDGET_ID = "budgetId";

    private BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private BudgetService tested = new BudgetService(budgetRepository);
    private SessionUser user = mock(SessionUser.class);

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_budgetId_is_set_on_retrieve() throws ApiException {
        tested.retrieve(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_creation() {
        tested.create(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_budgetId_is_set_on_remove() throws ApiException {
        tested.retrieve(user, null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_budgetId_is_set_on_update() {
        tested.update(user, "", null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_update() {
        tested.update(user, null, new Budget());
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_budgetId_is_set_on_partial_update() throws ApiException {
        tested.partialUpdate(user, "", null);
    }


    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_payload_is_set_for_partial_update() throws ApiException {
        tested.partialUpdate(user, null, new Budget());
    }

    @Test(expected = ElementNotFoundException.class)
    public void throws_ElementNotFoundException_when_invalid_userId_is_set_on_retrieve() throws ApiException {
        tested.retrieve(user, "invalid_id");
    }

    @Test(expected = ElementNotFoundException.class)
    public void retrieve_throw_ElementNotFoundException_user_when_invalid_budgetId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.empty());
        tested.retrieve(user, INVALID_BUDGET_ID);
    }

    @Test
    public void retrieve_budget_when_valid_budgetId_is_set() throws ApiException {
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        final Budget expected = new BudgetBuilder().with(o -> o.id = "BUDGET_ID").build();
        when(budgetRepository.findByIdAndOwnerId(any(String.class), any(String.class))).thenReturn(Optional.of(expected));

        final Budget actual = tested.retrieve(user, BUDGET_ID);

        assertThat(actual).isSameAs(expected);

    }

    @Test
    public void create_user_when_valid_userId_is_set() {
        final Budget expected = new BudgetBuilder().with(o -> o.id = BUDGET_ID).build();
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.insert(expected)).thenReturn(expected);

        final Budget actual = tested.create(user, expected);

        assertThat(actual.getId()).isSameAs(BUDGET_ID);
        assertThat(actual.getOwnerId()).isSameAs(CURRENT_USER_ID);
    }

    @Test
    public void remove_user_when_valid_userId_is_set() throws ApiException {
        final Budget budgetToRemove = new BudgetBuilder()
                .with(o -> {
                    o.id = BUDGET_ID;
                    o.ownerId = CURRENT_USER_ID;
                }).build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(budgetToRemove));

        tested.remove(user, BUDGET_ID);

        verify(budgetRepository).delete(budgetToRemove);
    }

    @Test(expected = ElementNotFoundException.class)
    public void throws_ElementNotFoundException_invalid_budget_id_is_given() throws ApiException {

        when(user.getId()).thenReturn(CURRENT_USER_ID);

        tested.remove(user, "anId");
    }

    @Test
    public void get_all_budget_for_user() {
        final List<Budget> expected = List.of(
                new BudgetBuilder().with(o -> {
                    o.id = BUDGET_ID;
                    o.name = "new a name";
                    o.ownerId = CURRENT_USER_ID;
                }).build());
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.findByOwnerId(anyString())).thenReturn(Optional.of(expected));

        final List<Budget> actual = tested.getAll(user);

        assertThat(actual).hasSameElementsAs(expected);
    }


    @Test
    public void make_a_partial_update_of_budget() throws ApiException {
        final Budget oldBudget = new BudgetBuilder()
                .with(o -> {
                    o.id = BUDGET_ID;
                    o.name = "old name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        final Budget newBudget = new BudgetBuilder()
                .with(o -> {
                    o.name = "new name";
                    o.ownerId = CURRENT_USER_ID;
                })
                .build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.findByIdAndOwnerId(anyString(), anyString())).thenReturn(Optional.of(oldBudget));

        tested.partialUpdate(user, "anId", newBudget);

        ArgumentCaptor<Budget> argumentCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(argumentCaptor.capture());
        Budget capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument).isEqualToComparingFieldByField(
                new BudgetBuilder()
                        .with(o -> {
                            o.id = BUDGET_ID;
                            o.name = "new name";
                            o.ownerId = CURRENT_USER_ID;
                        })
                        .build()
        );

    }

    @Test
    public void update_user_when_payload_without_id_is_set() {
        final Budget expected = new BudgetBuilder().with(o -> {
            o.id = BUDGET_ID;
            o.name = "new a name";
            o.ownerId = CURRENT_USER_ID;
        }).build();

        final Budget newOne = new BudgetBuilder().with(o -> o.name = "new a name").build();

        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(budgetRepository.save(newOne)).thenReturn(newOne);

        Budget actual = tested.update(user, BUDGET_ID, newOne);


        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}