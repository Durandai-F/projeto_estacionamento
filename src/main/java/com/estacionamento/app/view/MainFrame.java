package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;

import javax.swing.*;
import java.awt.*;

import com.estacionamento.app.model.User;

public class MainFrame extends JFrame {
    private MySQLTicketDAO ticketDAO;
    private User loggedInUser;

    public MainFrame(User user) {
        this.loggedInUser = user;
        ticketDAO = new MySQLTicketDAO();
        setTitle("Sistema de Controle de Estacionamento Rotativo - " + user.getUsername() + " (" + user.getRole() + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        JTabbedPane tabbedPane = new JTabbedPane();

        // Adicionar painéis para cada funcionalidade
        tabbedPane.addTab("Registrar Entrada", new EntradaPanel(ticketDAO));
        tabbedPane.addTab("Registrar Saída / Pagamento", new SaidaPanel(ticketDAO));
        tabbedPane.addTab("Monitorar Pátio", new MonitoramentoPanel(ticketDAO));

        // A aba de Histórico e Faturamento é apenas para ADMIN
        if (loggedInUser.isAdmin()) {
            tabbedPane.addTab("Histórico e Faturamento", new HistoricoPanel(ticketDAO));
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
