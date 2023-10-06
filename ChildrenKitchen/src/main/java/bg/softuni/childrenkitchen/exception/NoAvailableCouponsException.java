package bg.softuni.childrenkitchen.exception;

public class NoAvailableCouponsException extends RuntimeException{
    public NoAvailableCouponsException() {
        super("Нямате налични купони! Моля закупете от системата или на място в Детска Кухня!");
    }
}
