INSERT INTO address (id, street) VALUES
                                     (NEXTVAL('address_seq'), 'Москва, ул. Тверская, д. 15, кв. 42'),
                                     (NEXTVAL('address_seq'), 'Санкт-Петербург, Невский пр., д. 28'),
                                     (NEXTVAL('address_seq'), 'Казань, ул. Баумана, д. 7'),
                                     (NEXTVAL('address_seq'), 'Екатеринбург, ул. Вайнера, д. 10'),
                                     (NEXTVAL('address_seq'), 'Новосибирск, Красный пр., д. 200');

-- Затем вставляем клиентов, связывая с адресами
INSERT INTO client (id, name, addressId) VALUES
                                             (NEXTVAL('client_seq'), 'Сергей Васильев', 1),
                                             (NEXTVAL('client_seq'), 'Ольга Смирнова', 2),
                                             (NEXTVAL('client_seq'), 'Андрей Попов', 3),
                                             (NEXTVAL('client_seq'), 'Наталья Ковалева', 4),
                                             (NEXTVAL('client_seq'), 'Павел Морозов', 5);

-- Добавляем телефоны для клиентов
-- Клиент 1: Сергей Васильев (2 телефона)
INSERT INTO phone (id, number, clientId) VALUES
                                             (NEXTVAL('phone_seq'), '+7 (999) 123-45-67', 1),
                                             (NEXTVAL('phone_seq'), '+7 (495) 777-88-99', 1);

-- Клиент 2: Ольга Смирнова (1 телефон)
INSERT INTO phone (id, number, clientId) VALUES
    (NEXTVAL('phone_seq'), '+7 (911) 222-33-44', 2);

-- Клиент 3: Андрей Попов (3 телефона)
INSERT INTO phone (id, number, clientId) VALUES
                                             (NEXTVAL('phone_seq'), '+7 (905) 555-55-55', 3),
                                             (NEXTVAL('phone_seq'), '+7 (906) 666-66-66', 3),
                                             (NEXTVAL('phone_seq'), '+7 (903) 777-77-77', 3);

-- Клиент 4: Наталья Ковалева (2 телефона)
INSERT INTO phone (id, number, clientId) VALUES
                                             (NEXTVAL('phone_seq'), '+7 (915) 888-88-88', 4),
                                             (NEXTVAL('phone_seq'), '+7 (916) 999-99-99', 4);

-- Клиент 5: Павел Морозов (1 телефон)
INSERT INTO phone (id, number, clientId) VALUES
    (NEXTVAL('phone_seq'), '+7 (925) 111-11-11', 5);