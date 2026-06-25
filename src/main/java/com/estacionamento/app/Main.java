package com.estacionamento.app;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Agora usamos o MySQLTicketDAO no padrão profissional
        @SuppressWarnings("unused")
        MySQLTicketDAO dao = new MySQLTicketDAO();
        
        // Os usuários padrão devem ser inseridos via script SQL no MySQL Workbench
        // Mas mantemos a chamada se quiser garantir via código (opcional)
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
