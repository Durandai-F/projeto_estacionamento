package com.estacionamento.app.model;

import java.time.LocalDateTime;

public abstract class Veiculo {
    protected String placa;
    protected String categoria;
    protected LocalDateTime horaEntrada;

    public Veiculo(String placa, String categoria, LocalDateTime horaEntrada) {
        this.placa = placa;
        this.categoria = categoria;
        this.horaEntrada = horaEntrada;
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

    public abstract double calcularTarifa(long tempoMinutos);
}
