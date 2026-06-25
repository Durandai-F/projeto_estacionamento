package com.estacionamento.app.model;

import java.time.LocalDateTime;

public class Moto extends Veiculo {
    private static final double TARIFA_MOTO_POR_HORA = 5.00;

    public Moto(String placa, LocalDateTime horaEntrada) {
        super(placa, "MOTO", horaEntrada);
    }

    @Override
    public double calcularTarifa(long tempoMinutos) {
        if (tempoMinutos <= 15) {
            return 0.00; // Tolerância de 15 minutos
        } else {
            long horasCobradas = (long) Math.ceil(tempoMinutos / 60.0);
            return horasCobradas * TARIFA_MOTO_POR_HORA;
        }
    }
}
