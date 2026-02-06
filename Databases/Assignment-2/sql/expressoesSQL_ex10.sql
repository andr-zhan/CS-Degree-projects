-- a)
SELECT Nome
FROM leitor
WHERE Tipo = 'regular';



-- b)
WITH l_gen as (
  select Titulo, livro.ISBN, Genero
  from livro natural inner join livro_gen
),
autores as (
  select ISBN, Nome
  from autor natural inner join autoria
)
select Genero, Titulo, Nome
from l_gen natural inner join autores;



-- c)
SELECT Genero, COUNT(ISBN)
from livro_gen
GROUP by Genero;



-- d)
with l_amigos as (
  select NIF, NIF2
  from leitor, amigos
  where Tipo = 'frequente' and NIF = NIF1
)
SELECT NIF, COUNT(NIF2)
FROM l_amigos
group by NIF;



-- e)
SELECT e.ISBN, COUNT(en.IdExemplar) AS NumeroDeExemplares
FROM encomenda enc
JOIN encomendou en ON enc.IdEncomenda = en.IdEncomenda
JOIN exemplar e ON en.IdExemplar = e.IdExemplar
WHERE EXTRACT(WEEK FROM enc.DataEnc) = 3 AND EXTRACT(YEAR FROM enc.DataEnc) = 2024
GROUP BY e.ISBN;



-- f)
with alug_exemp as (
  select Nif, ISBN, Semana, Ano, Valor
  from aluguer natural inner join exemplar
)
select Nif, Titulo, Semana, Ano, Valor 
from alug_exemp NATURAL INNER JOIN livro;



-- g)
select ISBN, Nif, Semana, Ano, Valor
from aluguer NATURAL INNER join exemplar;



--h)
WITH AlugueresAteSemana4 AS (
  SELECT Nif, SUM(Valor) AS ValorDivida
  	FROM aluguer
  	WHERE Semana <= 4
  	GROUP BY Nif),
PagamentosAteSemana4 AS (
  	SELECT Nif, SUM(Valor) AS ValorPago
 	 FROM factura
  	WHERE (Ano = 2024 AND Mes <= 1)
  	GROUP BY Nif)
SELECT
-- COALESCE para preencher valores nulos com 0
  COALESCE(a.Nif, p.Nif) AS Nif, 
  COALESCE(ValorDivida, 0) AS ValorDivida, 
  COALESCE(ValorPago, 0) AS ValorPago
FROM 
  AlugueresAteSemana4 a
FULL OUTER JOIN PagamentosAteSemana4 p
ON a.Nif = p.Nif;



-- i)
select NIF, Estrelas, Comentario
from avaliacao natural inner join livro
where Titulo = 'Memorial do Convento';



-- j)
WITH misterio AS (
  SELECT ISBN
  FROM avaliacao NATURAL JOIN livro_gen
  WHERE Genero = 'Mistério'
),
fantasia AS (
  SELECT ISBN
  FROM avaliacao NATURAL JOIN livro_gen
  WHERE Genero = 'Fantasia'
),
livros AS (
  SELECT ISBN
  FROM misterio
  INTERSECT
  SELECT ISBN
  FROM fantasia
),
leitores AS (
  SELECT NIF 
  FROM avaliacao NATURAL JOIN livros
)
SELECT DISTINCT a.NIF1
FROM amigos a
JOIN leitores l ON a.NIF2 = l.NIF;



-- k)
with leitor_liv as (
  select Nif, COUNT(IdExemplar) as n 
  from aluguer
  group by Nif
)
Select DISTINCT Nif 
from aluguer natural inner join leitor_liv
where n > 10 and Semana < 5 and Ano = 2024;



-- l)
with liv_est as (
  select ISBN, avg(Estrelas) as n
  from avaliacao
  group by ISBN
)
select ISBN 
from liv_est
where n > 3;



