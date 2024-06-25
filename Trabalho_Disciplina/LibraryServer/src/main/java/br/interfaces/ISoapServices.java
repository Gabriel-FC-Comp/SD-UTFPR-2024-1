/**
 * Título do Arquivo: ISoapServices.java
 * 
 * Descrição Breve: Este arquivo contém a interface que define os métodos para os serviços SOAP.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.interfaces;

import br.rmi.MessageRMI;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * Interface que define os métodos para os serviços SOAP.
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface ISoapServices {
    
    /**
     * Método para consultar um livro por meio do serviço SOAP.
     * 
     * @param mensagem a mensagem RMI contendo os detalhes da consulta.
     * @return a mensagem RMI com o resultado da consulta.
     */
    @WebMethod
    MessageRMI consultaLivro(MessageRMI mensagem);
    
    /**
     * Método para reservar livros por meio do serviço SOAP.
     * 
     * @param mensagem a mensagem RMI contendo os detalhes da reserva.
     * @return a mensagem RMI com o resultado da reserva.
     */
    @WebMethod
    MessageRMI reservaLivros(MessageRMI mensagem);
    
}// ISoapServices
