package bg.softuni.childrenkitchen.exception;

public class EmailProblemException extends RuntimeException{
    public EmailProblemException() {
        super("Проблем при изпращане на имейл! Моля да ни извините!");
    }
}