-- m)
with leitores as (
  SELECT leitor.NIF 
  from leitor, aluguer, exemplar, livro 
  where Tipo = 'frequente' and Titulo = 'Memorial do Convento' and leitor.NIF = aluguer.Nif and aluguer.idexemplar = exemplar.IdExemplar and exemplar.ISBN = livro.ISBN 
)
(select NIF 
from leitor 
where Tipo = 'frequente')
except
(select NIF
from leitores);



-- n)
WITH alugueres AS (
  SELECT Nif, IdExemplar 
  FROM aluguer
  WHERE Semana = 4
),
r AS (
  SELECT Nif, COUNT(IdExemplar) AS n
  FROM alugueres
  GROUP BY Nif
)
SELECT Nif, n
FROM r
WHERE n = (SELECT MAX(n) FROM r);



-- o)
WITH alugados AS (
  SELECT ISBN, aluguer.IdExemplar
  FROM aluguer
  NATURAL INNER JOIN exemplar
  WHERE Semana < 9 AND Ano = 2024
),
r AS (
  SELECT ISBN, COUNT(IdExemplar) AS n
  FROM alugados
  GROUP BY ISBN
)
SELECT ISBN, n
FROM r
WHERE n = (SELECT MAX(n) FROM r);



-- p)
SELECT L.NIF
FROM Leitor L
WHERE NOT EXISTS (
    SELECT 1
    FROM (
        -- Subconsulta que seleciona os leitores que alugaram o livro
        SELECT DISTINCT Aluguer.NIF
        FROM Aluguer
        JOIN Exemplar ON Aluguer.IdExemplar = Exemplar.IdExemplar
        WHERE Exemplar.ISBN = (SELECT ISBN FROM Livro WHERE Titulo = 'Memorial do Convento')
    ) Alugadores
    WHERE NOT EXISTS (
        -- Verificar se o leitor L é amigo do leitor da subconsulta
        SELECT 1
        FROM Amigos
        WHERE (Amigos.NIF1 = L.NIF AND Amigos.NIF2 = Alugadores.NIF)
           OR (Amigos.NIF1 = Alugadores.NIF AND Amigos.NIF2 = L.NIF)
    )
);



-- q)
with leitores as (
  select NIF1 
  from amigos
)
(select NIF
from leitor)
EXCEPT
(select NIF1
from leitores);



-- r)
WITH DividaPorLeitor AS (
  	SELECT Nif, SUM(Valor) AS TotalDivida
  	FROM aluguer
  	GROUP BY Nif),
PagamentoPorLeitor AS (
  SELECT Nif, SUM(Valor) AS TotalPago
 	 FROM factura
  GROUP BY Nif),
Comparacao AS (
  SELECT 
  -- COALESCE para preencher valores nulos com 0
    COALESCE(d.Nif, p.Nif) AS Nif, 
    COALESCE(d.TotalDivida, 0) AS TotalDivida, 
    COALESCE(p.TotalPago, 0) AS TotalPago
  FROM DividaPorLeitor d
  FULL OUTER JOIN PagamentoPorLeitor p
  ON d.Nif = p.Nif
)
-- Leitores com pagamentos em dia
SELECT Nif
FROM Comparacao
WHERE TotalPago >= TotalDivida;



-- s)
select ISBN, count(IdExemplar)
from encomendou
group by ISBN;



-- t)
WITH Total_Exemplares AS (
    SELECT Exemplar.ISBN, COUNT(Exemplar.IdExemplar) AS Total
    FROM Exemplar
    GROUP BY Exemplar.ISBN
),
Exemplares_Alugados AS (
    SELECT Exemplar.ISBN, COUNT(DISTINCT Aluguer.IdExemplar) AS Alugados
    FROM Aluguer
    JOIN Exemplar ON Aluguer.IdExemplar = Exemplar.IdExemplar
    GROUP BY Exemplar.ISBN
)
SELECT L.Titulo
FROM Livro L
JOIN Total_Exemplares TE ON L.ISBN = TE.ISBN
JOIN Exemplares_Alugados EA ON L.ISBN = EA.ISBN
WHERE TE.Total = EA.Alugados;
