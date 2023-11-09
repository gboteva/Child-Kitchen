package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.util.TestDataUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.contains;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommonRestControllerIT {
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
    void testGetAllPointsMustReturnValidCountOfPoints() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/points"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetUserByKeyWordMustReturnValidCountOfUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/add-delete-order/search")
                       .param("addApplicantName", "user")
                       .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].userEmail", is(NOT_ADMIN_MAIL)))
        ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetUserByKeyWordMustReturnNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/add-delete-order/search")
                                              .param("addApplicantName", "nonexistinguser")
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAllOrdersOfChildMustReturnOneOrder() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expected = formatter.format(LocalDate.now().with(DayOfWeek.FRIDAY).plusDays(5));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/add-delete-order/allOrders")
                                              .param("childName", "TestChild2")
                                              .param("userEmail", NOT_ADMIN_MAIL)
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.length()", is(1)))
               .andExpect(jsonPath("$[0]", is(expected)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAllOrdersOfChildMustReturnNothing() throws Exception {
        testDataUtil.populateOrdersToTestAdminStatistic();
        testDataUtil.populateOneMoreChild();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/add-delete-order/allOrders")
                                              .param("childName", "Gosho")
                                              .param("userEmail", "petko@abv.bg")
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetFoodMustReturnValidCountOfFoods() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-foods")
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.length()", is(6)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetFoodMustReturnValidSoup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-foods")
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.soups.[1]", is("Supichka")));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetMenuByDateMustReturnValidSoup() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String menuForDate = formatter.format(LocalDate.now().with(DayOfWeek.MONDAY).plusDays(2));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-menu-by-date")
                       .param("date", menuForDate)
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                                              .with(csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.soup.name", is("Supichka")));
    }
}
