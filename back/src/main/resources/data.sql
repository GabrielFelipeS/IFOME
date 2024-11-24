-- $2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG  -> @Password1
INSERT INTO clients (name, email, password, date_of_birth, cpf, phone,  role)
VALUES ('Gabriel', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '92051362041', '(12) 1234-1234', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, phone, role)
VALUES ('Gabriel', 'user1@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '52800314028', '(12) 1234-1234','CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, phone,  role)
VALUES ('Telha Rina', 'telha_rina@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '20000596000', '(12) 1234-1234', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf,  phone, role)
VALUES ('Gabriel', 'noreply.ifome@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '77025681008', '(12) 1234-1234',  'CLIENT');

INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, latitude, longitude, client_id)
VALUES
    ('Endereço João', '05413-020', 'Pinheiros', 'São Paulo', 'SP', 'Rua dos Três Irmãos', '50', NULL, 'Próximo à Praça da República', 'Casa', '-23.5895527', '-46.7157754', 1),
    ('Endereço Maria', '01234-567', 'Centro', 'São Paulo', 'SP', 'Avenida São João', '500','Apto 101', 'Próximo ao Parque Villa-Lobos' , 'Apartamento', '-23.1958622', '-45.89846969888535', 2),
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', '-23.553584112883435', '-46.62239845521472', 3),
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa',  '-23.553584112883435', '-46.62239845521472', 4);

INSERT INTO restaurants (name_restaurant, cnpj, food_category, telephone, person_responsible, person_responsible_cpf, email, password, payment_methods, restaurant_image, bank, agency, account, is_open, role, rating)
VALUES ('Açai do monge', '58911612000116', 'Sorveteria', '(11) 1234-5678', 'Nome Responsável', '07635915053', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', 'Dinheiro, Cartão', 'monge.jpeg', 'Banco do Brasil', '1234', '00012345-6', true, 'RESTAURANT', 0);


INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, latitude, longitude, restaurant_id)
VALUES
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202',
     'Perto do Parque da Mooca', 'Casa','-23.5585905', '-46.5900739', 1);
--      'Perto do Parque da Mooca', 'Casa','-23.524153650000002', '-46.621807429313016', 1);

INSERT INTO opening_hours (day_of_the_week, opening, closing, restaurant_id) VALUES
     ('Segunda-feira', '08:00', '22:00', 1),
     ('Terça-feira', '08:00', '22:00', 1),
     ('Quarta-feira', '08:00', '22:00', 1),
     ('Quinta-feira', '08:00', '22:00', 1),
     ('Sexta-feira', '08:00', '23:00', 1),
     ('Sábado', '09:00', '23:00', 1),
     ('Domingo', '09:00', '22:00', 1);

INSERT INTO dish (
    name,
    description,
    price,
    dish_category,
    dish_image,
    availability,
    restaurant_id
) VALUES
      ('Açai com Frutas', 'Açai com banana, morango e granola', 19.90, 'Sobremesa', 'acai_frutas.jpeg', 'Indisponível', 1),
      ('Pizza de Calabresa', 'Pizza com calabresa, cebola e azeitonas', 34.90, 'Prato Principal', 'pizza_calabresa.jpeg', 'Indisponível', 1),
      ('Hambúrguer Artesanal', 'Hambúrguer com queijo, alface e tomate', 29.90, 'Prato Principal', 'hamburguer.jpeg', 'Disponível', 1),
      ('Milkshake de Chocolate', 'Milkshake cremoso de chocolate', 14.90, 'Bebida', 'milkshake_chocolate.jpeg', 'Disponível', 1),
      ('Salada Tropical', 'Salada com frutas e molho de iogurte', 22.50, 'Entrada', 'salada_tropical.jpeg', 'Disponível', 1);

-- Inserindo novo restaurante
INSERT INTO restaurants (
    name_restaurant,cnpj, food_category,
    telephone, person_responsible, person_responsible_cpf,
    email, password, payment_methods,
    restaurant_image, bank, agency,
    account, is_open, role
) VALUES (
     'Pizzaria do Chef', '12345678000199', 'Pizzaria', '(21) 9876-5432',
     'Chef Antônio', '12345678901','email2@email.com',
     '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',  'Dinheiro, Cartão, Pix',
     'pizzaria_chef.jpeg',  'Caixa Econômica',  '4321',
     '00123456-7',  true, 'RESTAURANT'
 );

-- Inserindo endereço para o novo restaurante
INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, latitude, longitude, restaurant_id)
VALUES
    ('Endereço Chef', '22041-001', 'Copacabana',
     'Rio de Janeiro', 'RJ', 'Avenida Atlântica',
     '1000', 'Loja 3', 'Frente à praia', 'Comercial', '-22.9716406', '-43.1845041', 2);

