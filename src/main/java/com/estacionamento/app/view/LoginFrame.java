package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private MySQLTicketDAO ticketDAO;

    public LoginFrame() {
        ticketDAO = new MySQLTicketDAO();
        setTitle("Login - Sistema de Estacionamento");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Usuário:");
        usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Senha:");
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Entrar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> performLogin());

        add(panel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User authenticatedUser = ticketDAO.authenticateUser(username, password);

        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            MainFrame mainFrame = new MainFrame(authenticatedUser);
            mainFrame.setVisible(true);
            dispose(); // Fecha a janela de login
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
