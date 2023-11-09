package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
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

import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodControllerIt {
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
    void testGetHealthyFoodMustReturnRightView() throws Exception {
        mockMvc.perform(get("/healthy-food"))
               .andExpect(view().name("healthy"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddReceiptReturnRightView() throws Exception {
        mockMvc.perform(get("/admin/add-recipe"))
               .andExpect(view().name("add-recipe"))
                .andExpect(model().attributeExists("foodCategories"))
                .andExpect(model().attributeExists("ageGroups"));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddReceiptMustSuccess() throws Exception {

        mockMvc.perform(post("/admin/add-recipe")
                .param("ageGroup", AgeGroupEnum.МАЛКИ.name())
                .param("foodCategory", FoodCategoryEnum.ДЕСЕРТ.name())
                .param("foodName", "Крем карамел")
                .param("allergens", String.join(", ", List.of(AllergensEnum.ПРЯСНО_МЛЯКО.name())))
                .param("otherAllergen", "")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.flash()
                        .attributeExists("success"))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddReceiptMustSuccessWithOtherAllergens() throws Exception {

        mockMvc.perform(post("/admin/add-recipe")
                       .param("ageGroup", AgeGroupEnum.МАЛКИ.name())
                       .param("foodCategory", FoodCategoryEnum.ДЕСЕРТ.name())
                       .param("foodName", "Крем карамел")
                       .param("allergens", String.join(", ", List.of(AllergensEnum.ПРЯСНО_МЛЯКО.name())))
                       .param("otherAllergen", "Друг алерген, Още един")
                       .with(csrf()))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("success"))
               .andExpect(status().is3xxRedirection());

    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddReceiptMustReturnBindingResult() throws Exception {

        mockMvc.perform(post("/admin/add-recipe")
                       .param("ageGroup", AgeGroupEnum.МАЛКИ.name())
                       .param("foodCategory", FoodCategoryEnum.ДЕСЕРТ.name())
                       .param("foodName", "")
                       .param("allergens", String.join(", ", List.of(AllergensEnum.ПРЯСНО_МЛЯКО.name())))
                       .param("otherAllergen", "")
                       .with(csrf()))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("org.springframework.validation.BindingResult.addRecipeBindingModel"))
               .andExpect(status().is3xxRedirection());

    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL,
            userDetailsServiceBeanName = "testUserDataService")
    void testAddReceiptAlreadyExists() throws Exception {

        mockMvc.perform(post("/admin/add-recipe")
                       .param("ageGroup", AgeGroupEnum.МАЛКИ.name())
                       .param("foodCategory", FoodCategoryEnum.ДЕСЕРТ.name())
                       .param("foodName", "SOUP")
                       .param("allergens", String.join(", ", List.of(AllergensEnum.ПРЯСНО_МЛЯКО.name())))
                       .param("otherAllergen", "")
                       .with(csrf()))
               .andExpect(MockMvcResultMatchers.flash()
                                               .attributeExists("existFood"))
               .andExpect(status().is3xxRedirection());

    }
}
