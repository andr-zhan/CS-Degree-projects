-- Inserção de 10 leitores, 5 regulares e 5 frequentes
INSERT INTO leitor (Nome, Nif, Email, Nacionalidade, Tipo) VALUES
('João Maria', 1001, 'joao@mail.com', 'Português', 'regular'),
('Maria Beatriz', 1002, 'maria@mail.com', 'Português', 'regular'),
('Filipe Santos', 1003, 'filipe@mail.com', 'Português', 'regular'),
('Ana Filipa', 1004, 'ana@mail.com', 'Português', 'regular'),
('Pedro Matos', 1005, 'pedro@mail.com', 'Português', 'regular'),
('Lucas Marques', 1006, 'lucas@mail.com', 'Português', 'frequente'),
('Rita Costa', 1007, 'rita@mail.com', 'Português', 'frequente'),
('Beatriz Almeida', 1008, 'beatriz@mail.com', 'Português', 'frequente'),
('Francisco Gonçalves', 1009, 'francisco@mail.com', 'Português', 'frequente'),
('Sofia Trindade', 1010, 'sofia@mail.com', 'Português', 'frequente');

-- Inserção de 3 autores
INSERT INTO autor (Nome, CodA, Nacionalidade) VALUES
('José Saramago', 01, 'Português'),
('Fernando Pessoa', 02, 'Português'),
('Eça de Queirós', 03, 'Português');

INSERT INTO editora (Nome, NifEditora, Email) VALUES
('Porto Editora', 101, 'portoeditora@gmail.com'),
('LeYa', 102, 'leyaeditora@gmail.com');

-- Inserção de 10 obras
INSERT INTO livro (Titulo, ISBN, NifEditora) VALUES
('Memorial do Convento', 10001, 101),
('Ensaio sobre a Cegueira', 10002, 101),
('O Homem Duplicado', 10003, 101),
('Mensagem', 10004, 101),
('Poemas de Alberto Caeiro', 10005, 101),
('O Guardador de Rebanhos', 10006, 102),
('Os Maias', 10007, 102),
('A Cidade e as Serras', 10008, 102),
('A Relíquia', 10009, 102),
('Caim', 10010, 102);

-- 10 obras de 3 autores diferentes, 2 obras têm dois autores (10005 e 10010)
INSERT INTO autoria (ISBN, CodA) VALUES
(10001, 01),
(10002, 01),
(10003, 01),
(10004, 02),
(10005, 02),
(10005, 03),
(10006, 02),
(10007, 03),
(10008, 03),
(10009, 03),
(10010, 01),
(10010, 02);

-- 3 Exemplares para cada obra
INSERT INTO exemplar (IdExemplar, ISBN, LinkDownload) VALUES
(01, 10001, 'http://example.com/memorial01'),
(02, 10001, 'http://example.com/memorial02'),
(03, 10001, 'http://example.com/memorial03'),
(04, 10002, 'http://example.com/ensaio01'),
(05, 10002, 'http://example.com/ensaio02'),
(06, 10002, 'http://example.com/ensaio03'),
(07, 10003, 'http://example.com/homemduplicado01'),
(08, 10003, 'http://example.com/homemduplicado02'),
(09, 10003, 'http://example.com/homemduplicado03'),
(10, 10004, 'http://example.com/mensagem01'),
(11, 10004, 'http://example.com/mensagem02'),
(12, 10004, 'http://example.com/mensagem03'),
(13, 10005, 'http://example.com/poemasalbertocaeiro01'),
(14, 10005, 'http://example.com/poemasalbertocaeiro02'),
(15, 10005, 'http://example.com/poemasalbertocaeiro03'),
(16, 10006, 'http://example.com/guardadorrebanhos01'),
(17, 10006, 'http://example.com/guardadorrebanhos02'),
(18, 10006, 'http://example.com/guardadorrebanhos03'),
(19, 10007, 'http://example.com/maias01'),
(20, 10007, 'http://example.com/maias02'),
(21, 10007, 'http://example.com/maias03'),
(22, 10008, 'http://example.com/cidadeserras01'),
(23, 10008, 'http://example.com/cidadeserras02'),
(24, 10008, 'http://example.com/cidadeserras03'),
(25, 10009, 'http://example.com/reliquia01'),
(26, 10009, 'http://example.com/reliquia02'),
(27, 10009, 'http://example.com/reliquia03'),
(28, 10010, 'http://example.com/caim01'),
(29, 10010, 'http://example.com/caim02'),
(30, 10010, 'http://example.com/caim03');

