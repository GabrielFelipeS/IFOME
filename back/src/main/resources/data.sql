-- $2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG  -> @Password1
INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'user1@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '52800314028', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '92051362041', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'noreply.ifome@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '77025681008',  'CLIENT');

INSERT INTO restaurants (
    name_restaurant,
    cnpj,
    food_category,
    telephone,
    person_responsible,
    person_responsible_cpf,
    email,
    password,
    payment_methods,
    restaurant_image,
    role,
    is_open
) VALUES (
    'Açai do monge',
    '58911612000116',
    'Sorveteria',
    '(11) 1234-5678',
    'Nome Responsável',
    '07635915053',
    'email1@email.com',
    '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
    'Dinheiro, Cartão',
    'image.jpg',
    'RESTAURANT',
    false
);

INSERT INTO delivery_person (
    email,
    password,
    cpf,
    name,
    date_of_birth,
    type_of_vehicle,
    plate,
    telephone,
    cnh_number,
    cnh_validity,
    vehicle_document,
    role
)
VALUES (
   'email1@email.com',
   '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
   '52800314028',
   'João da Silva',
   '1985-06-15',
   'carro',
   'ABC-1234',
   '(11) 98765-4321',
   '12345678910',
   '2026-12-31',
   '987654321',
    'DELIVERY'
);

