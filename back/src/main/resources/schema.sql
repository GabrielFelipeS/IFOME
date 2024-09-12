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
