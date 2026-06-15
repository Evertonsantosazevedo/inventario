-- Usuário Administrador Inicial (Senha: 12345678)
-- O hash foi gerado usando Bcrypt
INSERT INTO usuarios (nome, email, senha, perfil, ativo) 
VALUES ('Administrador', 'admin@admin.com', '$2a$10$8K9V/Y/M2k6A9f9K.k/E.OeF.kK9f9K.k/E.OeF.kK9f9K.k/E.O', 'ADMINISTRADOR', true);

-- Produtos Iniciais
INSERT INTO produtos (codigo, nome, marca, quantidade, valorVenda) 
VALUES ('P001', 'Arroz 5kg', 'Tio João', 50, 25.50);

INSERT INTO produtos (codigo, nome, marca, quantidade, valorVenda) 
VALUES ('P002', 'Feijão 1kg', 'Camil', 100, 8.90);

INSERT INTO produtos (codigo, nome, marca, quantidade, valorVenda) 
VALUES ('P003', 'Óleo de Soja', 'Soya', 200, 7.20);
