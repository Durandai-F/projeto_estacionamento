package com.estacionamento.app.dao;

import com.estacionamento.app.model.User;
import com.estacionamento.app.model.Ticket;
import com.estacionamento.app.model.Veiculo;
import com.estacionamento.app.model.Carro;
import com.estacionamento.app.model.Moto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MySQLTicketDAO implements TicketDAO {

    public MySQLTicketDAO() {
        // As tabelas devem ser criadas via script SQL no MySQL Workbench
    }

    @Override
    public void salvar(Ticket ticket) {
        String sql = "INSERT INTO tickets (placa, categoria, horaEntrada, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            // Especificamos explicitamente que queremos as chaves geradas (ID AUTO_INCREMENT)
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, ticket.getPlaca());
            pstmt.setString(2, ticket.getCategoria());
            pstmt.setTimestamp(3, Timestamp.valueOf(ticket.getHoraEntrada()));
            pstmt.setString(4, ticket.getStatus());
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar ticket: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fechamento manual para garantir compatibilidade total
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void atualizar(Ticket ticket) {
        String sql = "UPDATE tickets SET horaSaida = ?, valorPago = ?, status = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(ticket.getHoraSaida()));
            pstmt.setDouble(2, ticket.getValorPago());
            pstmt.setString(3, ticket.getStatus());
            pstmt.setInt(4, ticket.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ticket buscarAtivoPorPlaca(String placa) {
        String sql = "SELECT * FROM tickets WHERE placa = ? AND status = 'ATIVO'";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTicket(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ticket> listarHistorico() {
        List<Ticket> historico = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = 'CONCLUIDO' ORDER BY horaEntrada DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                historico.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historico;
    }

    @Override
    public List<Ticket> listarAtivos() {
        List<Ticket> ativos = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = 'ATIVO' ORDER BY horaEntrada ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ativos.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ativos;
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String placa = rs.getString("placa");
        String categoria = rs.getString("categoria");
        LocalDateTime entrada = rs.getTimestamp("horaEntrada").toLocalDateTime();
        
        Timestamp saidaTS = rs.getTimestamp("horaSaida");
        LocalDateTime saida = (saidaTS != null) ? saidaTS.toLocalDateTime() : null;
        
        double valor = rs.getDouble("valorPago");
        String status = rs.getString("status");

        return new Ticket(id, placa, categoria, entrada, saida, valor, status);
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = ConnectionFactory.getConnection();
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
            e.printStackTrace();
        }
        return null;
    }
}
