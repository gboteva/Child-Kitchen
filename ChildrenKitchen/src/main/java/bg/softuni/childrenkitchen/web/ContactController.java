package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.ContactBindingModel;
import bg.softuni.childrenkitchen.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {
    private final EmailService emailService;


    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }



    @GetMapping("/contacts")
    public String getContacts(){
        return "contacts";
    }

    @PostMapping("/contacts")
    public String sendEmail(@Valid ContactBindingModel contactBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("contactBindingModel", contactBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactBindingModel", bindingResult);
            return "redirect:/contacts";
        }

        try {
            emailService.sendEmail(contactBindingModel.getSender(),
                    contactBindingModel.getSubject(),
                    contactBindingModel.getContent());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/thanks-feedback";
    }

    @GetMapping("/thanks-feedback")
    public String getThanks(){
        return "thanks-feedback";
    }

    @GetMapping("/about-us")
    public String aboutUs(){
        return "about-us";
    }

    @ModelAttribute
    public ContactBindingModel contactBindingModel(){
        return new ContactBindingModel();
    }
}
