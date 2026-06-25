CREATE DATABASE IF NOT EXISTS estacionamento;
USE estacionamento;

CREATE TABLE IF NOT EXISTS tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    horaEntrada DATETIME NOT NULL,
    horaSaida DATETIME,
    valorPago DECIMAL(10, 2),
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT IGNORE INTO users (username, password, role) VALUES ('atendente', '12345', 'ATENDENTE');
