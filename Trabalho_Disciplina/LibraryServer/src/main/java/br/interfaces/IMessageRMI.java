/**
 * Título do Arquivo: IMessageRMI.java
 * 
 * Descrição Breve: Este arquivo contém a interface que define os métodos para comunicação RMI.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import br.rmi.MessageRMI;

/**
 * Interface que define os métodos para comunicação RMI.
 */
public interface IMessageRMI extends Remote{
   
    /**
     * Método para consultar um livro por meio de comunicação RMI.
     * 
     * @param mensagem a mensagem RMI contendo os detalhes da consulta.
     * @return a mensagem RMI com o resultado da consulta.
     * @throws RemoteException se ocorrer um erro durante a comunicação remota.
     */
    public MessageRMI consultaLivro(MessageRMI mensagem) throws RemoteException;
    
    /**
     * Método para reservar livros por meio de comunicação RMI.
     * 
     * @param mensagem a mensagem RMI contendo os detalhes da reserva.
     * @return a mensagem RMI com o resultado da reserva.
     * @throws RemoteException se ocorrer um erro durante a comunicação remota.
     */
    public MessageRMI reservaLivros(MessageRMI mensagem) throws RemoteException;
    
}
