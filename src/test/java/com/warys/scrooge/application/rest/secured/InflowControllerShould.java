package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.domain.model.builder.InflowBuilder;
import com.warys.scrooge.infrastructure.repository.mongo.InflowRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class InflowControllerShould extends SecuredTest {

    private static final String RESOURCE = "/me/inflows";
    static final String INFLOW_ID = "VALID_ID";

    @MockBean
    private InflowRepository inflowRepository;
    private InflowDocument INFLOW;


    @BeforeEach
    void setUp() {
        init();
        INFLOW = new InflowBuilder().with(o -> {
            o.id = INFLOW_ID;
            o.label = "Salary";
            o.ownerId = USER_ID;
            o.category = "NONE";
            o.amount = 1500;
            o.frequency = "monthly";
        }).build();


        when(inflowRepository.findByOwnerId(USER_ID)).thenReturn(Optional.of(List.of(INFLOW)));
        when(inflowRepository.findByIdAndOwnerId(INFLOW_ID, USER_ID)).thenReturn(Optional.of(INFLOW));

        when(inflowRepository.insert(any(InflowDocument.class))).thenAnswer(i -> i.getArguments()[0]);
        when(inflowRepository.save(any(InflowDocument.class))).thenAnswer(i -> i.getArguments()[0]);
    }


    @Test
    void get_all_inflow() throws Exception {
        this.mockMvc.perform(
                get(RESOURCE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(INFLOW.getId())))
                .andExpect(jsonPath("$[0].ownerId", is(INFLOW.getOwnerId())))
                .andExpect(jsonPath("$[0].category", is(INFLOW.getCategory())))
                .andExpect(jsonPath("$[0].label", is(INFLOW.getLabel())))
                .andExpect(jsonPath("$[0].amount", is(INFLOW.getAmount())))
                .andExpect(jsonPath("$[0].frequency", is(INFLOW.getFrequency())));
    }

    @Test
    void get_inflow() throws Exception {
        this.mockMvc.perform(
                get(RESOURCE + "/" + INFLOW_ID)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(INFLOW.getId())))
                .andExpect(jsonPath("$.ownerId", is(INFLOW.getOwnerId())))
                .andExpect(jsonPath("$.category", is(INFLOW.getCategory())))
                .andExpect(jsonPath("$.label", is(INFLOW.getLabel())))
                .andExpect(jsonPath("$.amount", is(INFLOW.getAmount())))
                .andExpect(jsonPath("$.frequency", is(INFLOW.getFrequency())));
    }

    @Test
    void remove_inflow() throws Exception {
        this.mockMvc.perform(
                delete(RESOURCE.concat("/").concat(INFLOW_ID))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void get_404_when_invalid_id_is_given() throws Exception {
        this.mockMvc.perform(
                get(RESOURCE + "/" + "INVALID_INFLOW_ID")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void create_inflow() throws Exception {
        this.mockMvc.perform(
                post(RESOURCE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(INFLOW))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(INFLOW.getId())))
                .andExpect(jsonPath("$.ownerId", is(INFLOW.getOwnerId())))
                .andExpect(jsonPath("$.category", is(INFLOW.getCategory())))
                .andExpect(jsonPath("$.label", is(INFLOW.getLabel())))
                .andExpect(jsonPath("$.amount", is(INFLOW.getAmount())))
                .andExpect(jsonPath("$.frequency", is(INFLOW.getFrequency())));
    }


    @Test
    void make_partial_update_inflow() throws Exception {
        InflowDocument inflowToUpdate = new InflowBuilder().with(o -> {
            o.label = "new salary";
            o.amount = 1800;
        }).build();

        this.mockMvc.perform(
                put(RESOURCE.concat("/").concat(INFLOW_ID))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(inflowToUpdate))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(INFLOW.getId())))
                .andExpect(jsonPath("$.ownerId", is(INFLOW.getOwnerId())))
                .andExpect(jsonPath("$.label", is(inflowToUpdate.getLabel())))
                .andExpect(jsonPath("$.amount", is(inflowToUpdate.getAmount())));
    }

    @Test
    void update_inflow() throws Exception {
        InflowDocument inflowToUpdate = new InflowBuilder().with(o -> {
            o.label = "new salary";
            o.amount = 1800;
        }).build();

        this.mockMvc.perform(
                patch(RESOURCE.concat("/").concat(INFLOW_ID))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(inflowToUpdate))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(INFLOW.getId())))
                .andExpect(jsonPath("$.ownerId", is(INFLOW.getOwnerId())))
                .andExpect(jsonPath("$.category", is(INFLOW.getCategory())))
                .andExpect(jsonPath("$.label", is(inflowToUpdate.getLabel())))
                .andExpect(jsonPath("$.amount", is(inflowToUpdate.getAmount())))
                .andExpect(jsonPath("$.frequency", is(INFLOW.getFrequency())));
    }

    @Test
    void throw_HttpRequestMethodNotSupportedException_when_call_on_invalid_uri() throws Exception {
        this.mockMvc.perform(
                post(RESOURCE + "/" + "INVALID_URI")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(INFLOW))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isMethodNotAllowed());
    }
}