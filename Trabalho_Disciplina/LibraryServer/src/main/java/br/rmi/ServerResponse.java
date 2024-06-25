/**
 * Título do Arquivo: ServerResponse.java
 * 
 * Descrição Breve: Este arquivo contém a enumeração que representa as respostas do servidor.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.rmi;

/**
 * Enumeração que representa as respostas do servidor.
 */
public enum ServerResponse {
    /**
     * Resposta indicando uma reserva bem-sucedida.
     */
    SUCCESSFUL_RESERVATION("successfulReservation"),

    /**
     * Resposta indicando uma reserva malsucedida.
     */
    UNSUCCESSFUL_RESERVATION("unsuccessfulReservation"),

    /**
     * Resposta indicando uma consulta bem-sucedida.
     */
    SUCCESSFUL_QUERY("successfulQuery"),

    /**
     * Resposta indicando uma consulta malsucedida.
     */
    UNSUCCESSFUL_QUERY("unsuccessfulQuery"),

    /**
     * Resposta indicando um erro desconhecido.
     */
    UNKNOWN("error");

    private final String response;

    /**
     * Construtor que inicializa a resposta.
     * 
     * @param response a resposta do servidor.
     */
    ServerResponse(String response) {
        this.response = response;
    }// ServerResponse

    /**
     * Retorna a resposta como uma string.
     * 
     * @return a resposta do servidor.
     */
    @Override
    public String toString() {
        return response;
    }// toString
}// ServerResponse
