package bg.softuni.childrenkitchen.model.binding;

import java.time.LocalDate;

public class DeleteOrderBindingModel {
    private LocalDate deleteOrderDate;
    private String childName;

    public LocalDate getDeleteOrderDate() {
        return deleteOrderDate;
    }

    public void setDeleteOrderDate(LocalDate deleteOrderDate) {
        this.deleteOrderDate = deleteOrderDate;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}
