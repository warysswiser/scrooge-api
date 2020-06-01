package com.warys.scrooge;

import com.warys.scrooge.application.rest.PublicUsersController;
import com.warys.scrooge.application.rest.secured.AttachmentsController;
import com.warys.scrooge.application.rest.secured.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ScroogeApplicationShould {

    @Autowired
    private PublicUsersController publicUsersController;
    @Autowired
    private AttachmentsController attachmentsController;
    @Autowired
    private UserController userController;

    @Test
    void load_context() {
        assertThat(publicUsersController).isNotNull();
        assertThat(attachmentsController).isNotNull();
        assertThat(userController).isNotNull();
    }

    @Test
    void launch_main() {
        ScroogeApplication.main(new String[]{});
    }

}
