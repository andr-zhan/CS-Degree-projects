-- Introdução de valores na BD:
-- Inserção de 10 membros
INSERT INTO membro (Nome, IdMemb, Pais, DataNasc) VALUES
    ('Ana', 'idana', 'Portugal', '1992-04-10'),
    ('Bernardo', 'idbernardo', 'Portugal', '1983-07-23'),
    ('Carlota', 'idcarlota', 'Espanha', '1997-08-15'),
    ('Carlos', 'idcarlos', 'Portugal', '1989-11-27'),
    ('Ema', 'idema', 'Brasil', '1996-01-13'),
    ('Emanuel', 'idemanuel', 'Itália', '1988-03-19'),
    ('Gabriela', 'idgabriela', 'Portugal', '1997-04-15'),
    ('Francisco', 'idfrancisco', 'Alemanha', '2001-11-05'),
    ('Isabel', 'idisabel', 'Espanha', '2002-06-25'),
    ('Jonathan', 'idjonathan', 'Portugal', '1999-02-17');

-- Inserção dos 13 ingredientes na tabela ingrediente
INSERT INTO ingrediente (Nome, Custo) VALUES
    ('Açucar', 0.6),
    ('Farinha', 0.2),
    ('Chocolate', 1.1),
    ('Ovos', 0.2),
    ('Natas', 0.5),
    ('Leite', 0.8),
    ('Água', 0.1),
    ('Maçã', 0.7),
    ('Manteiga', 1.2),
    ('Alfarroba', 1.3),
    ('Baunilha', 1.6),
    ('Canela', 0.9),
    ('Pimenta', 0.7);

-- Inserção de 10 doces na tabela doce
INSERT INTO doce (Nome, Descricao, Genero) VALUES
    ('Pastel de Nata', 'Massa folhada com natas', 'Tradicional'),
    ('Tiramisu', 'Doce de café e rum', 'Regional'),
    ('Mousse de Chocolate', 'Mousse cremosa de chocolate', 'Regional'),
    ('Pudim de Caramelo', 'Pudim com caramelo', 'Tradicional'),
    ('Biscoitos', 'Biscoitos crocantes com pepitas de chocolate', 'Regional'),
    ('Tarte de Maçã', 'Tarte com maça caramelizada', 'Internacional'),
    ('Torta de Amêndoa', 'Torta de amêndoa', 'Doce de Camada'),
    ('Bolo de Chocolate', 'Bolo com chocolate', 'Regional'),
    ('Bolo de Alfarroba', 'Bolo com alfarroba e frutos secos', 'Regional'),
    ('Tarte de Limão', 'Tarte doce com limão fresco', 'Tradicional');

-- Inserção dos dados na tabela criou (10 doces criados por 5 membros diferentes)
INSERT INTO criou (Membro, Doce) VALUES
    ('idana', 'Mousse de Chocolate'),
    ('idbernardo', 'Tarte de Maçã'),
    ('idcarlota', 'Bolo de Chocolate'),
    ('idcarlos', 'Pudim de Caramelo'),
    ('idema', 'Biscoitos'),
    ('idana', 'Tiramisu'),
    ('idbernardo', 'Torta de Amêndoa'),
    ('idcarlota', 'Pastel de Nata'),
    ('idcarlos', 'Bolo de Alfarroba'),
    ('idema', 'Tarte de Limão');

-- Inserção dos dados na tabela fez (Cada doce foi feito por 2 membros, e um dos doces foi feito por 3 membros)
INSERT INTO fez (Membro, Doce, Tempo, Aspeto, Sabor) VALUES
    ('idana', 'Bolo de Chocolate', 3, 4, 5),
    ('idbernardo', 'Bolo de Chocolate', 4, 3, 5),
    ('idcarlota', 'Tarte de Maçã', 2, 5, 4),
    ('idcarlos', 'Tarte de Maçã', 3, 4, 3),
    ('idema', 'Mousse de Chocolate', 1, 5, 5),
    ('idana', 'Mousse de Chocolate', 1, 4, 4),
    ('idcarlota', 'Pudim de Caramelo', 5, 4, 3),
    ('idfrancisco', 'Pudim de Caramelo', 3, 5, 3),
    ('idcarlos', 'Biscoitos', 2, 5, 4),
    ('idgabriela', 'Biscoitos', 2, 4, 4),
    ('idbernardo', 'Tiramisu', 4, 5, 5),
    ('idana', 'Tiramisu', 5, 5, 3),
    ('idema', 'Pastel de Nata', 3, 5, 4),
    ('idana', 'Pastel de Nata', 4, 4, 5),
    ('idbernardo', 'Bolo de Alfarroba', 3, 4, 4),
    ('idcarlota', 'Bolo de Alfarroba', 2, 5, 3),
    ('idema', 'Bolo de Alfarroba', 5, 4, 5),
    ('idbernardo', 'Torta de Amêndoa', 4, 4, 5),
    ('idisabel', 'Torta de Amêndoa', 3, 4, 4),
    ('idjonathan', 'Tarte de Limão', 3, 4, 5),
    ('idemanuel', 'Tarte de Limão', 5, 5, 5);

