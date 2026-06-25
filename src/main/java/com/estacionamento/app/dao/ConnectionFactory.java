package com.estacionamento.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe ConnectionFactory - Padrão Profissional (HealthTrack)
 * Centraliza a conexão com o banco de dados MySQL.
 */
public class ConnectionFactory {
    
    // Configurações do Banco de Dados
    private static final String SERVER = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "estacionamento";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // <--- COLOQUE SUA SENHA DO MYSQL AQUI

    // URL de Conexão formatada
    private static final String URL = "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DATABASE + "?useTimezone=true&serverTimezone=UTC&useSSL=false";

    public static Connection getConnection() {
        try {
            // Registra o driver explicitamente (necessário para a pasta lib)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO: Driver do MySQL não encontrado na pasta lib!");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("ERRO: Não foi possível conectar ao MySQL. Verifique se o servidor está rodando e se a senha está correta.");
            System.err.println("Detalhes: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
