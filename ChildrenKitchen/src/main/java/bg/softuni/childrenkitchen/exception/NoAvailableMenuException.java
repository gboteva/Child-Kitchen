package bg.softuni.childrenkitchen.exception;

public class NoAvailableMenuException extends RuntimeException{
    public NoAvailableMenuException() {
        super("Все още няма въведено меню за тази дата!");
    }
}
