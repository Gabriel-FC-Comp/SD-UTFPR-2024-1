/**
 * Título do Arquivo: ServerRMI.java
 * 
 * Descrição Breve: Este arquivo contém a implementação do servidor RMI para consultas e reservas de livros.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.rmi;

import br.interfaces.IMessageRMI;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import br.data.ActionType;
import br.data.Operation;
import br.data.SharedResourcesManager;

/**
 * Classe que implementa o servidor RMI para consultas e reservas de livros.
 */
public class ServerRMI implements IMessageRMI {

    private String serverName;
    private int remoteCallCount;
    private SharedResourcesManager serverMemory;

    /**
     * Método para consultar um livro.
     * 
     * @param mensagem a mensagem contendo os dados da consulta.
     * @return a mensagem de resposta do servidor RMI.
     * @throws RemoteException em caso de erro na comunicação remota.
     */
    @Override
    public MessageRMI consultaLivro(MessageRMI mensagem) throws RemoteException {
        String remoteCallIndex = getRemoteCallIndex();
        List<Operation> operationList = new ArrayList<>();
        for (String titulo : mensagem.getListaLivros()) {
            operationList.add(new Operation(titulo, remoteCallIndex));
        }

        MessageRMI serverResponse;
        String serverFeedback = this.serverMemory.scheduler2PL(operationList, remoteCallIndex);
        if (!serverFeedback.contains("abort")) {
            serverResponse = new MessageRMI(serverFeedback, ServerResponse.SUCCESSFUL_QUERY);
        } else {
            serverResponse = new MessageRMI(serverFeedback, ServerResponse.UNSUCCESSFUL_QUERY);
        }
        return serverResponse;
    }

    /**
     * Método para reservar livros.
     * 
     * @param mensagem a mensagem contendo os dados da reserva.
     * @return a mensagem de resposta do servidor RMI.
     * @throws RemoteException em caso de erro na comunicação remota.
     */
    @Override
    public MessageRMI reservaLivros(MessageRMI mensagem) throws RemoteException {
        String remoteCallIndex = getRemoteCallIndex();
        List<Operation> operationList = new ArrayList<>();
        ActionType actionType = ActionType.getActionType(mensagem.getMensagem());
        for (String titulo : mensagem.getListaLivros()) {
            operationList.add(new Operation(titulo, remoteCallIndex, actionType));
        }

        MessageRMI serverResponse;
        String serverFeedback = this.serverMemory.scheduler2PL(operationList, remoteCallIndex);
        if (!serverFeedback.contains("abort")) {
            serverResponse = new MessageRMI(serverFeedback, ServerResponse.SUCCESSFUL_RESERVATION);
        } else {
            serverResponse = new MessageRMI(serverFeedback, ServerResponse.UNSUCCESSFUL_RESERVATION);
        }
        return serverResponse;
    }

    /**
     * Construtor que inicializa o servidor RMI com um nome específico.
     * 
     * @param serverName o nome do servidor RMI.
     */
    public ServerRMI(String serverName) {
        setServerName(serverName);
        startServer();
        this.serverMemory = new SharedResourcesManager();
        remoteCallCount = 0;
    }

    /**
     * Obtém o índice da chamada remota.
     * 
     * @return o índice da chamada remota.
     */
    private String getRemoteCallIndex() {
        int currIndex = remoteCallCount++;
        return Integer.toString(currIndex);
    }

    /**
     * Inicia o servidor RMI.
     */
    private void startServer() {
        try {
            Registry serverRegistry;
            try {
                serverRegistry = LocateRegistry.createRegistry(1099);
            } catch (java.rmi.server.ExportException e) {
                System.out.print("Registry already started. Using active one.\n");
            }
            serverRegistry = LocateRegistry.getRegistry();

            IMessageRMI skeleton = (IMessageRMI) UnicastRemoteObject.exportObject(this, 0);

            serverRegistry.rebind(serverName, skeleton);
            System.out.println(serverName + " RMI Server: Waiting for connections...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtém o nome do servidor RMI.
     * 
     * @return o nome do servidor RMI.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Define o nome do servidor RMI.
     * 
     * @param serverName o nome do servidor RMI a ser definido.
     */
    private void setServerName(String serverName) {
        this.serverName = serverName;
    }

}