-- Inserindo horário de funcionamento para o novo restaurante
INSERT INTO opening_hours (day_of_the_week, opening, closing, restaurant_id)
VALUES
     ('Segunda-feira', '18:00', '23:00', 2),
     ('Terça-feira', '18:00', '23:00', 2),
     ('Quarta-feira', '18:00', '23:00', 2),
     ('Quinta-feira', '18:00', '23:00', 2),
     ('Sexta-feira', '18:00', '00:00', 2),
     ('Sábado', '18:00', '00:00', 2),
     ('Domingo', '18:00', '22:00', 2);

-- Inserindo pratos para o novo restaurante
INSERT INTO dish (
    name,description, price,
    dish_category, dish_image,availability,
    restaurant_id
) VALUES
      ('Pizza Margherita', 'Pizza tradicional com molho de tomate, mussarela e manjericão', 32.90, 'Prato Principal', 'pizza_margherita.jpeg', 'Disponível', 2),
      ('Pizza Quatro Queijos', 'Pizza com uma combinação de queijos: mussarela, gorgonzola, parmesão e provolone', 36.90, 'Prato Principal', 'pizza_quatro_queijos.jpeg', 'Disponível', 2),
      ('Bruschetta', 'Entrada com pão, tomate fresco e manjericão', 12.90, 'Entrada', 'bruschetta.jpeg', 'Disponível', 2),
      ('Tiramisu', 'Sobremesa italiana com café e mascarpone', 18.90, 'Sobremesa', 'tiramisu.jpeg', 'Indisponível', 2),
      ('Suco de Laranja', 'Suco natural de laranja', 8.90, 'Bebida', 'suco_laranja.jpeg', 'Disponível', 2);

INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude,  available, role
)
VALUES (
    'email1@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
    '52800314028','João da Silva', '1985-06-15','carro',
    'ABC-1234','(11) 98765-4321','12345678910',
    'Banco do Brasil','1234','00012345-6',
    '2026-12-31','987654321', '-23.460437', '-46.5325546', 'Disponível', 'DELIVERY'
);

INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude, available, role
)
VALUES (
           'email2@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '70574622047','Neymar Junior', '1985-06-15','carro',
           'ABC-1234','(11) 98765-4321','12345678911',
           'Banco do Brasil','1234','00012345-6',
           '2026-12-31','987654321', '-23.43881975', '-46.53746045330729','Indisponível', 'DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 1);

-- Entregador 3
INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude, available, role
)
VALUES (
           'email3@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '32841654021','João Silva', '1990-07-20','moto',
           'DEF-5678','(11) 91234-5678','23456789101',
           'Santander','5678','12345678-9',
           '2025-11-30','234567890', '-23.532126', '-46.629177','Indisponível',  'DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço João', '04020-000', 'Vila Mariana', 'São Paulo', 'SP', 'Rua Vergueiro', '400', 'Apto 402', 'Próximo ao metrô Ana Rosa', 'Apartamento', 3);

-- Entregador 4
INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude, available, role
)
VALUES (
           'email4@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '57812543090','Carlos Santana', '1988-08-05','bicicleta',
           'N/A','(11) 95678-1234','34567891202',
           'Caixa Econômica','9876','98765432-1',
           '2024-10-01','345678912', '-23.558708', '-46.648454','Indisponível',  'DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço Carlos', '05510-000', 'Butantã', 'São Paulo', 'SP', 'Av. Vital Brasil', '500', 'Casa', 'Perto do metrô Butantã', 'Casa', 4);

-- Entregador 5
INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude, available, role
)
VALUES (
           'email5@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '91234567890','Fernanda Costa', '1992-09-15','carro',
           'GHI-9101','(11) 92345-6789','45678912303',
           'Itaú','3456','34567890-2',
           '2027-06-15','456789123', '-23.550520', '-46.633308','Indisponível',  'DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço Fernanda', '01310-000', 'Bela Vista', 'São Paulo', 'SP', 'Av. Paulista', '600', 'Apto 603', 'Próximo ao MASP', 'Apartamento', 5);

-- Entregador 6
INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, latitude, longitude, available, role
)
VALUES (
           'email6@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '67812345980','Mariana Silva', '1994-04-25','moto',
           'JKL-3456','(11) 93456-7890','56789123404',
           'Bradesco','1234','45678901-3',
           '2026-08-20','567891234', '-23.561416', '-46.655881', 'Indisponível', 'DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço Mariana', '04578-000', 'Itaim Bibi', 'São Paulo', 'SP', 'Rua Tabapuã', '700', 'Apto 704', 'Próximo ao Parque do Povo', 'Apartamento', 6);

