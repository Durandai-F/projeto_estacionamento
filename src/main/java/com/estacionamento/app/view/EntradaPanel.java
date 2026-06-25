package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.model.Ticket;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class EntradaPanel extends JPanel {
    private JTextField placaField;
    private JComboBox<String> categoriaComboBox;
    private JButton registrarButton;
    private MySQLTicketDAO ticketDAO;

    public EntradaPanel(MySQLTicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JLabel placaLabel = new JLabel("Placa:");
        placaField = new JTextField(10);
        JLabel categoriaLabel = new JLabel("Categoria:");
        categoriaComboBox = new JComboBox<>(new String[]{"CARRO", "MOTO"});
        registrarButton = new JButton("Registrar Entrada");

        // Adicionar componentes ao painel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(placaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(placaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(categoriaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(categoriaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(registrarButton, gbc);

        // Ação do botão Registrar Entrada
        registrarButton.addActionListener(e -> registrarEntrada());
    }

    private void registrarEntrada() {
        String placa = placaField.getText().trim().toUpperCase();
        String categoria = (String) categoriaComboBox.getSelectedItem();

        if (placa.isEmpty() || placa.length() < 7) {
            JOptionPane.showMessageDialog(this, "Placa inválida. Deve conter no mínimo 7 caracteres.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Bloquear Duplicidade (RF03)
        if (ticketDAO.buscarAtivoPorPlaca(placa) != null) {
            JOptionPane.showMessageDialog(this, "Veículo com esta placa já está no pátio.", "Erro de Duplicidade", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ticket novoTicket = new Ticket(placa, categoria, LocalDateTime.now());
        ticketDAO.salvar(novoTicket);
        JOptionPane.showMessageDialog(this, "Entrada registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        placaField.setText("");
        // Notificar o painel de monitoramento para atualizar
        // Isso será feito de forma mais robusta com um Observer Pattern ou similar, por enquanto, apenas um placeholder
    }
}
