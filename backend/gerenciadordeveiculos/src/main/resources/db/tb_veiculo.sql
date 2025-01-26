CREATE TABLE IF NOT EXISTS veiculos (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    preco DECIMAL(15, 2) NOT NULL,
    cor VARCHAR(50),
    quantidade_portas INT,
    tipo_combustivel VARCHAR(20),
    cilindrada INT
);
