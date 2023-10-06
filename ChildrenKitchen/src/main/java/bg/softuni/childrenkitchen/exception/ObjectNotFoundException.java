package bg.softuni.childrenkitchen.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException() {
        super("Няма намерен запис в базата данни !");
    }
}
