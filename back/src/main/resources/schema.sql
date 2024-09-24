CREATE TABLE clients (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
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
    opening_hours TEXT,
    person_responsible VARCHAR(255),
    person_responsible_CPF VARCHAR(11),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    payment_methods TEXT,
    restaurant_image TEXT,
    bank_account VARCHAR(255)
);
CREATE TABLE delivery_person (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     cpf VARCHAR(14) NOT NULL UNIQUE,
     email VARCHAR(255) UNIQUE,
     password VARCHAR(255) NOT NULL
);

CREATE TABLE opening_hours(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj        VARCHAR(14) NOT NULL REFERENCES restaurants(cnpj),
    day_of_the_week    VARCHAR(16) NOT NULL,
    opening     VARCHAR(16) NOT NULL,
    closing     VARCHAR(16) NOT NULL
);