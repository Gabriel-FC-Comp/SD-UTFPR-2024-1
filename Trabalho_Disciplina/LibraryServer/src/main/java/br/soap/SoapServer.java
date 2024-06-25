/**
 * Título do Arquivo: SoapServer.java
 * 
 * Descrição Breve: Este arquivo contém a classe responsável por iniciar o servidor SOAP.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */
package br.soap;

import br.interfaces.ISoapServices;
import javax.xml.ws.Endpoint;

/**
 * Classe responsável por iniciar o servidor SOAP.
 */
public class SoapServer {

    /**
     * Método que inicia o servidor SOAP em um determinado endereço com um serviço SOAP específico.
     * 
     * @param address o endereço onde o servidor SOAP será publicado.
     * @param soapService o serviço SOAP a ser disponibilizado pelo servidor.
     */
    public void initiSoapServer(String address, ISoapServices soapService) {
        Endpoint.publish(address, soapService);
        System.out.println("SOAP Server is running at " + address);
    }// initiSoapServer
}// SoapServer
