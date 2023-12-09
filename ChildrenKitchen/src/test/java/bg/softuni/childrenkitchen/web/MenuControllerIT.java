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
public class MenuControllerIT {
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
        testDataUtil.initFood();
        testDataUtil.initMenus();
    }

    @AfterEach
    void cleanDB() {
        testDataUtil.cleanUpDateBase();
    }

    @Test
    void testGetMenuMustReturnRightView() throws Exception {
        mockMvc.perform(get("/menus"))
                .andExpect(view().name("menus"))
                .andExpect(model().attributeExists("smallWeeklyMenu"))
                .andExpect(model().attributeExists("bigWeeklyMenu"));
    }

    @Test
    void testGetMenuMustReturnErrorView() throws Exception {
        testDataUtil.cleanUpDateBase();
        mockMvc.perform(get("/menus"))
                .andExpect(view().name("no-available-menus"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetMenuByDateMustReturnRightView() throws Exception {
        mockMvc.perform(get("/admin/view-menu-by-date"))
                .andExpect(view().name("date-menu"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAddMenuMustReturnRightView() throws Exception {
        mockMvc.perform(get("/admin/add-menu"))
               .andExpect(view().name("add-menu"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testViewMenuByDateMustReturnMenuViewModel() throws Exception {
        mockMvc.perform(post("/admin/view-menu-by-date")
                .param("date", LocalDate.now().with(DayOfWeek.MONDAY).plusDays(2).toString())
                .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .with(csrf())
        )
                .andExpect(MockMvcResultMatchers.flash()
                        .attributeExists("menuViewModel"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testViewMenuByDateMustReturnNotFound() throws Exception {
        mockMvc.perform(post("/admin/view-menu-by-date")
                       .param("date", LocalDate.now().toString())
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("notFound"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddMenuMustSuccess() throws Exception {
        mockMvc.perform(post("/admin/add-menu")
                       .param("date", LocalDate.of(2023, 12, 8).toString())
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .param("soup", "SOUP")
                       .param("main", "MAIN")
                       .param("dessert", "SWEET")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("addedNewMenu"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddMenuMustReturnBindingResult() throws Exception {
        mockMvc.perform(post("/admin/add-menu")
                       .param("date", LocalDate.now().toString())
                       .param("ageGroup", "")
                       .param("soup", "SOUP")
                       .param("main", "MAIN")
                       .param("dessert", "SWEET")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("org.springframework.validation.BindingResult.addMenuBindingModel"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testExistingMenu() throws Exception {
        mockMvc.perform(post("/admin/add-menu")
                       .param("date", LocalDate.now().with(DayOfWeek.MONDAY).plusDays(2).toString())
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .param("soup", "Supichka")
                       .param("main", "Prase")
                       .param("dessert", "Sladko")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("isExistingMenu"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testEditMenu() throws Exception {
        mockMvc.perform(patch("/admin/add-menu")
                       .param("date", LocalDate.now().with(DayOfWeek.MONDAY).plusDays(2).toString())
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .param("soup", "SOUP")
                       .param("main", "MAIN")
                       .param("dessert", "SWEET")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("addedNewMenu"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testEditMenuMustThrow() throws Exception {
        mockMvc.perform(patch("/admin/add-menu")
                       .param("date", LocalDate.now().toString())
                       .param("ageGroup", AgeGroupEnum.ГОЛЕМИ.name())
                       .param("soup", "SOUP")
                       .param("main", "MAIN")
                       .param("dessert", "SWEET")
                       .with(csrf())
               )
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("notFound"))
               .andExpect(status().is3xxRedirection());
    }


}