-- Cada obra tem 3 géneros (há 6 géneros diferentes: Romance, Fantasia, Mistério, Aventura, História e Biografia)
INSERT INTO livro_gen (ISBN, Genero) VALUES
(10001, 'Romance'),
(10001, 'História'),
(10001, 'Mistério'),
(10002, 'Fantasia'),
(10002, 'Aventura'),
(10002, 'Mistério'),
(10003, 'Biografia'),
(10003, 'História'),
(10003, 'Fantasia'),
(10004, 'Romance'),
(10004, 'Aventura'),
(10004, 'História'),
(10005, 'Mistério'),
(10005, 'Romance'),
(10005, 'Aventura'),
(10006, 'Fantasia'),
(10006, 'Aventura'),
(10006, 'Biografia'),
(10007, 'História'),
(10007, 'Biografia'),
(10007, 'Romance'),
(10008, 'Mistério'),
(10008, 'Fantasia'),
(10008, 'Aventura'),
(10009, 'Fantasia'),
(10009, 'História'),
(10009, 'Aventura'),
(10010, 'Biografia'),
(10010, 'Mistério'),
(10010, 'Aventura');

-- Inserção dos alugueres nas primeiras 8 semanas (o leitor 1001 não tem nenhum aluguer nessas semanas)
INSERT INTO aluguer (IdAluguer, Semana, Ano, Valor, Nif, IdExemplar) VALUES
--Alugueres do leitor 1002
(1, 1, 2024, 2, 1002, 01),
(2, 2, 2024, 2, 1002, 02),
(3, 3, 2024, 2, 1002, 03),
(4, 4, 2024, 2, 1002, 04),
(5, 5, 2024, 2, 1002, 05),
(6, 6, 2024, 2, 1002, 06),
(7, 7, 2024, 2, 1002, 07),
(8, 8, 2024, 2, 1002, 08),
--Alugueres do leitor 1003
(9, 1, 2024, 2, 1003, 09),
(10, 2, 2024, 2, 1003, 10),
(11, 3, 2024, 2, 1003, 11),
(12, 4, 2024, 2, 1003, 12),
(13, 5, 2024, 2, 1003, 13),
(14, 6, 2024, 2, 1003, 14),
(15, 7, 2024, 2, 1003, 15),
(16, 8, 2024, 2, 1003, 16),
--Alugueres do leitor 1004
(17, 1, 2024, 2, 1004, 17),
(18, 2, 2024, 2, 1004, 18),
(19, 3, 2024, 2, 1004, 19),
(20, 4, 2024, 2, 1004, 20),
(21, 5, 2024, 2, 1004, 21),
(22, 6, 2024, 2, 1004, 22),
(23, 7, 2024, 2, 1004, 23),
(24, 8, 2024, 2, 1004, 24),
--Alugueres do leitor 1005
(25, 1, 2024, 2, 1005, 25),
(26, 2, 2024, 2, 1005, 26),
(27, 3, 2024, 2, 1005, 27),
(28, 4, 2024, 2, 1005, 28),
(29, 5, 2024, 2, 1005, 29),
(30, 6, 2024, 2, 1005, 30),
(31, 7, 2024, 2, 1005, 01),
(32, 8, 2024, 2, 1005, 02),
--Alugueres do leitor 1006
(33, 1, 2024, 1, 1006, 03),
(34, 2, 2024, 1, 1006, 04),
(35, 3, 2024, 1, 1006, 05),
(36, 4, 2024, 1, 1006, 06),
(37, 5, 2024, 1, 1006, 07),
(38, 6, 2024, 1, 1006, 08),
(39, 7, 2024, 1, 1006, 09),
(40, 8, 2024, 1, 1006, 10),
(41, 1, 2024, 1, 1006, 11),
(42, 2, 2024, 1, 1006, 12),
(43, 3, 2024, 1, 1006, 13),
(44, 4, 2024, 1, 1006, 14),
(45, 5, 2024, 1, 1006, 15),
(46, 6, 2024, 1, 1006, 16),
(47, 7, 2024, 1, 1006, 17),
(48, 8, 2024, 1, 1006, 18),
(49, 1, 2024, 1, 1006, 19),
(50, 2, 2024, 1, 1006, 20),
(51, 3, 2024, 1, 1006, 21),
(52, 4, 2024, 1, 1006, 22),
(53, 5, 2024, 1, 1006, 23),
(54, 6, 2024, 1, 1006, 24),
(55, 7, 2024, 1, 1006, 25),
(56, 8, 2024, 1, 1006, 26),
--Alugueres do leitor 1007
(57, 1, 2024, 1, 1007, 27),
(58, 2, 2024, 1, 1007, 28),
(59, 3, 2024, 1, 1007, 29),
(60, 4, 2024, 1, 1007, 30),
(61, 5, 2024, 1, 1007, 01),
(62, 6, 2024, 1, 1007, 02),
(63, 7, 2024, 1, 1007, 03),
(64, 8, 2024, 1, 1007, 04),
(65, 1, 2024, 1, 1007, 05),
(66, 2, 2024, 1, 1007, 06),
(67, 3, 2024, 1, 1007, 07),
(68, 4, 2024, 1, 1007, 08),
(69, 5, 2024, 1, 1007, 09),
(70, 6, 2024, 1, 1007, 10),
(71, 7, 2024, 1, 1007, 11),
(72, 8, 2024, 1, 1007, 12),
(73, 1, 2024, 1, 1007, 13),
(74, 2, 2024, 1, 1007, 14),
(75, 3, 2024, 1, 1007, 15),
(76, 4, 2024, 1, 1007, 16),
(77, 5, 2024, 1, 1007, 17),
(78, 6, 2024, 1, 1007, 18),
(79, 7, 2024, 1, 1007, 19),
(80, 8, 2024, 1, 1007, 20),
--Alugueres do leitor 1008
(81, 1, 2024, 1, 1008, 21),
(82, 2, 2024, 1, 1008, 22),
(83, 3, 2024, 1, 1008, 23),
(84, 4, 2024, 1, 1008, 24),
(85, 5, 2024, 1, 1008, 25),
(86, 6, 2024, 1, 1008, 26),
(87, 7, 2024, 1, 1008, 27),
(88, 8, 2024, 1, 1008, 28),
(89, 1, 2024, 1, 1008, 29),
(90, 2, 2024, 1, 1008, 30),
(91, 3, 2024, 1, 1008, 01),
(92, 4, 2024, 1, 1008, 02),
(93, 5, 2024, 1, 1008, 03),
(94, 6, 2024, 1, 1008, 04),
(95, 7, 2024, 1, 1008, 05),
(96, 8, 2024, 1, 1008, 06),
(97, 1, 2024, 1, 1008, 07),
(98, 2, 2024, 1, 1008, 08),
(99, 3, 2024, 1, 1008, 09),
(100, 4, 2024, 1, 1008, 10),
(101, 5, 2024, 1, 1008, 11),
(102, 6, 2024, 1, 1008, 12),
(103, 7, 2024, 1, 1008, 13),
(104, 8, 2024, 1, 1008, 14),
--Alugueres do leitor 1009
(105, 1, 2024, 1, 1009, 15),
(106, 2, 2024, 1, 1009, 16),
(107, 3, 2024, 1, 1009, 17),
(108, 4, 2024, 1, 1009, 18),
(109, 5, 2024, 1, 1009, 19),
(110, 6, 2024, 1, 1009, 20),
(111, 7, 2024, 1, 1009, 21),
(112, 8, 2024, 1, 1009, 22),
(113, 1, 2024, 1, 1009, 23),
(114, 2, 2024, 1, 1009, 24),
(115, 3, 2024, 1, 1009, 25),
(116, 4, 2024, 1, 1009, 26),
(117, 5, 2024, 1, 1009, 27),
(118, 6, 2024, 1, 1009, 28),
(119, 7, 2024, 1, 1009, 29),
(120, 8, 2024, 1, 1009, 30),
(121, 1, 2024, 1, 1009, 01),
(122, 2, 2024, 1, 1009, 02),
(123, 3, 2024, 1, 1009, 03),
(124, 4, 2024, 1, 1009, 04),
(125, 5, 2024, 1, 1009, 05),
(126, 6, 2024, 1, 1009, 06),
(127, 7, 2024, 1, 1009, 07),
(128, 8, 2024, 1, 1009, 08),
--Alugueres do leitor 1010
(129, 1, 2024, 1, 1010, 09),
(130, 2, 2024, 1, 1010, 10),
(131, 3, 2024, 1, 1010, 11),
(132, 4, 2024, 1, 1010, 12),
(133, 5, 2024, 1, 1010, 13),
(134, 6, 2024, 1, 1010, 14),
(135, 7, 2024, 1, 1010, 15),
(136, 8, 2024, 1, 1010, 16),
(137, 1, 2024, 1, 1010, 17),
(138, 2, 2024, 1, 1010, 18),
(139, 3, 2024, 1, 1010, 19),
(140, 4, 2024, 1, 1010, 20),
(141, 5, 2024, 1, 1010, 21),
(142, 6, 2024, 1, 1010, 22),
(143, 7, 2024, 1, 1010, 23),
(144, 8, 2024, 1, 1010, 24),
(145, 1, 2024, 1, 1010, 25),
(146, 2, 2024, 1, 1010, 26),
(147, 3, 2024, 1, 1010, 27),
(148, 4, 2024, 1, 1010, 28),
(149, 5, 2024, 1, 1010, 29),
(150, 6, 2024, 1, 1010, 30),
(151, 7, 2024, 1, 1010, 08),
(152, 8, 2024, 1, 1010, 07);

