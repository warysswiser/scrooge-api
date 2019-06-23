package com.warys.scrooge;

import com.warys.scrooge.controller.PublicUsersController;
import com.warys.scrooge.controller.secured.AttachmentsController;
import com.warys.scrooge.controller.secured.BudgetsController;
import com.warys.scrooge.controller.secured.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScroogeApplicationShould {

    @Autowired
    private PublicUsersController publicUsersController;
    @Autowired
    private AttachmentsController attachmentsController;
    @Autowired
    private BudgetsController budgetsController;
    @Autowired
    private UserController userController;

    @Test
    public void load_context() {
        assertThat(publicUsersController).isNotNull();
        assertThat(attachmentsController).isNotNull();
        assertThat(budgetsController).isNotNull();
        assertThat(userController).isNotNull();
    }

    @Test
    public void launch_main() {
        ScroogeApplication.main(new String[]{});
    }

}
