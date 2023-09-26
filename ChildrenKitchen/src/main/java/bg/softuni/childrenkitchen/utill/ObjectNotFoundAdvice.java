package bg.softuni.childrenkitchen.utill;

import bg.softuni.childrenkitchen.model.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ObjectNotFoundAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView onObjectNotFound(ObjectNotFoundException onfe){
        ModelAndView modelAndView = new ModelAndView("error-not-found");

        modelAndView.addObject("error", onfe.getMessage());

        return modelAndView;
    }
}