-- Inserção dos pagamentos de 9 leitores corretos e 1 com um pagamento inferior ao devido (leitor 1010 fez o pagamento incorreto)
INSERT INTO factura (IdFactura, Nif, Valor, Mes, Ano) VALUES
(1, 1001, 2, 1, 2024),
(2, 1001, 2, 2, 2024),
(3, 1002, 10, 1, 2024),
(4, 1002, 10, 2, 2024),
(5, 1003, 10, 1, 2024),
(6, 1003, 10, 2, 2024),
(7, 1004, 10, 1, 2024),
(8, 1004, 10, 2, 2024),
(9, 1005, 10, 1, 2024),
(10, 1005, 10, 2, 2024),
(11, 1006, 13, 1, 2024),
(12, 1006, 13, 2, 2024),
(13, 1007, 13, 1, 2024),
(14, 1007, 13, 2, 2024),
(15, 1008, 13, 1, 2024),
(16, 1008, 13, 2, 2024),
(17, 1009, 13, 1, 2024),
(18, 1009, 13, 2, 2024),
-- O leitor 1010 devia ter pago 13€ em cada mes e apenas pagou 10€
(19, 1010, 10, 1, 2024),
(20, 1010, 10, 2, 2024);

-- Inserção de 3 avaliaçõoes por cada leitor de forma a que todos os livros tenham pelo menos uma avaliação (o livro 10010 não tem avaliações)
INSERT INTO avaliacao (Nif, ISBN, Estrelas, Comentario) VALUES
-- Avaliações do leitor 1001
(1001, 10001, 4, 'Ótimo livro!'),
(1001, 10002, 5, 'Incrível e emocionante.'),
(1001, 10003, 3, 'Bastante interessante.'),
-- Avaliações do leitor 1002
(1002, 10004, 5, 'Fantástico, recomendo!'),
(1002, 10005, 2, 'Pouco envolvente.'),
(1002, 10006, 4, 'Uma leitura agradável.'),
-- Avaliações do leitor 1003
(1003, 10007, 3, 'Bom, mas não excepcional.'),
(1003, 10008, 5, 'Adorei cada página!'),
(1003, 10009, 4, 'Uma história fascinante.'),
-- Avaliações do leitor 1004
(1004, 10001, 2, 'Não foi do meu agrado.'),
(1004, 10003, 4, 'Muito bem escrito.'),
(1004, 10004, 5, 'Uma obra-prima!'),
-- Avaliações do leitor 1005
(1005, 10002, 3, 'Interessante, mas um pouco longo.'),
(1005, 10005, 4, 'Gostei bastante.'),
(1005, 10006, 5, 'Simplesmente maravilhoso!'),
-- Avaliações do leitor 1006
(1006, 10007, 2, 'Esperava mais.'),
(1006, 10008, 3, 'Boa leitura.'),
(1006, 10009, 5, 'Muito envolvente.'),
-- Avaliações do leitor 1007
(1007, 10001, 5, 'Excelente leitura!'),
(1007, 10002, 4, 'Bem interessante.'),
(1007, 10003, 3, 'Achei mediano.'),
-- Avaliações do leitor 1008
(1008, 10004, 5, 'Uma obra incrível!'),
(1008, 10005, 3, 'Uma leitura ok.'),
(1008, 10006, 4, 'Gostei da narrativa.'),
-- Avaliações do leitor 1009
(1009, 10007, 5, 'Um dos melhores que já li!'),
(1009, 10008, 4, 'Muito bom!'),
(1009, 10009, 3, 'Esperava algo mais profundo.'),
-- Avaliações do leitor 1010
(1010, 10001, 4, 'Um clássico excelente.'),
(1010, 10002, 2, 'Não me agradou muito.'),
(1010, 10003, 5, 'Simplesmente perfeito!');