-- Um pedido para client com id 1
INSERT INTO cart (client_id, freight) VALUES (1, 0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, current_order_client_status, freight, order_date) VALUES (1015, 1, 1, 'PENDENTE', 'NOVO', 0.0, CURRENT_TIMESTAMP);

UPDATE cart SET customer_order_id = 1 WHERE client_id = 1 AND id = 1 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,1);
-- Fim dos insert de pedido

-- Outro pedido para cliente com id 2
INSERT INTO cart (client_id, freight) VALUES (2, 0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, current_order_client_status, freight, order_date) VALUES (1015, 1, 2, 'PENDENTE','NOVO',  0.0, CURRENT_TIMESTAMP);

UPDATE cart SET customer_order_id = 2 WHERE client_id = 2  LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO', CURRENT_TIMESTAMP, 2);
-- Fim dos insert de pedido

-- Outro pedido para cliente com id 3
INSERT INTO cart (client_id, freight) VALUES (3,  0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 1, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 2, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 4, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, current_order_client_status, current_order_delivery_status, order_date, delivery_cost, freight) VALUES (1015, 1, 3, 'PENDENTE', 'PRONTO_PARA_ENTREGA', 'NOVO', CURRENT_TIMESTAMP, 10.0, 0.0);

UPDATE cart SET customer_order_id = 3 WHERE client_id = 3 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,3);
INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('EM_PREPARO' ,CURRENT_TIMESTAMP,3);

-- Fim dos insert de pedido

-- Outro pedido para cliente com id 3
INSERT INTO cart (client_id, freight) VALUES (3, 0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 4, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, delivery_id, payment_status, current_order_client_status, current_order_delivery_status, order_date, delivery_cost, freight) VALUES (1015, 1, 3, 6, 'PENDENTE', 'PRONTO_PARA_ENTREGA', 'NOVO', CURRENT_TIMESTAMP, 10.0, 0.0);

UPDATE cart SET customer_order_id = 4 WHERE client_id = 3 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,4);
INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('EM_PREPARO' ,CURRENT_TIMESTAMP,4);
INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('PRONTO_PARA_ENTREGA' ,CURRENT_TIMESTAMP,4);
INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('SAIU_PARA_ENTREGA' ,CURRENT_TIMESTAMP,4);

INSERT INTO order_info_delivery(order_delivery_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,4);
INSERT INTO order_info_delivery(order_delivery_status, local_date_time, customer_order) VALUES ('ACEITO' ,CURRENT_TIMESTAMP,4);

-- Fim dos insert de pedido

-- Outro pedido para cliente com id 2
INSERT INTO cart (client_id, freight) VALUES (2, 0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (5, 3, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, current_order_client_status, freight, order_date) VALUES (1015, 2, 5, 'PENDENTE','NOVO',  0.0, CURRENT_TIMESTAMP);

UPDATE cart SET customer_order_id = 5 WHERE client_id = 2 AND id = 5 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO', CURRENT_TIMESTAMP, 5);
-- Fim dos insert de pedido

INSERT INTO cart (client_id, freight) VALUES (3, 0.0);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (6, 3, 29.00, 5);

INSERT INTO chat (id, order_id, created_at) VALUES (1, 4, '2024-11-22T10:00:00');

INSERT INTO client_delivery_chat (id, client_id, delivery_id) VALUES (1, 3, 6);

INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (1, 'telha_rina@email.com', 'CLIENT', 'Poderia subir?', CURRENT_TIMESTAMP);
INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (1, 'email6@email.com', 'DELIVERY', 'Não', CURRENT_TIMESTAMP);

INSERT INTO chat (id, order_id, created_at) VALUES (2, 4, '2024-11-22T10:00:00');

INSERT INTO client_restaurant_chat (id, client_id, restaurant_id) VALUES (2, 3, 2);

INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (2, 'telha_rina@email.com', 'CLIENT', 'Poderia mandar talher??', CURRENT_TIMESTAMP);
INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (2, 'email2@email.com', 'RESTAURANT', 'Claro!', CURRENT_TIMESTAMP);

INSERT INTO chat (id, order_id, created_at) VALUES (3, 4, '2024-11-22T10:00:00');

INSERT INTO restaurant_delivery_chat (id, restaurant_id, delivery_id) VALUES (3, 2, 6);

INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (3, 'email6@email.com', 'DELIVERY', 'Vou chegar em 2 minutos', CURRENT_TIMESTAMP);
INSERT INTO message (chat_id, email, sender_type, content, created_at) VALUES (3, 'email2@email.com', 'RESTAURANT', 'Ok', CURRENT_TIMESTAMP);

ALTER SEQUENCE CHAT_SEQUENCE RESTART WITH 4;