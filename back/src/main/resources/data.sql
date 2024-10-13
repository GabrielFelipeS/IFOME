-- $2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG  -> @Password1
INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'user1@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '52800314028', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '92051362041', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, role)
VALUES ('Gabriel', 'noreply.ifome@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '77025681008',  'CLIENT');

INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, client_id)
VALUES
    ('Endereço João', '05413-020', 'Pinheiros', 'São Paulo', 'SP', 'Rua dos Três Irmãos', '50', 'Apto 101', 'Próximo ao Parque Villa-Lobos', 'Casa', 1),
    ('Endereço Maria', '01234-567', 'Centro', 'São Paulo', 'SP', 'Avenida São João', '500', NULL, 'Próximo à Praça da República', 'Apartamento', 2),
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 3);

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
    bank,
    agency,
    account,
    is_open,
    role
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
    'Banco do Brasil',
    '1234',
    '00012345-6',
    false,
    'RESTAURANT'
);

INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, restaurant_id)
VALUES
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 1);

INSERT INTO opening_hours (day_of_the_week, opening, closing, restaurant_id) VALUES
     ('Segunda-feira', '08:00', '22:00', 1),
     ('Terça-feira', '08:00', '22:00', 1),
     ('Quarta-feira', '08:00', '22:00', 1),
     ('Quinta-feira', '08:00', '22:00', 1),
     ('Sexta-feira', '08:00', '23:00', 1),
     ('Sábado', '09:00', '23:00', 1),
     ('Domingo', '09:00', '22:00', 1);

INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, role
)
VALUES (
    'email1@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
    '52800314028','João da Silva', '1985-06-15','carro',
    'ABC-1234','(11) 98765-4321','12345678910',
    'Banco do Brasil','1234','00012345-6',
    '2026-12-31','987654321','DELIVERY'

);

INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, delivery_id)
VALUES
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 1);
