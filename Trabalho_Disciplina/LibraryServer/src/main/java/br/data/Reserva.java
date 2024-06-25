/**
 * Título do Arquivo: Reserva.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa uma reserva de recurso.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.data;

/**
 * Classe que representa uma reserva de recurso.
 */
public class Reserva {
    
    private String nomeCliente;

    /**
     * Construtor que inicializa a reserva com o nome do cliente.
     * 
     * @param nomeCliente o nome do cliente associado à reserva.
     */
    public Reserva(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }// Reserva

    /**
     * Retorna o nome do cliente associado à reserva.
     * 
     * @return o nome do cliente.
     */
    @Override
    public String toString() {
        return nomeCliente;
    }// toString
    
}// Reserva
