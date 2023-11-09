package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.util.TestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminReferenceControllerIT {
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
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAdminPageMustReturnRightView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("allergicKids"))
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnBindingResult() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                       .param("fromDate", LocalDate.now().toString())
                       .param("toDate", LocalDate.now().plusDays(30).toString())
                       .param("servicePoint", "" )
                       .param("ageGroup", "")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.adminSearchBindingModel"))
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnErrorIfDatesIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().minusDays(30).toString())
                                              .param("servicePoint", "something" )
                                              .param("ageGroup", "small")
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMsg"))
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnErrorIfDatesIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("servicePoint", "something" )
                                              .param("ageGroup", "small")
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMsg"))
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnValidResultForAllPointsAndGroups() throws Exception {

        testDataUtil.populateOrdersToTestAdminStatistic();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().plusDays(30).toString())
                                              .param("servicePoint", "All" )
                                              .param("ageGroup", "All")
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("referenceAllPointsViewModelList"))
               .andExpect(MockMvcResultMatchers.flash().attribute("smallSum", 1))
               .andExpect(MockMvcResultMatchers.flash().attribute("bigSum", 2))
               .andExpect(MockMvcResultMatchers.flash().attribute("allergicSum", 1))

               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnValidResultForOnePointAndAllGroups() throws Exception {

        testDataUtil.populateOrdersToTestAdminStatistic();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().plusDays(30).toString())
                                              .param("servicePoint", "ДЯ Мир" )
                                              .param("ageGroup", "All")
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("referenceByPointsViewModelList"))
               .andExpect(MockMvcResultMatchers.flash().attribute("totalCountOrders", 1))
               .andExpect(MockMvcResultMatchers.flash().attribute("totalCountAllOrders", 1))
               .andExpect(MockMvcResultMatchers.flash().attributeExists  ("allergicChildren"))

               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnValidResultForOnePointAndOneGroups() throws Exception {

        testDataUtil.populateOrdersToTestAdminStatistic();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().plusDays(30).toString())
                                              .param("servicePoint", "ДЯ Мир" )
                                              .param("ageGroup", AgeGroupEnum.МАЛКИ.name())
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("referenceByPointsViewModelList"))
               .andExpect(MockMvcResultMatchers.flash().attribute("totalCountOrders", 1))
               .andExpect(MockMvcResultMatchers.flash().attribute("totalCountAllOrders", 1))
               .andExpect(MockMvcResultMatchers.flash().attributeExists  ("allergicChildren"))

               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnValidResultForAllPointAndOneGroups() throws Exception {

        testDataUtil.populateOrdersToTestAdminStatistic();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().plusDays(30).toString())
                                              .param("servicePoint", "All" )
                                              .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("referenceAllPointsViewModelList"))
               .andExpect(MockMvcResultMatchers.flash().attribute("smallSum", 0))
               .andExpect(MockMvcResultMatchers.flash().attribute("bigSum", 2))
               .andExpect(MockMvcResultMatchers.flash().attribute ("allergicSum", 0))

               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testPostAdminPageReturnValidResultForAllPointAndAllGroupsWithMoreAllergicChildren() throws Exception {

        testDataUtil.populateOrdersToTestAdminStatistic();
        testDataUtil.populateMoreAllergicChildToTestAdminStatistic();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                                              .param("fromDate", LocalDate.now().toString())
                                              .param("toDate", LocalDate.now().plusDays(30).toString())
                                              .param("servicePoint", "All" )
                                              .param("ageGroup", "All")
                                              .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash().attributeExists("referenceAllPointsViewModelList"))
               .andExpect(MockMvcResultMatchers.flash().attribute("smallSum", 1))
               .andExpect(MockMvcResultMatchers.flash().attribute("bigSum", 3))
               .andExpect(MockMvcResultMatchers.flash().attribute ("allergicSum", 2))

               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }
}
