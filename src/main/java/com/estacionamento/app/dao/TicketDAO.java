package com.estacionamento.app.dao;

import com.estacionamento.app.model.Ticket;

import java.util.List;

public interface TicketDAO {
    void salvar(Ticket ticket);
    void atualizar(Ticket ticket);
    Ticket buscarAtivoPorPlaca(String placa);
    List<Ticket> listarHistorico();
    List<Ticket> listarAtivos();
}
