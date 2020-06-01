package com.warys.scrooge.domain.common.util;

import com.warys.scrooge.domain.model.budget.Cashflow;
import com.warys.scrooge.domain.model.builder.InflowBuilder;
import org.junit.jupiter.api.Test;

import static com.warys.scrooge.domain.common.util.Patcher.patch;
import static org.assertj.core.api.Assertions.assertThat;

class PatcherShould {

    @Test
    void make_a_complete_copy_of_origin_flow_when_destination_is_empty() {

        Cashflow orig = new InflowBuilder()
                .with(
                        o -> {
                            o.id = "SOURCE_ID";
                            o.label = "my flow";
                            o.amount = 15;
                        })
                .build();

        var dest = new InflowBuilder()
                .with(
                        o -> {
                            o.id = "source_id_updated";
                            o.label = "my flow updated";
                            o.amount = 10;
                            o.category = "category_updated";
                            o.ownerId = "ORIG_OWNER_ID";
                        })
                .build();

        patch(orig, dest);

        assertThat(dest.getOwnerId()).isEqualTo("ORIG_OWNER_ID");
        assertThat(dest.getId()).isEqualTo("SOURCE_ID");
        assertThat(dest.getLabel()).isEqualTo("my flow");
        assertThat(dest.getAmount()).isEqualTo(15);
        assertThat(dest.getCategory()).isEqualTo("category_updated");
    }

}