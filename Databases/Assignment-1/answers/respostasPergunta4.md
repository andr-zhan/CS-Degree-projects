-- alínea 4.a)
WITH doces AS (
    SELECT Doce
    FROM temingrediente, doce
    WHERE Ingrediente = 'Canela' AND Genero = 'Regional' AND doce.Nome =
temIngrediente.Doce
INTERSECT 
    SELECT Doce
    FROM temIngrediente, doce
    WHERE Ingrediente = 'Chocolate' AND Genero = 'Regional' AND doce.Nome =
temIngrediente.Doce
)
SELECT DISTINCT nome
FROM membro, (criou NATURAL INNER JOIN doces) AS s
WHERE Pais = 'Espanha' AND membro.IdMemb = s.Membro


-- alínea 4.b)
SELECT DISTINCT d.Nome
FROM doce d
JOIN criou c ON d.Nome = c.Doce
JOIN amigo a ON c.Membro = a.Membro2
JOIN fez f ON d.Nome = f.Doce
WHERE a.Membro1 = 'idjoaquimjose'
  AND d.Genero = 'Tradicional'
  AND f.Aspeto = 5;


-- alínea 4.c)
SELECT DISTINCT m.Nome
FROM membro m
WHERE NOT EXISTS (
    SELECT 1
    FROM fez f
    WHERE f.Membro = m.IdMemb
      AND NOT EXISTS (
          SELECT 1
          FROM amigo a
          JOIN criou c ON c.Membro = a.Membro2
          WHERE a.Membro1 = m.IdMemb AND c.Doce = f.Doce
       )
);


-- alínea 4.d)
SELECT DISTINCT d.Nome
FROM doce d
JOIN temingrediente ti ON d.Nome = ti.Doce
JOIN fez f ON d.Nome = f.Doce
WHERE ti.Ingrediente = 'Natas'
  AND f.Sabor < 5;


-- alínea 4.e)
SELECT DISTINCT c.Membro
FROM criou c
JOIN temingrediente ti1 ON c.Doce = ti1.Doce
JOIN temingrediente ti2 ON c.Doce = ti2.Doce
WHERE ti1.Ingrediente = 'Canela'
  AND ti2.Ingrediente = 'Leite';


-- alínea 4.f)
SELECT DISTINCT d.Nome
FROM doce d
JOIN temingrediente ti ON d.Nome = ti.Doce
WHERE ti.Ingrediente = 'Maçã' OR ti.Ingrediente = 'Baunilha'


-- alínea 4.g)
SELECT SUM(ti.Quantidade * i.Custo) AS Custo_Arroz_Doce
FROM temingrediente ti
JOIN ingrediente i ON ti.Ingrediente = i.Nome
WHERE ti.Doce = 'Arroz Doce';


-- alínea 4.h)
SELECT d.Nome, SUM(ti.Quantidade * i.Custo) AS Custo
FROM doce d
JOIN temingrediente ti ON d.Nome = ti.Doce
JOIN ingrediente i ON ti.Ingrediente = i.Nome
GROUP BY d.Nome;


-- alínea 4.i)
SELECT c.Membro, COUNT(c.Doce) AS Numero_Doces
FROM criou c
GROUP BY c.Membro;


-- alínea 4.j)
WITH r AS (
  SELECT membro.nome, COUNT(criou.doce) AS num_doces
  FROM membro, criou
  WHERE membro.IdMemb = criou.membro
  GROUP BY membro.nome
)
SELECT nome
FROM r
WHERE num_doces = (SELECT MAX(num_doces)
                                  FROM r);


-- alínea 4.k)
WITH Custo_Doce AS (
    SELECT d.Nome, SUM(ti.Quantidade * i.Custo) AS Custo
    FROM doce d
    JOIN temingrediente ti ON d.Nome = ti.Doce
    JOIN ingrediente i ON ti.Ingrediente = i.Nome
    GROUP BY d.Nome
)
SELECT DISTINCT m.Nome
FROM fez f
JOIN membro m ON f.Membro = m.IdMemb
JOIN Custo_Doce cd ON f.Doce = cd.Nome
WHERE cd.Custo = (SELECT MAX(Custo) FROM Custo_Doce);


-- alínea 4.l)
SELECT m1.IdMemb
FROM membro m1
WHERE NOT EXISTS (
    SELECT *
    FROM amigo a1
    WHERE a1.Membro1 = 'idjoaquimjose'
      AND NOT EXISTS (
          SELECT *
          FROM amigo a2
          WHERE a2.Membro1 = m1.IdMemb
            AND a2.Membro2 = a1.Membro2
      )
);


-- alínea 4.m)
SELECT m.Nome
FROM membro m
WHERE NOT EXISTS (
    SELECT *
    FROM temingrediente ti
    WHERE ti.Ingrediente = 'Baunilha'
      AND NOT EXISTS (
          SELECT *
          FROM fez f
          WHERE f.Membro = m.IdMemb
            AND f.Doce = ti.Doce
      )
);


-- alínea 4.n)
SELECT f.Doce
FROM fez f
GROUP BY f.Doce
HAVING MIN(f.Tempo) = 1 AND MAX(f.Tempo) = 1;


-- alínea 4.o)
SELECT Doce
FROM (
SELECT t.Doce, SUM(i.Custo * t.Quantidade) AS CustoTotal
FROM temIngrediente t
JOIN ingrediente i ON t.Ingrediente = i.Nome
GROUP BY t.Doce
HAVING SUM(i.Custo * t.Quantidade) = (
    SELECT MIN(CustoTotal)
    FROM (
        SELECT SUM(i.Custo * t.Quantidade) AS CustoTotal
        FROM temIngrediente t
        JOIN ingrediente i ON t.Ingrediente = i.Nome
        GROUP BY t.Doce
    ) AS Custos
)
)
INTERSECT
SELECT Doce
FROM fez
GROUP BY Doce
HAVING AVG(Tempo) = (
    SELECT MIN(TempoMedio)
    FROM (
        SELECT AVG(Tempo) AS TempoMedio
        FROM fez
        GROUP BY Doce
    ) AS Tempos
);

