package com.estacionamento.app.model;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String placa;
    private String categoria;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private double valorPago;
    private String status; // ATIVO, CONCLUIDO

    // Construtor para novo ticket (entrada)
    public Ticket(String placa, String categoria, LocalDateTime horaEntrada) {
        this.placa = placa;
        this.categoria = categoria;
        this.horaEntrada = horaEntrada;
        this.status = "ATIVO";
    }

    // Construtor para recuperar do banco de dados
    public Ticket(int id, String placa, String categoria, LocalDateTime horaEntrada, LocalDateTime horaSaida, double valorPago, String status) {
        this.id = id;
        this.placa = placa;
        this.categoria = categoria;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.valorPago = valorPago;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalDateTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        if ("CARRO".equalsIgnoreCase(this.categoria)) {
            return new Carro(this.placa, this.horaEntrada);
        } else if ("MOTO".equalsIgnoreCase(this.categoria)) {
            return new Moto(this.placa, this.horaEntrada);
        }
        return null; // Ou lançar uma exceção
    }
}
