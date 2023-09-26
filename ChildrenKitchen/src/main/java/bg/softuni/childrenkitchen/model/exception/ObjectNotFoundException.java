package bg.softuni.childrenkitchen.model.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException() {
        super("Няма намерен запис в базата данни !");
    }
}
