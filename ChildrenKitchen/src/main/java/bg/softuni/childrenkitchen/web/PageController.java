package bg.softuni.childrenkitchen.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/users/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/about-us")
    public String aboutUs(){
        return "about-us";
    }

    @GetMapping("/healthy-food")
    public String getHealthyFood(){
        return "healthy";
    }


}
