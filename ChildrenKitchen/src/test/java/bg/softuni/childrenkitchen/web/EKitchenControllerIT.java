package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EKitchenControllerIT {
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
        testDataUtil.initAllergens();
        testDataUtil.initFood();
        testDataUtil.initMenus();
    }

    @AfterEach
    void cleanDB() {
        testDataUtil.cleanUpDateBase();
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetEKitchenMustReturnRightView() throws Exception {
        mockMvc.perform(get("/e-kitchen"))
               .andExpect(view().name("e-kitchen"))
                .andExpect(model().attributeExists("children"))
                .andExpect(model().attributeExists("buyCouponsBindingModel"))
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testBuyCouponsMustSuccess() throws Exception {
        mockMvc.perform(post("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("countCoupons", "10")
                       .param("price", "8,00 лв.")
                       .param("ageGroupName", AgeGroupEnum.ГОЛЕМИ.name())
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("success"))
                .andExpect(MockMvcResultMatchers.flash().attribute("countCoupons", 10))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testBuyCouponsMustReturnBindingResult() throws Exception {
        mockMvc.perform(post("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("countCoupons", "10")
                       .param("price", "8,00 лв.")
                       .param("ageGroupName", AgeGroupEnum.МАЛКИ.name())
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("noMatchAgeGroup"))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testVerifyCouponsMustReturnNoMoreOrderPerDay() throws Exception {
        LocalDate date = LocalDate.now();
        LocalDate with = date.with(DayOfWeek.FRIDAY).plusDays(5);

        mockMvc.perform(patch("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("verifyDate", with.toString())
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("noMoreOrdersPerDay"))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testVerifyCouponsSuccess() throws Exception {

        testDataUtil.buyCouponsByUser();

        mockMvc.perform(patch("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("verifyDate", LocalDate.of(2023, 12, 12).toString())
                       .with(csrf())
               )
                .andExpect(MockMvcResultMatchers.flash()
                                                .attributeExists("successVerify"))
               .andExpect(status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testVerifyCouponsMustThrowNoAvailableCoupons() throws Exception {
        mockMvc.perform(patch("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("verifyDate", LocalDate.of(2023, 12, 12).toString())
                       .with(csrf())
               )
               .andExpect(view().name("no-available-coupons"))
        ;
    }

    @Test
    @WithUserDetails(value = NOT_ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testVerifyCouponsMustReturnBindingResult() throws Exception {
        mockMvc.perform(patch("/e-kitchen")
                       .param("childName", "TestChild2")
                       .param("verifyDate", LocalDate.now().toString())
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.verifyCouponBindingModel"))
        ;
    }
}
