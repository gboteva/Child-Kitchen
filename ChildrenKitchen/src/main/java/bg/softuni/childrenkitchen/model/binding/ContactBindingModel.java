package bg.softuni.childrenkitchen.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactBindingModel {
    @NotBlank
    @Email
    private String recipient;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
