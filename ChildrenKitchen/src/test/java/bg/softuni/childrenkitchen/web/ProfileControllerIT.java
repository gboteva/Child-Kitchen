package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CloudinaryImage;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.service.interfaces.CloudinaryService;
import bg.softuni.childrenkitchen.util.TestDataUtil;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileControllerIT {
    private static final String ADMIN_MAIL = "admin@test.com";
    private static final String NOT_ADMIN_MAIL = "user@test.bg";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtil testDataUtil;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private Integer mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    private GreenMail greenMail;

    @Mock
    private  CloudinaryService mockCloudinaryService;

    @BeforeEach
    void setup() {
        testDataUtil.initRoles();
        testDataUtil.initPoints();
        testDataUtil.initUsers();
        testDataUtil.initAllergy();
        testDataUtil.initChild();

        greenMail = new GreenMail(new ServerSetup(mailPort, mailHost, "smtp"));
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }


    @AfterEach
    void tearDown() {
        testDataUtil.cleanUpDateBase();
        greenMail.stop();
    }

    @Test
    @WithAnonymousUser
    void testGetProfileMustRedirectToLoginWithAnonymous() throws Exception {
        mockMvc.perform(get("/users/profile")
               )
               .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetProfileModelMustContainAttr() throws Exception {
        mockMvc.perform(get("/users/profile"))
               .andExpect(view().name("profile"))
               .andExpect(model().attributeExists("lastOrders"))
               .andExpect(model().attributeExists("loggedInUser"))
               .andExpect(model().attributeExists("points"))
               .andExpect(model().attributeExists("cities"))
               .andExpect(status().is2xxSuccessful())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testEditProfileMustReturnBindingResult() throws Exception {
        mockMvc.perform(patch("/users/profile")
                       .param("email", "something")
                       .param("fullName", "")
                       .param("cityName", "")
                       .param("password", "")
                       .param("phoneNumber", "")
                       .param("servicePointName", "Детска кухня")
                       .param("makeAdmin", "true")
                       .param("makeUser", "true")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("org.springframework.validation.BindingResult.userUpdateBindingModel"))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testEditProfileMustSuccess() throws Exception {

        mockMvc.perform(patch("/users/profile")
                       .param("email", NOT_ADMIN_MAIL)
                       .param("fullName", "updatedName")
                       .param("phoneNumber", "updatedNumber")
                       .param("cityName", "ПЛЕВЕН")
                       .param("servicePointName", "Детска кухня")
                       .param("makeAdmin", "false")
                       .param("makeUser", "false")
                       .param("newEmail", "somethingNew")
                       .param("password", "")
                       .with(csrf())
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
               )
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/users/profile"))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("successEdit"))
        ;

        UserEntity updated = testDataUtil.getEntityByEmail(NOT_ADMIN_MAIL);

        Assertions.assertEquals("updatedName".toUpperCase(), updated.getFullName());
    }

    @Test
    @WithMockUser(username = "mocked@test.bg", password = "", authorities = {})
    void testGetAddKiddMustReturnRightView() throws Exception {
        mockMvc.perform(get("/users/profile/add-kid"))
               .andExpect(view().name("add-kid"));
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddKiddMustReturnBindingResult() throws Exception {
        MockMultipartFile birthCertificate = new MockMultipartFile("birthCertificate", "dummy.jpeg",
                MediaType.APPLICATION_FORM_URLENCODED_VALUE, Files.readAllBytes(Path.of("C:\\SOFTUNI\\DK_PROJECT_RESOURCES\\Backend\\ChildrenKitchen\\src\\main\\resources\\static\\images\\backgrounds\\Birth-Certificate.jpg")));

        MockMultipartFile medicalList = new MockMultipartFile("medicalList", "dummy2.jpeg",
                MediaType.APPLICATION_FORM_URLENCODED_VALUE, Files.readAllBytes(Path.of("C:\\SOFTUNI\\DK_PROJECT_RESOURCES\\Backend\\ChildrenKitchen\\src\\main\\resources\\static\\images\\backgrounds\\medicalList.jpg")));



        mockMvc.perform(
                       MockMvcRequestBuilders.multipart("/users/profile/add-kid")
                                             .file(birthCertificate)
                                             .file(medicalList)
                                             .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .param("fullName", "")
                                             .param("allergy", "")
                                             .param("isChecked", "false")
                                             .param("birthDate", LocalDate.of(2022, 10, 4)
                                                                          .toString())
                                             .param("id", "3")
                                             .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("org.springframework.validation.BindingResult.childRegisterBindingModel"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("add-kid"))
        ;
    }


    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddKiddMustSuccess() throws Exception {

        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .build();


        MockMultipartFile birthCertificate = new MockMultipartFile("birthCertificate", "dummy.jpeg",
                MediaType.APPLICATION_FORM_URLENCODED_VALUE, Files.readAllBytes(Path.of("C:\\SOFTUNI\\DK_PROJECT_RESOURCES\\Backend\\ChildrenKitchen\\src\\main\\resources\\static\\images\\backgrounds\\Birth-Certificate.jpg")));

        MockMultipartFile medicalList = new MockMultipartFile("medicalList", "dummy2.jpeg",
                MediaType.APPLICATION_FORM_URLENCODED_VALUE, Files.readAllBytes(Path.of("C:\\SOFTUNI\\DK_PROJECT_RESOURCES\\Backend\\ChildrenKitchen\\src\\main\\resources\\static\\images\\backgrounds\\medicalList.jpg")));

        CloudinaryImage certImg = new CloudinaryImage();
        certImg.setUrl("URL");
        certImg.setPublicKey("Key");


        when(mockCloudinaryService.uploadImage(medicalList))
                .thenReturn(certImg);

        when(mockCloudinaryService.uploadImage(birthCertificate))
                .thenReturn(certImg);

        mockMvc.perform(
                       MockMvcRequestBuilders.multipart("/users/profile/add-kid")
                                             .file(birthCertificate)
                                             .file(medicalList)
                                             .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .param("fullName", "kidddrrrrrrrrr")
                                             .param("allergy", "НЯМА")
                                             .param("isChecked", "true")
                                             .param("birthDate", LocalDate.of(2022, 10, 4)
                                                                          .toString())
                                             .param("id", "3")
               )
               .andExpect(redirectedUrl("/users/profile"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, receivedMessages.length);

        MimeMessage message = receivedMessages[0];
        Assertions.assertTrue(
                message.getContent()
                       .toString()
                       .contains("ново дете"));
    }


}
