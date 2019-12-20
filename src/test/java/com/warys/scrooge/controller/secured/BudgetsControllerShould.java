package com.warys.scrooge.controller.secured;


import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.budget.BudgetLine;
import com.warys.scrooge.core.model.builder.BudgetBuilder;
import com.warys.scrooge.core.model.builder.BudgetLineBuilder;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.BudgetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class BudgetsControllerShould extends SecuredTest {

    private static final String RESOURCE = "/me/budgets";

    private static final String BUDGET_ID = "budgetId";
    private static final String SECOND_LINE_VALID_ID = "SECOND_LINE_VALID_ID";
    private static final String FIRST_LINE_VALID_ID = "FIRST_LINE_VALID_ID";
    private static final String FIRST_LINE_LABEL = "First line";
    private static final String SECOND_LINE_LABEL = "Second line";
    public static final String BUDGET_NAME = "new a name";

    @MockBean
    private BudgetRepository budgetRepository;

    @Before
    public void setUp() {
        super.init();

        User user = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = EMAIL;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        when(userRepository.insert(any(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        final BudgetLine firstLine = new BudgetLineBuilder().with(l -> {
            l.label = FIRST_LINE_LABEL;
            l.budgetId = BUDGET_ID;
            l.id = FIRST_LINE_VALID_ID;
        }).build();
        final BudgetLine secondLine = new BudgetLineBuilder().with(l -> {
            l.label = SECOND_LINE_LABEL;
            l.budgetId = BUDGET_ID;
            l.id = SECOND_LINE_VALID_ID;
        }).build();

        final Budget expected = new BudgetBuilder().with(o -> {
            o.id = BUDGET_ID;
            o.name = BUDGET_NAME;
            o.ownerId = USER_ID;
            o.budgetLines = new ArrayList<>(asList(firstLine, secondLine));
        }).build();

        when(budgetRepository.findByOwnerId(USER_ID)).thenReturn(Optional.of(List.of(expected)));
    }

    @Test
    public void get_my_budgets_list() throws Exception {
        this.mockMvc.perform(
                get(RESOURCE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(BUDGET_ID)))
                .andExpect(jsonPath("$[0].name", is(BUDGET_NAME)))
                .andExpect(jsonPath("$[0].ownerId", is(USER_ID)))
                .andExpect(jsonPath("$[0].budgetLines.[0].id", is(FIRST_LINE_VALID_ID)))
                .andExpect(jsonPath("$[0].budgetLines.[0].label", is(FIRST_LINE_LABEL)))
                .andExpect(jsonPath("$[0].budgetLines[0].budgetId", is(BUDGET_ID)))
                .andExpect(jsonPath("$[0].budgetLines.[1].id", is(SECOND_LINE_VALID_ID)))
                .andExpect(jsonPath("$[0].budgetLines.[1].label", is(SECOND_LINE_LABEL)))
                .andExpect(jsonPath("$[0].budgetLines[1].budgetId", is(BUDGET_ID)));
    }

}