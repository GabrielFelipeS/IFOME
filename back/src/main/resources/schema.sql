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
    name VARCHAR(255) NOT NULL, -- Nome é obrigatório e não pode ser nulo
    cpf VARCHAR(14) NOT NULL, -- CPF deve ser único, com formato 999.999.999-99
    email VARCHAR(255) NOT NULL, -- Email é obrigatório e validado
    password VARCHAR(255) NOT NULL, -- Senha é obrigatória
    confirmation_password VARCHAR(255) NOT NULL, -- Confirmação de senha obrigatória
    date_of_birth DATE NOT NULL, -- Data de nascimento é obrigatória e deve estar no passado
    type_of_vehicle VARCHAR(10), -- Tipo do veículo: carro ou moto (valores válidos são 'carro', 'moto')
    plate VARCHAR(8), -- Placa no formato XXX-9999
    telephone VARCHAR(20), -- Telefone no formato (XX) XXXXX-XXXX
    cnh_number VARCHAR(9) NOT NULL, -- Número da CNH com exatamente 9 dígitos
    cnh_validity DATE NOT NULL, -- Validade da CNH deve ser uma data futura
    vehicle_document VARCHAR(11), -- RENAVAM deve conter entre 9 e 11 dígitos numéricos
    bank_account_id BIGINT, -- Referência para a tabela de contas bancárias (se aplicável)
    CONSTRAINT unique_cpf UNIQUE (cpf), -- CPF deve ser único
    CONSTRAINT unique_email UNIQUE (email) -- Email deve ser único
);

CREATE TABLE opening_hours(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj        VARCHAR(14) NOT NULL REFERENCES restaurants(cnpj),
    day_of_the_week    VARCHAR(16) NOT NULL,
    opening     VARCHAR(16) NOT NULL,
    closing     VARCHAR(16) NOT NULL
);