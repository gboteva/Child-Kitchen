package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.view.PointViewModel;
import bg.softuni.childrenkitchen.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class PointRestController {
    private final PointService pointService;

    public PointRestController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/api/points")
    public ResponseEntity<Set<PointViewModel>> getAboutUs(){
        return ResponseEntity.ok(pointService.getAll());
    }
}
