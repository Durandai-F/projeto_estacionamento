package com.estacionamento.app.dao;

import com.estacionamento.app.model.User;

import com.estacionamento.app.model.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTicketDAO implements TicketDAO {
    private static final String DB_URL = "jdbc:sqlite:estacionamento.db";

    public SQLiteTicketDAO() {
        createTableTickets();
        createTableUsers();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createTableTickets() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    placa TEXT NOT NULL,\n" +
                "    categoria TEXT NOT NULL,\n" +
                "    horaEntrada TEXT NOT NULL,\n" +
                "    horaSaida TEXT,\n" +
                "    valorPago REAL,\n" +
                "    status TEXT NOT NULL\n" +
                ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTableUsers() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    username TEXT NOT NULL UNIQUE,\n" +
                "    password TEXT NOT NULL,\n" +
                "    role TEXT NOT NULL\n" +
                ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void salvar(Ticket ticket) {
        String sql = "INSERT INTO tickets(placa, categoria, horaEntrada, status) VALUES(?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getPlaca());
            pstmt.setString(2, ticket.getCategoria());
            pstmt.setString(3, ticket.getHoraEntrada().toString());
            pstmt.setString(4, ticket.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void atualizar(Ticket ticket) {
        String sql = "UPDATE tickets SET horaSaida = ?, valorPago = ?, status = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getHoraSaida().toString());
            pstmt.setDouble(2, ticket.getValorPago());
            pstmt.setString(3, ticket.getStatus());
            pstmt.setInt(4, ticket.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Ticket buscarAtivoPorPlaca(String placa) {
        String sql = "SELECT * FROM tickets WHERE placa = ? AND status = 'ATIVO'";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Ticket(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("categoria"),
                        LocalDateTime.parse(rs.getString("horaEntrada")),
                        null, // horaSaida será null para tickets ATIVOS
                        0.0,  // valorPago será 0.0 para tickets ATIVOS
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Ticket> listarHistorico() {
        List<Ticket> historico = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = 'CONCLUIDO' ORDER BY horaEntrada DESC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                historico.add(new Ticket(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("categoria"),
                        LocalDateTime.parse(rs.getString("horaEntrada")),
                        LocalDateTime.parse(rs.getString("horaSaida")),
                        rs.getDouble("valorPago"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return historico;
    }

    @Override
    public List<Ticket> listarAtivos() {
        List<Ticket> ativos = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = 'ATIVO' ORDER BY horaEntrada ASC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ativos.add(new Ticket(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("categoria"),
                        LocalDateTime.parse(rs.getString("horaEntrada")),
                        null,
                        0.0,
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ativos;
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Em uma aplicação real, a senha deve ser hash
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addDefaultUsers() {
        if (authenticateUser("admin", "admin123") == null) {
            saveUser(new User("admin", "admin123", "ADMIN"));
            System.out.println("Usuário admin padrão adicionado.");
        }
        if (authenticateUser("atendente", "12345") == null) {
            saveUser(new User("atendente", "12345", "ATENDENTE"));
            System.out.println("Usuário atendente padrão adicionado.");
        }
    }
}
