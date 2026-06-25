package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.model.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonitoramentoPanel extends JPanel {
    private JTable tabelaAtivos;
    private DefaultTableModel tableModel;
    private MySQLTicketDAO ticketDAO;

    public MonitoramentoPanel(MySQLTicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new BorderLayout());

        // Configurar a tabela
        String[] colunas = {"Placa", "Categoria", "Hora Entrada"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        tabelaAtivos = new JTable(tableModel);
        add(new JScrollPane(tabelaAtivos), BorderLayout.CENTER);

        // Botão para atualizar a lista
        JButton atualizarButton = new JButton("Atualizar Lista");
        atualizarButton.addActionListener(e -> atualizarTabelaAtivos());
        JPanel southPanel = new JPanel();
        southPanel.add(atualizarButton);
        add(southPanel, BorderLayout.SOUTH);

        atualizarTabelaAtivos(); // Carrega os dados iniciais
    }

    public void atualizarTabelaAtivos() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Ticket> ativos = ticketDAO.listarAtivos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Ticket ticket : ativos) {
            tableModel.addRow(new Object[]{
                    ticket.getPlaca(),
                    ticket.getCategoria(),
                    ticket.getHoraEntrada().format(formatter)
            });
        }
    }
}
