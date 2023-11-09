package bg.softuni.childrenkitchen.model;

import org.springframework.context.ApplicationEvent;

public class RegistrationChildEvent extends ApplicationEvent {
    private Long childId;

    public RegistrationChildEvent(Object source) {
        super(source);
    }

    public RegistrationChildEvent setChildId (Long childId){
        this.childId = childId;
        return this;
    }

    public Long getChildId() {
        return childId;
    }
}