-- Inserção das amizades na tabela amigo
-- O membro com IdMemb = 'idana' é amigo de todos os outros
INSERT INTO amigo (Membro1, Membro2) VALUES
    ('idana', 'idbernardo'), ('idbernardo', 'idana'),
    ('idana', 'idcarlota'), ('idcarlota', 'idana'),
    ('idana', 'idcarlos'), ('idcarlos', 'idana'),
    ('idana', 'idema'), ('idema', 'idana'),
    ('idana', 'idemanuel'), ('idemanuel', 'idana'),
    ('idana', 'idgabriela'), ('idgabriela', 'idana'),
    ('idana', 'idfrancisco'), ('idfrancisco', 'idana'),
    ('idana', 'idisabel'), ('idisabel', 'idana'),
    ('idana', 'idjonathan'), ('idjonathan', 'idana');

-- 5 membros com pelo menos 3 amigos cada
INSERT INTO amigo (Membro1, Membro2) VALUES
    ('idbernardo', 'idcarlota'), ('idcarlota', 'idbernardo'),
    ('idbernardo', 'idcarlos'), ('idcarlos', 'idbernardo'),
    ('idbernardo', 'idema'), ('idema', 'idbernardo'),
    ('idcarlota', 'idcarlos'), ('idcarlos', 'idcarlota'),
    ('idcarlota', 'idema'), ('idema', 'idcarlota'),
    ('idcarlos', 'idema'), ('idema', 'idcarlos'),
    ('idcarlos', 'idemanuel'), ('idemanuel', 'idcarlos'),
    ('idema', 'idemanuel'), ('idemanuel', 'idema');

-- Valores necessários para alínea 4.a)
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
	('Bolo de Chocolate', 'Chocolate', 250),
    ('Bolo de Chocolate', 'Canela', 2);

-- Valores necessários para alínea 4.b)
-- Inserção do membro Joaquim José
INSERT INTO membro (Nome, IdMemb, Pais, DataNasc) VALUES
    ('Joaquim José', 'idjoaquimjose', 'Portugal', '1987-05-11');
-- Inserção dos amigos de Joaquim José que criaram doces tradicionais
INSERT INTO amigo (Membro1, Membro2) VALUES
    ('idjoaquimjose', 'idema'), ('idema', 'idjoaquimjose'),
    ('idjoaquimjose', 'idcarlota'), ('idcarlota', 'idjoaquimjose'),
    ('idjoaquimjose', 'idcarlos'), ('idcarlos', 'idjoaquimjose');

-- Inserção do ingrediente "Natas" no doce Pastel de Nata para alínea 4.d)
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
	('Pastel de Nata', 'Natas', 150);
-- Valores necessários para alínea 4.e) (Inserção do ingrediente "Leite")
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
    ('Bolo de Chocolate', 'Leite', 250);
-- Valores necessários para alínea 4.f)
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
    ('Tarte de Maçã', 'Maçã', 150),
    ('Pastel de Nata', 'Baunilha', 2);

-- Inserção do arroz doce, valores necessários para alinea 4.g)
INSERT INTO doce (Nome, Descricao, Genero) VALUES
    ('Arroz Doce', 'Arroz com leite e canela e açucar', 'Tradicional');
INSERT INTO criou (Membro, Doce) VALUES
	('idjonathan', 'Arroz Doce');
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
	('Arroz Doce', 'Açucar', 250),
    ('Arroz Doce', 'Leite', 200),
    ('Arroz Doce', 'Canela', 3);

-- Inserção dos valores necessários para alinea 4.h)
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
	('Tiramisu', 'Ovos', 6),
    ('Tiramisu', 'Chocolate', 3),
    ('Tiramisu', 'Açucar', 150),
    ('Mousse de Chocolate', 'Manteiga', 20),
    ('Mousse de Chocolate', 'Chocolate', 1),
    ('Mousse de Chocolate', 'Açucar', 50),
    ('Pudim de Caramelo', 'Ovos', 4),
    ('Pudim de Caramelo', 'Leite', 200),
    ('Pudim de Caramelo', 'Açucar', 150),
    ('Biscoitos', 'Farinha', 350),
    ('Biscoitos', 'Açucar', 150),
    ('Biscoitos', 'Ovos', 2),
    ('Torta de Amêndoa', 'Farinha', 280),
    ('Torta de Amêndoa', 'Açucar', 120),
    ('Torta de Amêndoa', 'Ovos', 3),
    ('Bolo de Alfarroba', 'Alfarroba', 150),
    ('Bolo de Alfarroba', 'Açucar', 170),
    ('Bolo de Alfarroba', 'Ovos', 5),
    ('Tarte de Limão', 'Manteiga', 80),
    ('Tarte de Limão', 'Leite', 170),
    ('Tarte de Limão', 'Água', 100),
    ('Tarte de Maçã', 'Leite', 160),
    ('Tarte de Maçã', 'Açucar', 80);

-- Inserção de valores necessários para 4.j)
INSERT INTO doce (Nome, Descricao, Genero) VALUES
    ('Doce de Natas', 'Doce caseiro de natas', 'Tradicional');
INSERT INTO criou (Membro, Doce) VALUES
    ('idana', 'Doce de Natas');
INSERT INTO temingrediente (Doce, Ingrediente, Quantidade) VALUES
	('Doce de Natas', 'Ovos', 5),
    ('Doce de Natas', 'Leite', 200),
    ('Doce de Natas', 'Açucar', 110);

