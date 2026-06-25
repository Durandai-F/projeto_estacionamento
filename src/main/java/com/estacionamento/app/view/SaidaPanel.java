package com.estacionamento.app.view;

import com.estacionamento.app.dao.MySQLTicketDAO;
import com.estacionamento.app.model.Ticket;
import com.estacionamento.app.model.Veiculo;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaidaPanel extends JPanel {
    private JTextField placaField;
    private JButton processarSaidaButton;
    private JTextArea reciboArea;
    private MySQLTicketDAO ticketDAO;

    public SaidaPanel(MySQLTicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JLabel placaLabel = new JLabel("Placa do Veículo:");
        placaField = new JTextField(10);
        processarSaidaButton = new JButton("Processar Saída");
        reciboArea = new JTextArea(10, 30);
        reciboArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reciboArea);

        // Adicionar componentes ao painel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(placaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(placaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(processarSaidaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        // Ação do botão Processar Saída
        processarSaidaButton.addActionListener(e -> processarSaida());
    }

    private void processarSaida() {
        String placa = placaField.getText().trim().toUpperCase();

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira a placa do veículo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ticket ticketAtivo = ticketDAO.buscarAtivoPorPlaca(placa);

        if (ticketAtivo == null) {
            JOptionPane.showMessageDialog(this, "Veículo não encontrado ou já saiu do pátio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime horaSaida = LocalDateTime.now();
        Duration duracao = Duration.between(ticketAtivo.getHoraEntrada(), horaSaida);
        long tempoMinutos = duracao.toMinutes();

        Veiculo veiculo = ticketAtivo.getVeiculo();
        double valorTotal = veiculo.calcularTarifa(tempoMinutos);

        // Exibir recibo para confirmação (RF08)
        String recibo = "--- RECIBO DE PAGAMENTO ---\n" +
                        "Placa: " + ticketAtivo.getPlaca() + "\n" +
                        "Categoria: " + ticketAtivo.getCategoria() + "\n" +
                        "Entrada: " + ticketAtivo.getHoraEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                        "Saída: " + horaSaida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                        "Tempo Total: " + tempoMinutos + " minutos\n" +
                        "Valor a Pagar: R$ " + String.format("%.2f", valorTotal) + "\n" +
                        "---------------------------";
        reciboArea.setText(recibo);

        int confirmacao = JOptionPane.showConfirmDialog(this, new JScrollPane(new JTextArea(recibo)), "Confirmar Pagamento", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            ticketAtivo.setHoraSaida(horaSaida);
            ticketAtivo.setValorPago(valorTotal);
            ticketAtivo.setStatus("CONCLUIDO");
            ticketDAO.atualizar(ticketAtivo);
            JOptionPane.showMessageDialog(this, "Pagamento confirmado e saída registrada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            placaField.setText("");
            reciboArea.setText("");
            // Notificar outros painéis para atualização, se necessário
        } else {
            JOptionPane.showMessageDialog(this, "Pagamento cancelado. Veículo permanece no pátio.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
