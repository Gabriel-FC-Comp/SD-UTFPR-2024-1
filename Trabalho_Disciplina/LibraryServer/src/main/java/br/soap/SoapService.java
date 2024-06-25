/**
 * Título do Arquivo: SoapService.java
 * 
 * Descrição Breve: Este arquivo contém a implementação do serviço SOAP que utiliza o RMI para operações de consulta e reserva de livros.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.soap;

import br.interfaces.ISoapServices;
import br.rmi.ClientRMI;
import br.rmi.MessageRMI;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

/**
 * Serviço SOAP que implementa as operações definidas na interface ISoapServices.
 */
@WebService(endpointInterface = "br.interfaces.ISoapServices")
public final class SoapService implements ISoapServices{

    private final ClientRMI clienteRMI;

    /**
     * Construtor que inicializa o serviço SOAP com o nome do servidor RMI.
     * 
     * @param RMIserverName o nome do servidor RMI para comunicação.
     */
    public SoapService(String RMIserverName) {
        this.clienteRMI = new ClientRMI(RMIserverName);
    }// SoapService

    /**
     * Consulta um livro utilizando o servidor RMI.
     * 
     * @param mensagem a mensagem contendo os dados da consulta.
     * @return a mensagem de resposta do servidor RMI.
     */
    @Override
    public MessageRMI consultaLivro(MessageRMI mensagem) {
        try {
            return this.clienteRMI.consultaLivro(mensagem);
        } catch (RemoteException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return new MessageRMI();
        }
    }// consultaLivro

    /**
     * Reserva livros utilizando o servidor RMI.
     * 
     * @param mensagem a mensagem contendo os dados da reserva.
     * @return a mensagem de resposta do servidor RMI.
     */
    @Override
    public MessageRMI reservaLivros(MessageRMI mensagem) {
        try {
            return this.clienteRMI.reservaLivros(mensagem);
        } catch (RemoteException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return new MessageRMI();
        }
    }// reservaLivros

}// SoapService
