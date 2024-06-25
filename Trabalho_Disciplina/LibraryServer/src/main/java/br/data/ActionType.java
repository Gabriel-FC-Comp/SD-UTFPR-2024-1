/**
 * Título do Arquivo: ActionType.java
 * 
 * Descrição Breve: Este arquivo contém a enumeração que representa os tipos de ação possíveis.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.data;

/**
 * Enumeração que representa os tipos de ação possíveis.
 */
public enum ActionType {
    /**
     * Representa a ação de reservar.
     */
    RESERV("reservar"),

    /**
     * Representa a ação de cancelar.
     */
    CANCEL("cancelar"),

    /**
     * Representa uma ação desconhecida.
     */
    UNKNOWN("desconhecido");

    private final String type;
    
    /**
     * Construtor privado para inicializar o tipo de ação.
     *
     * @param type o tipo de ação como uma string.
     */
    private ActionType(String type) {
        this.type = type;
    }// ActionType

    /**
     * Retorna a representação em string do tipo de ação.
     *
     * @return a representação em string do tipo de ação.
     */
    @Override
    public String toString() {
        return this.type;
    }// to String

    /**
     * Retorna o tipo de ação baseado na mensagem fornecida.
     * <p>
     * A mensagem deve conter uma linha no formato "serviceRequest": "tipo".
     * </p>
     *
     * @param mensagem a mensagem contendo o tipo de ação.
     * @return o tipo de ação correspondente na enumeração ActionType.
     */
    public static ActionType getActionType(String mensagem) {
        String cabecalho = "serviceRequest\": \"";
        int startIndex = mensagem.lastIndexOf(cabecalho) + cabecalho.length();
        int endIndex = mensagem.indexOf("\"", startIndex);
        String tipo = mensagem.substring(startIndex, endIndex);
        switch(tipo.toLowerCase()){
            case "reservar":
                return ActionType.RESERV;
            case "cancelar":
                return ActionType.CANCEL;
            default:
                return ActionType.UNKNOWN;
        }// switch
    }// getActionType
}// ActionType
