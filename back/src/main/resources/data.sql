-- $2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG  -> @Password1
INSERT INTO clients (name, email, password, date_of_birth, cpf, phone, role)
VALUES ('Gabriel', 'user1@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '52800314028', '(12) 1234-1234','CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, phone,  role)
VALUES ('Gabriel', 'email1@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '92051362041', '(12) 1234-1234', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf, phone,  role)
VALUES ('Telha Rina', 'telha_rina@email.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '20000596000', '(12) 1234-1234', 'CLIENT');

INSERT INTO clients (name, email, password, date_of_birth, cpf,  phone, role)
VALUES ('Gabriel', 'noreply.ifome@gmail.com', '$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG', '2003-04-14', '77025681008', '(12) 1234-1234',  'CLIENT');

INSERT INTO address (name_address, cep, neighborhood, city, state, address, number, complement, details, type_residence, client_id)
VALUES
    ('Endereço João', '05413-020', 'Pinheiros', 'São Paulo', 'SP', 'Rua dos Três Irmãos', '50', NULL, 'Próximo à Praça da República', 'Casa', 1),
    ('Endereço Maria', '01234-567', 'Centro', 'São Paulo', 'SP', 'Avenida São João', '500','Apto 101', 'Próximo ao Parque Villa-Lobos' , 'Apartamento', 2),
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 3),
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 4);

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
    'monge.jpeg',
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
     'Chef Antônio', '12345678901','chefantonio@pizzas.com',
     '$2a$10$dfFjxI3KHQwLTIWbF0X1XeX1MwVsL5kgVPoQOziXm4KDZjO5eVc0u',  'Dinheiro, Cartão, Pix',
     'pizzaria_chef.jpeg',  'Caixa Econômica',  '4321',
     '00123456-7',  true, 'RESTAURANT'
 );

-- Inserindo endereço para o novo restaurante
INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, restaurant_id)
VALUES
    ('Endereço Chef', '22041-001', 'Copacabana',
     'Rio de Janeiro', 'RJ', 'Avenida Atlântica',
     '1000', 'Loja 3', 'Frente à praia', 'Comercial', 2);

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
    cnh_validity, vehicle_document, role
)
VALUES (
    'email1@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
    '52800314028','João da Silva', '1985-06-15','carro',
    'ABC-1234','(11) 98765-4321','12345678910',
    'Banco do Brasil','1234','00012345-6',
    '2026-12-31','987654321','DELIVERY'
);

INSERT INTO delivery_person (
    email, password,
    cpf, name, date_of_birth, type_of_vehicle,
    plate, telephone,  cnh_number,
    bank, agency, account,
    cnh_validity, vehicle_document, role
)
VALUES (
           'user1@email.com','$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG',
           '70574622047','Neymar Junior', '1985-06-15','carro',
           'ABC-1234','(11) 98765-4321','12345678911',
           'Banco do Brasil','1234','00012345-6',
           '2026-12-31','987654321','DELIVERY'
       );

INSERT INTO address (
    name_address, cep, neighborhood,
    city, state, address,
    number, complement, details,
    type_residence, delivery_id)
VALUES
    ('Endereço Carlos', '03090-000', 'Mooca', 'São Paulo', 'SP', 'Rua da Mooca', '300', 'Apto 202', 'Perto do Parque da Mooca', 'Casa', 1);

-- Um pedido para client com id 1
INSERT INTO cart (client_id) VALUES (1);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (1, 3, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, order_date) VALUES (1015, 1, 1, 'PENDENTE', CURRENT_TIMESTAMP);

UPDATE cart SET customer_order_id = 1 WHERE client_id = 1 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,1);
-- Fim dos insert de pedido

-- Outro pedido para cliente com id 2
INSERT INTO cart (client_id) VALUES (2);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (2, 3, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, order_date) VALUES (1015, 1, 2, 'PENDENTE', CURRENT_TIMESTAMP);

UPDATE cart SET customer_order_id = 2 WHERE client_id = 2 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO', CURRENT_TIMESTAMP, 2);
-- Fim dos insert de pedido

-- Outro pedido para cliente com id 3
INSERT INTO cart (client_id) VALUES (3);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 1, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 2, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 4, 29.00, 5);

INSERT INTO customer_order (order_price, restaurant_id, cart_id, payment_status, order_date) VALUES (1015, 1, 3, 'PENDENTE', CURRENT_TIMESTAMP);

UPDATE customer_order SET delivery_id = 1 WHERE id = 3 LIMIT 1;
UPDATE cart SET customer_order_id = 3 WHERE client_id = 3 LIMIT 1;

INSERT INTO order_info (order_status, local_date_time, customer_order) VALUES ('NOVO' ,CURRENT_TIMESTAMP,3);
-- Fim dos insert de pedido


INSERT INTO cart (client_id) VALUES (3);

INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 4, 29.00, 5);
INSERT INTO order_item (cart_id, dish_id, unit_price, quantity ) VALUES (3, 3, 29.00, 5);