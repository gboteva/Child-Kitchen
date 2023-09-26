package bg.softuni.childrenkitchen.model.exception;

public class NoAvailableCouponsError extends RuntimeException{
    public NoAvailableCouponsError() {
        super("Нямате налични купони! Моля закупете от системата или на място в Детска Кухня!");
    }
}
