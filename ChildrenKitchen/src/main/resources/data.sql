# insert user role

INSERT INTO `roles`(`role`)
VALUES('USER');
INSERT INTO `roles`(`role`)
VALUES('ADMIN');

# #insert allergens
INSERT INTO `allergens`(`name`)
VALUES('ЯЙЦА');
INSERT INTO `allergens`(`name`)
VALUES('КИСЕЛО МЛЯКО');
INSERT INTO `allergens`(`name`)
VALUES('ГЛУТЕН');
INSERT INTO `allergens`(`name`)
VALUES('ЦЕЛИНА');
INSERT INTO `allergens`(`name`)
VALUES('СИРЕНЕ');
INSERT INTO `allergens`(`name`)
VALUES('ИЗВАРА');
INSERT INTO `allergens`(`name`)
VALUES('КРАВЕ МАСЛО');
INSERT INTO `allergens`(`name`)
VALUES('РИБА');
INSERT INTO `allergens`(`name`)
VALUES('ПРЯСНО МЛЯКО');
INSERT INTO `allergens`(`name`)
VALUES('КАШКАВАЛ');

#insert points

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Теменуга', 'ул. Ангел Войвода №2', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D1%82%D0%B5%D0%BC%D0%B5%D0%BD%D1%83%D0%B3%D0%B0_tmevd2.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Трети март', 'ул. Сергей Румянцев №69', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/3mart_kjzwbz.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Дружба', 'жк. Дружба 1', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%B4%D1%80%D1%83%D0%B6%D0%B1%D0%B0_xkmeio.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Славейче', 'ж.к. Сторгозия', '11:30ч. - 12:15ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D1%81%D0%BB%D0%B0%D0%B2%D0%B5%D0%B9%D1%87%D0%B5_clqfwp.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Незабравка', 'ул. Оряхово №6', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089118/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BD%D0%B5%D0%B7%D0%B0%D0%B1%D1%80%D0%B0%D0%B2%D0%BA%D0%B0_g56mql.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДГ Калина', 'жк. Дружба', '11:30ч. - 12:15ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BA%D0%B0%D0%BB%D0%B8%D0%BD%D0%B0_icszlx.png');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДЯ Латинка', 'ул. Бузлуджа №26', '11:30ч. - 12:15ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%B3_%D0%BB%D0%B0%D1%82%D0%B8%D0%BD%D0%BA%D0%B0_z4qvmm.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДЯ Дружба', 'ул. Борис Шивачев №36', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D1%8F_%D0%B4%D1%80%D1%83%D0%B6%D0%B1%D0%B0_qkcdod.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДЯ Мир', 'жк. Сторгозия', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D1%8F_%D0%BC%D0%B8%D1%80_yijqe3.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('ДЯ Чайка', 'ул. 10-ти декември №40', '11:30ч. - 12:00ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089120/DK_PROJECT/POINTS/%D0%B4%D0%B5%D1%82%D1%81%D0%BA%D0%B0_%D1%8F%D1%81%D0%BB%D0%B0_%D1%87%D0%B0%D0%B9%D0%BA%D0%B0_bxnnke.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('9-ти квартал', 'ул. Гурко №10', '11:30ч. - 12:15ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/gurko_tugnfe.jpg');

INSERT INTO `points`(`name`, `address`, `working_time`, `picture_url`)
VALUES('Детска кухня', 'ул. Георги Кочев', '11:30ч. - 12:30ч', 'https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%BA_w8lrse.jpg');

#insert soups
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ТОПЧЕТА', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА С ПИЛЕШКО МЕСО', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА С ПУЕШКО МЕСО', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ОТ ДОМАТИ С КАРТОФИ', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ОТ КАРТОФИ И МОРКОВИ', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'СУПА ОТ ЗЕЛЕН ФАСУЛ С ФИДЕ', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`,`age_group`)
VALUES('СУПА', 'КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ С ВАРЕНО ЯЙЦЕ', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ БЕЗ ЗАСТРОЙКА', 'ГОЛЕМИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('СУПА', 'КРЕМ СУПА ОТ ГРАХ, МОРКОВИ И ТИКВИЧКА', 'ГОЛЕМИ');

#insert purees
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ЗЕЛЕНЧУКОВО ПЮРЕ С ИЗВАРА', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ МАКАРОНИ СЪС СИРЕНЕ И ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ЛЕЩА СЪС ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ БРОКОЛИ И СИРЕНЕ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`,`age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ТЕЛЕШКО С ОРИЗ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ПИЛЕШКО СЪС ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ЗАЕШКО СЪС ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ТЕЛЕШКО СЪС ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`,`age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ВАРЕНА МУСАКА', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`,`age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ПИЛЕШКО С ГРАХ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ТЕЛЕШКО С ГРАХ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ ЗАЕШКО С ГРАХ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ КУС-КУС СЪС ЗАЕШКО МЕСО И ЗЕЛЕНЧУЦИ', 'МАЛКИ');
INSERT INTO `foods`(`category`, `name`, `age_group`)
VALUES('ПЮРЕ', 'ПЮРЕ ОТ РИБА И ЗЕЛЕНЧУЦИ', 'МАЛКИ');

#insert main food