CREATE TABLE categoria (
  id SERIAL PRIMARY KEY UNIQUE,
  nome VARCHAR(255) NOT NULL,
  ativo BOOLEAN NOT NULL
);