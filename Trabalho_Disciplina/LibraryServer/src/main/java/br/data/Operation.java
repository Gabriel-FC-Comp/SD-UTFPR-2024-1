/**
 * Título do Arquivo: Operation.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa uma operação em um recurso.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.data;

/**
 * Classe que representa uma operação em um recurso.
 */
public class Operation {
    
    protected OperationType type;
    protected String targetedResource;
    protected String clientName;
    protected ActionType action;

    /**
     * Construtor para uma operação de leitura.
     * 
     * @param resource o recurso alvo da operação.
     * @param clientName o nome do cliente realizando a operação.
     */
    public Operation(String resource, String clientName) {
        this.type = OperationType.READ;
        this.targetedResource = resource;
        this.action = ActionType.UNKNOWN;
        this.clientName = clientName;
    }// Operation
    
    /**
     * Construtor para uma operação de escrita.
     * 
     * @param resource o recurso alvo da operação.
     * @param clientName o nome do cliente realizando a operação.
     * @param action a ação a ser realizada (reserva, cancelamento, desconhecido, etc.).
     */
    public Operation(String resource, String clientName, ActionType action) {
        this.type = OperationType.WRITE;
        this.targetedResource = resource;
        this.action = action;
        this.clientName = clientName;
    }// Operation

    /**
     * Retorna uma representação em string da operação.
     * 
     * @return uma string representando a operação.
     */
    @Override
    public String toString() {
        if(this.action.equals("")){
            return this.type + this.clientName + "[" + this.targetedResource +  "]";
        }// if
        return this.type + this.clientName + "[" + this.targetedResource + ", " + this.action + "]";
    }// toString
    
}
