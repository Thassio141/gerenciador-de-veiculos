CREATE TABLE IF NOT EXISTS veiculos (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    preco DECIMAL(15,2) NOT NULL,
    quantidade_portas INT,
    tipo_combustivel VARCHAR(20),
    cilindrada INT
);