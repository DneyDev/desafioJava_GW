CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE enderecos (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL UNIQUE
        REFERENCES clientes(id) ON DELETE CASCADE, --CASCADE para se o Cliente for deletado os dados irem juntos*
    rua VARCHAR(255) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep CHAR(8) NOT NULL
);
CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    preco NUMERIC(10,2) NOT NULL CHECK (preco > 0), -- check para não deixar a validação apenas no Java
    peso NUMERIC(8,3) NOT NULL
);
CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL
        REFERENCES clientes(id) ON DELETE RESTRICT, --impede de apagar cliente pois terá uma entrega vinculada**
    endereco_id INT NOT NULL
        REFERENCES enderecos(id) ON DELETE RESTRICT, --    **
    codigo_rastreio CHAR(8) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'Pendente'
);
CREATE TABLE itens_entrega (
    entrega_id INT NOT NULL
        REFERENCES entregas(id) ON DELETE CASCADE, -- *
    produto_id INT NOT NULL
        REFERENCES produtos(id) ON DELETE RESTRICT, --impede apagar o produto pois é um registro da transportadora
    quantidade INT NOT NULL DEFAULT 1 CHECK (quantidade > 0),
    preco_unitario NUMERIC(10,2) NOT NULL CHECK (preco_unitario > 0),
    PRIMARY KEY (entrega_id, produto_id)
);