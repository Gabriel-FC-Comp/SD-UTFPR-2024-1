/**
 * Título do Arquivo: LibraryServer.java
 * 
 * Descrição Breve: Este arquivo contém a classe principal que inicia o servidor da biblioteca.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.edu.utfpr.libraryserver;

import br.rmi.ServerRMI;
import br.soap.SoapServer;
import br.soap.SoapService;

/**
 * Classe principal que inicia o servidor da biblioteca.
 */
public class LibraryServer {

    /**
     * Método principal que inicia o servidor.
     * 
     * @param args argumentos passados para o programa (não utilizados neste caso).
     */
    public static void main(String[] args) {
        String RMIserverName = "GeneralServer1";
        String address = "http://localhost:3000/library";
        
        ServerRMI serverRMI = new ServerRMI(RMIserverName);
        
        SoapService soapService = new SoapService(RMIserverName);
        SoapServer soapServer = new SoapServer();
        soapServer.initiSoapServer(address, soapService);
    }
}