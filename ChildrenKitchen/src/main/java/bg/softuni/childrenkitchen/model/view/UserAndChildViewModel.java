package bg.softuni.childrenkitchen.model.view;

import java.util.Set;

public class UserAndChildViewModel {
    private String userEmail;
    private String userNames;
    private Set<String> childrenNames;

    private String servicePointName;
    private String phoneNumber;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public Set<String> getChildrenNames() {
        return childrenNames;
    }

    public void setChildrenNames(Set<String> childrenNames) {
        this.childrenNames = childrenNames;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
