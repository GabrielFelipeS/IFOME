-- $2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG  -> @Password1
INSERT INTO clients (name, email, password, date_of_birth, cpf, type_residence, cep, address, payment_methods)
VALUES ('Gabriel', 'user1@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '528.003.140-28', 'casa', 'cep', 'Av. Salgado Filho, 3501 - Centro, Guarulhos - SP', 'Cartão de Crédito');

INSERT INTO clients (name, email, password, date_of_birth, cpf, type_residence, cep, address, payment_methods)
VALUES ('Gabriel', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '920.513.620-41', 'casa', 'cep', 'Av. Salgado Filho, 3501 - Centro, Guarulhos - SP', 'Cartão de Crédito');

INSERT INTO clients (name, email, password, date_of_birth, cpf, type_residence, cep, address, payment_methods)
VALUES ('Gabriel', 'noreply.ifome@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '770.256.810-08', 'casa', 'cep', 'Av. Salgado Filho, 3501 - Centro, Guarulhos - SP', 'Cartão de Crédito');

INSERT INTO restaurants (
    name_restaurant,
    cnpj,
    food_category,
    cep,
    address,
    telephone,
    opening_hours,
    person_responsible,
    person_responsible_CPF,
    email,
    password,
    payment_methods,
    restaurant_image,
    bank_account
) VALUES (
    'id'
    'Nome Restaurante',
    '58.911.612/0001-16',
    'Pizzaria',
    '07070-000',
    'Endereço completo',
    '(11) 1234-5678',
    '12:00',
    'Nome Responsável',
    '13212312365',
    'email1@email.com',
    '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
    'Dinheiro, Cartão',
    'imagem_restaurante.jpg',
    'Banco XYZ, Agência 123, Conta 456789'
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
    vehicle_document
)
VALUES (
   'email1@email.com',
   '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
   '528.003.140-28',
   'João da Silva',
   '1985-06-15',
   'carro',
   'ABC-1234',
   '(11) 98765-4321',
   '12345678910',
   '2026-12-31',
   '987654321'
);

