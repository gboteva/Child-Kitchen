package bg.softuni.childrenkitchen.web;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.service.interfaces.CloudinaryService;
import bg.softuni.childrenkitchen.util.TestDataUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIT {
    private static final String ADMIN_MAIL = "admin@test.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtil testDataUtil;
    @Mock
    private CloudinaryService mockCloudinaryService;


    @BeforeEach
    void setup(){
        testDataUtil.initRoles();
        testDataUtil.initPoints();
        testDataUtil.initUsers();
        testDataUtil.initAllergy();
        testDataUtil.initChild();
    }
    
    @AfterEach
    void tearDown(){
        testDataUtil.cleanUpDateBase();
    }

    @Test
    void testSuccessRegistration() throws Exception {
        mockMvc.perform(post("/users/register")
                       .param("email", "test@test.bg")
                       .param("fullName", "Test Testov")
                       .param("city", "ПЛЕВЕН")
                       .param("phoneNumber", "0888888888")
                       .param("password", "test")
                       .param("confirmPassword", "test")
                       .with(csrf())
               )
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void testNotSuccessRegistration() throws Exception {
        mockMvc.perform(post("/users/register")
                       .param("email", "")
                       .param("fullName", "")
                       .param("city", "")
                       .param("phoneNumber", "")
                       .param("password", "test")
                       .param("confirmPassword", "")
                       .with(csrf())
               )
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/users/register"))
               .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.userRegisterBindingModel"))
               ;
    }

    @Test
    void testGetLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(view().name("login"));
    }

    @Test
    void testGetRegister() throws Exception {
        mockMvc.perform(get("/users/register"))
               .andExpect(view().name("register"));
    }

    @Test
    void testNotSuccessLoginMustReturnCorrectView() throws Exception {

        mockMvc.perform(post("/users/login-error").with(csrf()))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void testPostAdminEditUserWithoutLoggedInUserMustReturnForbidden() throws Exception {
        mockMvc.perform(post("/admin/edit-user"))
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @WithAnonymousUser
    void testGetAdminEditUserWithoutLoggedInUserMustRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/edit-user"))
               .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"))
        ;
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void testGetAdminEditUserWithLoggedInAdminUserMustReturnView() throws Exception {
        mockMvc.perform(get("/admin/edit-user"))
               .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("cities"))
                .andExpect(model().attributeExists("userUpdateBindingModel"))
        ;
    }

    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    void testGetAdminEditUserWithLoggedInUserMustForbidden() throws Exception {
        mockMvc.perform(get("/admin/edit-user"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_MAIL, roles="ADMIN")
    void testPutEditUserByAdminMustRedirectWithErrors() throws Exception {

        mockMvc.perform(put("/admin/edit-user")
                       .param("email", ADMIN_MAIL)
                       .param("fullName", "")
                       .param("cityName", "")
                       .param("password", "")
                       .param("phoneNumber", "")
                       .param("servicePointName", "Детска кухня")
                       .param("makeAdmin", "true")
                       .param("makeUser", "true")
                       .with(csrf())
                    )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/edit-user"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("notPossible"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.userUpdateBindingModel"))
        ;

        Assertions.assertEquals("Admin Adminov", testDataUtil.getEntityByEmail(ADMIN_MAIL).getFullName());
    }

    @Test
    @WithMockUser(username = ADMIN_MAIL, roles="ADMIN")
    void testPutEditUserByAdminMustRedirectWithNotPossible() throws Exception {

        mockMvc.perform(put("/admin/edit-user")
                       .param("email", ADMIN_MAIL)
                       .param("fullName", "kslmklsdmldmckdcd")
                       .param("cityName", "ПЛЕВЕН")
                       .param("password", "")
                       .param("phoneNumber", "model.getPhoneNumber()")
                       .param("servicePointName", "Детска кухня")
                       .param("makeAdmin", "true")
                       .param("makeUser", "true")
                       .with(csrf())
               )
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/edit-user"))
               .andExpect(MockMvcResultMatchers.flash().attributeExists("notPossible"))
        ;

        Assertions.assertEquals("Admin Adminov", testDataUtil.getEntityByEmail(ADMIN_MAIL).getFullName());
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPutEditUserByAdminMustSuccess() throws Exception {

        mockMvc.perform(put("/admin/edit-user")
                       .param("email", "user@test.bg")
                       .param("fullName", "updatedName")
                       .param("phoneNumber", "updatedNumber")
                       .param("cityName", "ПЛЕВЕН")
                       .param("servicePointName", "Детска кухня")
                       .param("makeAdmin", "false")
                       .param("makeUser", "false")
                       .param("newEmail", "somethingNew")
                       .param("password", "").with(csrf())
               )
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/edit-user"))
               .andExpect(MockMvcResultMatchers.flash().attributeExists("successUpdate"))
               .andExpect(MockMvcResultMatchers.flash().attributeExists("updated"))
        ;

        UserEntity updated = testDataUtil.getEntityByEmail("somethingNew");

        Assertions.assertEquals("updatedName".toUpperCase(), updated.getFullName());
    }

    @Test
    @WithMockUser(username = ADMIN_MAIL, roles="ADMIN")
    void testDeleteUserMustThrowIfUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users/delete")
                       .param("email", "notExistingMail")
                       .with(csrf())
        )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("notSuccessDelete"));

        Assertions.assertEquals(2, testDataUtil.getAllEmailInDB().size());
    }

    @Test
    @WithMockUser(username = ADMIN_MAIL, roles="ADMIN")
    void testDeleteUserMustDeleteAllUserDataIfUserExist() throws Exception {



        mockMvc.perform(delete("/users/delete")
                       .param("email", ADMIN_MAIL)
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("successfullyDeleted"))
               .andExpect(MockMvcResultMatchers.flash().attribute("successfullyDeleted", ADMIN_MAIL));

        Assertions.assertEquals(1, testDataUtil.getAllEmailInDB().size());
    }

    @Test
    void testWrongLoginMustReturnErrorMassageInModel() throws Exception {
        mockMvc.perform(post("/users/login")
                       .param("email", "wrong")
                       .param("pass", "")
                .with(csrf()))
                .andExpect(forwardedUrl("/users/login-error"));
    }
}
