package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.PointEntity;
import bg.softuni.childrenkitchen.repository.PointRepository;
import bg.softuni.childrenkitchen.service.PointService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    public PointServiceImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public void initDB() {
        if (pointRepository.count() > 0){
            return;
        }

        PointEntity violet = new PointEntity();
        violet.setName("ДГ Теменуга");
        violet.setAddress("ул. Ангел Войвода №2");
        violet.setWorkingTime("11:30ч. - 12:00ч");
        violet.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D1%82%D0%B5%D0%BC%D0%B5%D0%BD%D1%83%D0%B3%D0%B0_tmevd2.jpg");

        PointEntity thirdOfMArch = new PointEntity();
        thirdOfMArch.setName("ДГ Трети март");
        thirdOfMArch.setAddress("ул. Сергей Румянцев №69");
        thirdOfMArch.setWorkingTime("11:30ч. - 12:00ч");
        thirdOfMArch.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/3mart_kjzwbz.jpg");

        PointEntity drujba = new PointEntity();
        drujba.setName("ДГ Дружба");
        drujba.setAddress("ж.к. Дружба 1");
        drujba.setWorkingTime("11:30ч. - 12:00ч");
        drujba.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%B4%D1%80%D1%83%D0%B6%D0%B1%D0%B0_xkmeio.jpg");

        PointEntity nightingale = new PointEntity();
        nightingale.setName("ДГ Славейче");
        nightingale.setAddress("ж.к. Сторгозия");
        nightingale.setWorkingTime("11:30ч. - 12:15ч");
        nightingale.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D1%81%D0%BB%D0%B0%D0%B2%D0%B5%D0%B9%D1%87%D0%B5_clqfwp.jpg");

        PointEntity oblivion = new PointEntity();
        oblivion.setName("ДГ Незабравка");
        oblivion.setAddress("ул. Оряхово №6");
        oblivion.setWorkingTime("11:30ч. - 12:00ч");
        oblivion.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089118/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BD%D0%B5%D0%B7%D0%B0%D0%B1%D1%80%D0%B0%D0%B2%D0%BA%D0%B0_g56mql.jpg");

        PointEntity kalina = new PointEntity();
        kalina.setName("ДГ Калина");
        kalina.setAddress("ж.к. Дружба");
        kalina.setWorkingTime("11:30ч. - 12:15ч");
        kalina.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BA%D0%B0%D0%BB%D0%B8%D0%BD%D0%B0_icszlx.png");

        PointEntity latin = new PointEntity();
        latin.setName("ДЯ Латинка");
        latin.setAddress("ул. Бузлуджа №26");
        latin.setWorkingTime("11:30ч. - 12:15ч");
        latin.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BB%D0%B0%D1%82%D0%B8%D0%BD%D0%BA%D0%B0_z4qvmm.jpg");

        PointEntity nurseryDrujba = new PointEntity();
        nurseryDrujba.setName("ДЯ Дружба");
        nurseryDrujba.setAddress("ул. Борис Шивачев №36");
        nurseryDrujba.setWorkingTime("11:30ч. - 12:00ч");
        nurseryDrujba.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D1%8F_%D0%B4%D1%80%D1%83%D0%B6%D0%B1%D0%B0_qkcdod.jpg");

        PointEntity peace = new PointEntity();
        peace.setName("ДЯ Мир");
        peace.setAddress("ж.к. Сторгозия");
        peace.setWorkingTime("11:30ч. - 12:00ч");
        peace.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D1%8F_%D0%BC%D0%B8%D1%80_yijqe3.jpg");

        PointEntity seagull = new PointEntity();
        seagull.setName("ДЯ Чайка");
        seagull.setAddress("ул. 10-ти декември №40");
        seagull.setWorkingTime("11:30ч. - 12:00ч");
        seagull.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D0%B4%D0%B5%D1%82%D1%81%D0%BA%D0%B0_%D1%8F%D1%81%D0%BB%D0%B0_%D1%87%D0%B0%D0%B9%D0%BA%D0%B0_bxnnke.jpg");

        PointEntity ninthDistrict = new PointEntity();
        ninthDistrict.setName("9-ти квартал");
        ninthDistrict.setAddress("ул. Гурко №10");
        ninthDistrict.setWorkingTime("11:30ч. - 12:15ч");
        ninthDistrict.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/gurko_tugnfe.jpg");

        PointEntity centralPoint = new PointEntity();
        centralPoint.setName("Детска кухня");
        centralPoint.setAddress("ул. Георги Кочев");
        centralPoint.setWorkingTime("11:30ч. - 12:30ч");
        centralPoint.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%BA_w8lrse.jpg");

        pointRepository.saveAll(List.of(centralPoint, ninthDistrict, seagull, peace, nurseryDrujba, latin, kalina, oblivion, nightingale, drujba, thirdOfMArch, violet));
    }

    @Override
    public Optional<PointEntity> getByName(String pointName) {
       return pointRepository.findByName(pointName);
    }

    @Override
    public Set<String> getAll() {
        return   pointRepository.findAll().stream()
                .map(PointEntity::getName)
                .collect(Collectors.toSet());
    }
}
