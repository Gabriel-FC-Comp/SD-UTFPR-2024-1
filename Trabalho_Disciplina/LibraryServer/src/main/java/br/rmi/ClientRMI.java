/**
 * Título do Arquivo: ClientRMI.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa o cliente RMI para comunicação com o servidor RMI.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.rmi;

import br.interfaces.IMessageRMI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

/**
 * Classe que representa o cliente RMI para comunicação com o servidor RMI.
 */
public final class ClientRMI implements IMessageRMI {
    
    private final String serverName;
    private Registry registro;
    private IMessageRMI stub;
    
    /**
     * Construtor para criar um cliente RMI com o nome do servidor.
     * 
     * @param serverName o nome do servidor RMI.
     */
    public ClientRMI(String serverName) {
        this.serverName = serverName;
        this.initServerConnection();
    }

    /**
     * Inicia a conexão com o servidor RMI.
     */
    public void initServerConnection() {

        try {
            // Obtém o registro RMI
            try {
                registro = LocateRegistry.getRegistry("127.0.0.1", 1099);
            } catch (RemoteException ex) {
                System.err.println("Erro: Registry não localizado, encerrando cliente...");
                System.exit(1);
            }// try-catch
            
            boolean conectou = false;
            int connectionTries = 0;
            // Enquanto não se conectar com o servidor
            while (!conectou) {
                connectionTries++;
                // Tenta se conectar com o peer
                try {
                    stub = (IMessageRMI) registro.lookup(serverName);
                    conectou = true;
                } catch (java.rmi.ConnectException e) {
                    System.out.println(serverName + " indisponível. ConnectException...");
                } catch (java.rmi.NotBoundException e) {
                    System.out.println(serverName + " indisponível. NotBoundException...");
                }// try-catch
                if(connectionTries == 20){
                    System.err.println("Erro: Limite máximo de tentativas excedido, encerrando cliente...");
                    System.exit(1);
                }// if
            }// while
        } catch (RemoteException e) {
            e.printStackTrace();
        }//try-catch
        
    }// initServerConnection

    @Override
    public MessageRMI consultaLivro(MessageRMI mensagem) throws RemoteException {
        return this.stub.consultaLivro(mensagem);
    }// consultaLivro

    @Override
    public MessageRMI reservaLivros(MessageRMI mensagem) throws RemoteException {
        return this.stub.reservaLivros(mensagem);
    }// reservaLivros
    
}// ClientRMI
