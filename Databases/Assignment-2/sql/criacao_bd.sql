-- Tabela leitor
CREATE TABLE leitor (
  Nome VARCHAR(50) NOT NULL,
  Nif INT primary key,
  Email VARCHAR(50) NOT NULL,
  Nacionalidade VARCHAR(50) NOT NULL,
  Tipo VARCHAR(20) NOT NULL
);

-- Tabela autor
CREATE TABLE autor (
  Nome VARCHAR(50) NOT NULL,
  CodA INT PRIMARY KEY,
  Nacionalidade VARCHAR(50) NOT NULL
);

-- Tabela editora
CREATE TABLE editora (
  Nome VARCHAR(50) NOT NULL,
  NifEditora INT PRIMARY KEY,
  Email VARCHAR(100) NOT NULL
);

-- Tabela livro
CREATE TABLE livro (
  Titulo VARCHAR(50) NOT NULL,
  ISBN INT PRIMARY KEY,
  NifEditora INT NOT NULL,
  FOREIGN KEY (NifEditora) REFERENCES editora(NifEditora)
);

-- Tabela exemplar 
CREATE TABLE exemplar (
  IdExemplar INT PRIMARY KEY,
  ISBN INT NOT NULL,
  LinkDownload VARCHAR(100) NOT NULL,
  FOREIGN KEY (ISBN) REFERENCES livro(ISBN)
);

-- Tabela livro_gen
CREATE TABLE livro_gen (
  ISBN INT NOT NULL,
  Genero VARCHAR(50) NOT NULL,
  PRIMARY KEY (ISBN, Genero),
  FOREIGN KEY (ISBN) REFERENCES livro(ISBN)
);

-- Tabela autoria
CREATE TABLE autoria (
  ISBN INT NOT NULL,
  CodA INT NOT NULL,
  PRIMARY KEY (ISBN, CodA),
  FOREIGN KEY (ISBN) REFERENCES livro(ISBN),
  FOREIGN KEY (CodA) REFERENCES autor(CodA)
);

-- Tabela amigos
CREATE TABLE amigos (
  Nif1 INT NOT NULL,
  Nif2 INT NOT NULL,
  PRIMARY key (Nif1, Nif2),
  FOREIGN KEY (Nif1) REFERENCES leitor(Nif),
  FOREIGN KEY (Nif2) REFERENCES leitor(Nif),
  CHECK (Nif1 <> Nif2)
);

-- Tabela avaliacao
CREATE TABLE avaliacao (
  Nif INT NOT NULL,
  ISBN INT NOT NULL,
  Estrelas INT CHECK (Estrelas BETWEEN 0 AND 5) NOT NULL,
  Comentario VARCHAR(100) NOT NULL,
  PRIMARY KEY (Nif, ISBN),
  FOREIGN key (Nif) REFERENCES leitor(Nif),
  FOREIGN key (ISBN) REFERENCES livro(ISBN)
);

-- Tabela encomenda 
CREATE TABLE encomenda (
  IdEncomenda INT primary key,
  DataEnc DATE NOT NULL,
  Valor INT NOT NULL,
  NifEditora INT NOT NULL,
  FOREIGN key (NifEditora) REFERENCES editora(NifEditora)
);

-- Tabela encomendou
CREATE TABLE encomendou (
  IdExemplar INT NOT NULL,
  ISBN INT NOT NULL,
  IdEncomenda INT NOT NULL,
  PRIMARY key (IdExemplar, ISBN, IdEncomenda),
  FOREIGN key (IdExemplar) REFERENCES exemplar(IdExemplar),
  FOREIGN key (ISBN) REFERENCES livro(ISBN),
  FOREIGN key (IdEncomenda) REFERENCES encomenda(IdEncomenda)
);

-- Tabela factura 
CREATE TABLE factura (
  IdFactura INT PRIMARY KEY,
  Nif INT NOT NULL,
  Valor INT NOT NULL,
  Mes INT CHECK (Mes BETWEEN 1 AND 12) NOT NULL,
  Ano INT NOT NULL,
  FOREIGN key (Nif) REFERENCES leitor(Nif)
);

-- Tabela aluguer 
CREATE TABLE aluguer (
  IdAluguer INT PRIMARY KEY,
  Semana INT CHECK (Semana BETWEEN 1 AND 52) NOT NULL,
  Ano INT NOT NULL,
  Valor INT NOT NULL,
  Nif INT NOT NULL,
  IdExemplar INT NOT NULL,
  FOREIGN key (Nif) REFERENCES leitor(Nif),
  FOREIGN key (IdExemplar) REFERENCES exemplar(IdExemplar)
);