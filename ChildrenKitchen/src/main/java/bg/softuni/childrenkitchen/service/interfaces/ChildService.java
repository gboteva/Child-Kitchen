package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ChildService {
    void initDB() throws FileNotFoundException;

    ChildViewModel saveChild(ChildRegisterBindingModel childRegisterBindingModel, String loggedInUserEmail) throws IOException;

    List<AllergicChildViewModel> getAllAllergicChildrenFromDB();

    void deleteAllChildByUserId(Long id);
}
