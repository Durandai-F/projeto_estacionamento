package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.model.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoPanel extends JPanel {
    private JTable tabelaHistorico;
    private DefaultTableModel tableModel;
    private MySQLTicketDAO ticketDAO;

    public HistoricoPanel(MySQLTicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new BorderLayout());

        // Configurar a tabela
        String[] colunas = {"Placa", "Categoria", "Entrada", "Saída", "Valor Pago"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        tabelaHistorico = new JTable(tableModel);
        add(new JScrollPane(tabelaHistorico), BorderLayout.CENTER);

        // Botão para atualizar a lista
        JButton atualizarButton = new JButton("Atualizar Histórico");
        atualizarButton.addActionListener(e -> atualizarTabelaHistorico());
        JPanel southPanel = new JPanel();
        southPanel.add(atualizarButton);
        add(southPanel, BorderLayout.SOUTH);

        atualizarTabelaHistorico(); // Carrega os dados iniciais
    }

    public void atualizarTabelaHistorico() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Ticket> historico = ticketDAO.listarHistorico();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Ticket ticket : historico) {
            tableModel.addRow(new Object[]{
                    ticket.getPlaca(),
                    ticket.getCategoria(),
                    ticket.getHoraEntrada().format(formatter),
                    ticket.getHoraSaida() != null ? ticket.getHoraSaida().format(formatter) : "N/A",
                    String.format("R$ %.2f", ticket.getValorPago())
            });
        }
    }
}
