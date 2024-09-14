INSERT INTO clients (email, password, date_of_birth, cpf, type_residence, cep, address, payment_methods)
VALUES ('user1@gmail.com', 'senha', '2003-04-14', '528.003.140-28', 'casa', 'cep', 'Av. Salgado Filho, 3501 - Centro, Guarulhos - SP', 'Cartão de Crédito');

INSERT INTO restaurants (
    name_restaurant,
    cnpj,
    food_category,
    cep,
    address,
    telephone,
    opening_hours_start,
    opening_hours_end,
    person_responsible,
    person_responsible_CPF,
    email,
    payment_methods,
    restaurant_image,
    bank_account
) VALUES (
    'Nome Restaurante',
    '10.882.594/0001-65',
    'Pizzaria',
    '07070-000',
    'Endereço completo',
    '(11) 1234-5678',
    '12:00',
    '23:00',
    'Nome Responsável',
    '13212312365',
    'email1@email.com',
    'Dinheiro, Cartão',
    'imagem_restaurante.jpg',
    'Banco XYZ, Agência 123, Conta 456789'
);

