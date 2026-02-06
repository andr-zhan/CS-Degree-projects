-- Tabela membro
CREATE TABLE membro (
    Nome VARCHAR(50) NOT NULL,
    IdMemb VARCHAR(50) PRIMARY KEY,
    Pais VARCHAR(50) NOT NULL,
    DataNasc DATE NOT NULL
);

-- Tabela doce
CREATE TABLE doce (
    Nome VARCHAR(100) PRIMARY KEY,
    Descricao TEXT NOT NULL,
    Genero VARCHAR(50) NOT NULL
);

-- Tabela amigo (relações simétricas de amizade)
CREATE TABLE amigo (
    Membro1 VARCHAR(50) NOT NULL,
    Membro2 VARCHAR(50) NOT NULL,
    PRIMARY KEY (Membro1, Membro2),
    FOREIGN KEY (Membro1) REFERENCES membro(IdMemb),
    FOREIGN KEY (Membro2) REFERENCES membro(IdMemb),
    CHECK (Membro1 <> Membro2)
);

-- Tabela criou
CREATE TABLE criou (
    Membro VARCHAR(50) NOT NULL,
    Doce VARCHAR(100) NOT NULL,
    PRIMARY KEY (Membro, Doce),
    FOREIGN KEY (Membro) REFERENCES membro(IdMemb),
    FOREIGN KEY (Doce) REFERENCES doce(Nome)
);

-- Tabela fez
CREATE TABLE fez (
    Membro VARCHAR(50) NOT NULL,
    Doce VARCHAR(100) NOT NULL,
    Tempo INT CHECK (Tempo BETWEEN 1 AND 5) NOT NULL,
    Aspeto INT CHECK (Aspeto BETWEEN 1 AND 5) NOT NULL,
    Sabor INT CHECK (Sabor BETWEEN 1 AND 5) NOT NULL,
    PRIMARY KEY (Membro, Doce),
    FOREIGN KEY (Membro) REFERENCES membro(IdMemb),
    FOREIGN KEY (Doce) REFERENCES doce(Nome)
);

-- Tabela ingrediente
CREATE TABLE ingrediente (
    Nome VARCHAR(100) PRIMARY KEY,
    Custo DECIMAL(10, 2) NOT NULL
);

-- Tabela temIngrediente
CREATE TABLE temIngrediente (
    Doce VARCHAR(100) NOT NULL,
    Ingrediente VARCHAR(100) NOT NULL,
    Quantidade INT NOT NULL,
    PRIMARY KEY (Doce, Ingrediente),
    FOREIGN KEY (Doce) REFERENCES doce(Nome),
    FOREIGN KEY (Ingrediente) REFERENCES ingrediente(Nome)
);