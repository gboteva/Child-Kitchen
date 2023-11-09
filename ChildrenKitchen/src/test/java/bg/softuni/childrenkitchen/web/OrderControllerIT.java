package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.util.TestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerIT {
    private static final String ADMIN_MAIL = "admin@test.com";
    private static final String NOT_ADMIN_MAIL = "user@test.bg";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtil testDataUtil;

    @BeforeEach
    public void setup() {
        testDataUtil.initRoles();
        testDataUtil.initPoints();
        testDataUtil.initUsers();
        testDataUtil.initAllergy();
        testDataUtil.initChild();
        testDataUtil.initCoupons();
        testDataUtil.initOrders();
    }

    @AfterEach
    void cleanDB() {
        testDataUtil.cleanUpDateBase();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetWithAdminUserMustReturnRightView() throws Exception {
        mockMvc.perform(get("/admin/add-delete-order")
               )
               .andExpect(view().name("add-delete-order"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostWithAdminUserMustSuccessAddedOrder() throws Exception {

        testDataUtil.buyCouponsByUser();

        mockMvc.perform(post("/admin/add-delete-order")
                       .param("date", LocalDate.now().plusDays(2)
                                               .toString())
                       .param("userEmail", NOT_ADMIN_MAIL)
                       .param("childFullName", "TestChild2")
                       .param("servicePoint", "9-ти квартал")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists(("date")))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attribute("childName", "TestChild2"))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("successAdded"))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddOrderByAdminWithBindingResult() throws Exception {
        mockMvc.perform(post("/admin/add-delete-order")
                .param("date", LocalDate.now().minusDays(1)
                                        .toString())
                .param("userEmail", NOT_ADMIN_MAIL)
                .param("childFullName", "TestChild2")
                .param("servicePoint", "9-ти квартал")
                .with(csrf())
        )
               .andExpect(MockMvcResultMatchers.flash()
                       .attributeExists("org.springframework.validation.BindingResult.addOrderBindingModel"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testDeleteMustSuccess() throws Exception {

        mockMvc.perform(delete("/admin/add-delete-order")
                       .param("deleteOrderDate", LocalDate.now().with(DayOfWeek.FRIDAY).plusDays(5).toString())
                .param("childName", "TestChild2")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash()
                        .attributeExists("successDelete"));

    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testDeleteMustThrowWhenChildHaveNotOrder() throws Exception {

        mockMvc.perform(delete("/admin/add-delete-order")
                       .param("deleteOrderDate", LocalDate.now().plusDays(2).toString())
                       .param("childName", "TestChild")
                       .with(csrf())
               )
               .andExpect(view().name("error-not-found"));

    }


}
