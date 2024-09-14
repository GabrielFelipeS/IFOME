CREATE TABLE clients (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       email VARCHAR(255) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL,
       date_of_birth DATE NOT NULL,
       cpf VARCHAR(14) NOT NULL UNIQUE,
       type_residence VARCHAR(255),
       cep VARCHAR(10),
       address TEXT,
       payment_methods TEXT
);
CREATE TABLE restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_restaurant VARCHAR(255) NOT NULL,
    cnpj VARCHAR(30) NOT NULL UNIQUE,
    food_category VARCHAR(100),
    cep VARCHAR(10),
    address TEXT,
    telephone VARCHAR(15),
    opening_hours_start TIME,
    opening_hours_end TIME,
    person_responsible VARCHAR(255),
    person_responsible_CPF VARCHAR(11),
    email VARCHAR(255) UNIQUE,
    payment_methods TEXT,
    restaurant_image TEXT,
    bank_account VARCHAR(255)
);
