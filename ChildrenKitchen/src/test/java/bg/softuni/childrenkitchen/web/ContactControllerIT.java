package bg.softuni.childrenkitchen.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContactControllerIT {
    private static final String NOT_ADMIN_MAIL = "user@test.bg";

    @Autowired
    private MockMvc mockMvc;

    private GreenMail greenMail;
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private Integer mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    @BeforeEach
    void setup(){
        greenMail = new GreenMail(new ServerSetup(mailPort, mailHost, "smtp"));
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    void closeSMTP(){
        greenMail.stop();
    }

    @Test
    void testGetContactMustReturnRightView() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(view().name("contacts"));
    }

    @Test
    void testGetThanksMustReturnRightView() throws Exception {
        mockMvc.perform(get("/thanks-feedback"))
               .andExpect(view().name("thanks-feedback"));
    }

    @Test
    void testGetAboutMustReturnRightView() throws Exception {
        mockMvc.perform(get("/about-us"))
               .andExpect(view().name("about-us"));
    }

    @Test
    void testSendMailIsSuccess() throws Exception {

        mockMvc.perform(post("/contacts")
                .param("sender", "test@abv.bg")
                .param("subject", "test")
                .param("content", "bg-alert test test")
                       .with(csrf())
        )
                .andExpect(redirectedUrl("/thanks-feedback"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, receivedMessages.length);

        MimeMessage message = receivedMessages[0];
        Assertions.assertTrue(
                message.getContent()
                       .toString()
                       .contains("bg-alert"));
    }

    @Test
    void testSendMailMustReturnBindingResult() throws Exception {

        mockMvc.perform(post("/contacts")
                       .param("sender", "test@abv.bg")
                       .param("subject", "test")
                       .param("content", "")
                       .with(csrf())
               )
               .andExpect(redirectedUrl("/contacts"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.contactBindingModel"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assertions.assertEquals(0, receivedMessages.length);

    }

}
