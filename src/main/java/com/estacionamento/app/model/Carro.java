package com.estacionamento.app.model;

import java.time.LocalDateTime;

public class Carro extends Veiculo {
    private static final double TARIFA_CARRO_POR_HORA = 10.00;

    public Carro(String placa, LocalDateTime horaEntrada) {
        super(placa, "CARRO", horaEntrada);
    }

    @Override
    public double calcularTarifa(long tempoMinutos) {
        if (tempoMinutos <= 15) {
            return 0.00; // Tolerância de 15 minutos
        } else {
            long horasCobradas = (long) Math.ceil(tempoMinutos / 60.0);
            return horasCobradas * TARIFA_CARRO_POR_HORA;
        }
    }
}
