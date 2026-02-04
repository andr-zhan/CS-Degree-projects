-- Apagar tabelas antigas, se existirem (útil para testes)
DROP TABLE IF EXISTS emprestimos;
DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS utilizadores;

-- ==========================
-- Tabela: Utilizadores
-- ==========================
CREATE TABLE utilizadores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    data_nascimento DATE,
    estado_operacional VARCHAR(20)
        CHECK (estado_operacional IN ('aguarda_aprovacao', 'ativo', 'suspenso', 'aguarda_suspensao', 'bloqueado'))
        DEFAULT 'aguarda_aprovacao',
    estado_admin VARCHAR(20)
        CHECK (estado_admin IN ('aprovado', 'nao_aprovado'))
        DEFAULT 'nao_aprovado',
    motivo_suspensao VARCHAR(255),
    justificacao_remocao VARCHAR(255),
    suspensoes_count INT DEFAULT 0
);

-- ==========================
-- Tabela: Livros
-- ==========================
CREATE TABLE livros (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    estado_operacional VARCHAR(20)
        CHECK (estado_operacional IN ('aguarda_aprovacao', 'disponivel', 'emprestado', 'manutencao'))
        DEFAULT 'aguarda_aprovacao',
    estado_admin VARCHAR(20)
        CHECK (estado_admin IN ('aprovado', 'nao_aprovado'))
        DEFAULT 'nao_aprovado'
);

-- ==========================
-- Tabela: Empréstimos
-- ==========================
CREATE TABLE emprestimos (
    id SERIAL PRIMARY KEY,
    utilizador_id INT REFERENCES utilizadores(id),
    livro_id INT REFERENCES livros(id),
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,
    estado VARCHAR(20)
        CHECK (estado IN ('ativo', 'concluido', 'devolucao_pendente', 'suspenso'))
        DEFAULT 'ativo',
    motivo_negacao VARCHAR(255)
);

-- (removido: agora usamos o estado_operacional = 'aguarda_suspensao' e a coluna justificacao_remocao na tabela utilizadores)
