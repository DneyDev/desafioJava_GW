CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(2) NOT NULL DEFAULT 'PF' CHECK (tipo IN ('PF', 'PJ')),
    documento VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    CHECK ((tipo = 'PF' AND length(documento) = 11) OR (tipo = 'PJ' AND length(documento) = 14))
);
CREATE TABLE enderecos (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
    rua VARCHAR(255) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep CHAR(8) NOT NULL
);
CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao varchar(255) NOT NULL,
    preco NUMERIC(10,2) NOT NULL CHECK (preco > 0),
    peso NUMERIC(8,3) NOT NULL CHECK (peso > 0),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE RESTRICT,
    origem_rua VARCHAR(255) NOT NULL,
    origem_numero VARCHAR(10) NOT NULL,
    origem_cidade VARCHAR(100) NOT NULL,
    origem_estado CHAR(2) NOT NULL,
    origem_cep CHAR(8) NOT NULL,
    endereco_destino_id INT NOT NULL REFERENCES enderecos(id) ON DELETE RESTRICT,
    codigo_rastreio CHAR(8) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'Pendente'
        CHECK (status IN ('Pendente', 'Em transito', 'Entregue', 'Cancelada'))
);
CREATE TABLE itens_entrega (
    entrega_id INT NOT NULL REFERENCES entregas(id) ON DELETE CASCADE,
    produto_id INT NOT NULL REFERENCES produtos(id) ON DELETE RESTRICT,
    quantidade INT NOT NULL DEFAULT 1 CHECK (quantidade > 0),
    preco_unitario NUMERIC(10,2) NOT NULL CHECK (preco_unitario > 0),
    PRIMARY KEY (entrega_id, produto_id)
);