-- Amizades: o leitor 1001 é amigo de todos. O leitor 1010 não tem nenhum amigo. Todos os outros teem 2 ou mais amigos
INSERT INTO amigos (NIF1, NIF2) VALUES
-- Leitor 1001 é amigo de todos os outros
(1001, 1002),
(1001, 1003),
(1001, 1004),
(1001, 1005),
(1001, 1006),
(1001, 1007),
(1001, 1008),
(1001, 1009),
-- Relações simétricas para 1001
(1002, 1001),
(1003, 1001),
(1004, 1001),
(1005, 1001),
(1006, 1001),
(1007, 1001),
(1008, 1001),
(1009, 1001),
-- Amizades dos leitores 1002 a 1009 (com pelo menos 2 amigos cada)
-- Leitor 1002
(1002, 1003),
(1002, 1004),
(1003, 1002),
(1004, 1002),
-- Leitor 1003
(1003, 1005),
(1003, 1006),
(1005, 1003),
(1006, 1003),
-- Leitor 1004
(1004, 1006),
(1004, 1007),
(1006, 1004),
(1007, 1004),
-- Leitor 1005
(1005, 1007),
(1005, 1008),
(1007, 1005),
(1008, 1005),
-- Leitor 1006
(1006, 1008),
(1006, 1009),
(1008, 1006),
(1009, 1006),
-- Leitor 1007
(1007, 1008),
(1007, 1009),
(1008, 1007),
(1009, 1007),
-- Leitor 1008
(1008, 1009),
(1009, 1008);

-- Encomendas: Inserção de 2 encomendas a duas editoras: uma com 2 obras e 2 exemplares de uma e 3 de outra. Outra com uma obra e um exemplar.
INSERT INTO encomenda (IdEncomenda, DataEnc, Valor, NifEditora) VALUES
(1, '2024-01-3', 250, 101),
(2, '2024-01-5', 50, 102);
INSERT INTO encomendou (IdExemplar, ISBN, IdEncomenda) VALUES
(01, 10001, 1),
(03, 10001, 1),
(10, 10004, 1),
(11, 10004, 1),
(12, 10004, 1),
(16, 10006, 2);

-- Inserção extra de uma encomenda para ter resultado na alínea e)
INSERT INTO encomenda (IdEncomenda, DataEnc, Valor, NifEditora) VALUES
(3, '2024-01-17', 50, 102);
INSERT INTO encomendou (IdExemplar, ISBN, IdEncomenda) VALUES
(19, 10007, 3);
