CREATE TABLE produto (
  id SERIAL PRIMARY KEY UNIQUE,
  nome VARCHAR(255) NOT NULL,
  preco Float NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  foto VARCHAR(255)
);