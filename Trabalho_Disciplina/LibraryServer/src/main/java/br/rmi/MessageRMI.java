/**
 * Título do Arquivo: MessageRMI.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa uma mensagem utilizada na comunicação RMI.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma mensagem utilizada na comunicação RMI.
 */
public final class MessageRMI implements Serializable {
    private String mensagem;
    private List<String> listaLivros;

    /**
     * Construtor padrão para criar uma mensagem de erro na consulta RMI.
     */
    public MessageRMI() {
        this.listaLivros = null;
        setErrorMesage();
    }

    /**
     * Construtor para criar uma mensagem enviada pelo Cliente RMI.
     * 
     * @param tipoServico o tipo de serviço.
     * @param listaLivros a lista de livros associada à mensagem.
     */
    public MessageRMI(String tipoServico, List<String> listaLivros) {
        setListaLivros(listaLivros);
        setClientRequest(tipoServico);
    }

    /**
     * Construtor para criar uma mensagem de resposta do Servidor RMI.
     * 
     * @param feedback o feedback da operação.
     * @param tipoFeedback o tipo de feedback.
     */
    public MessageRMI(String feedback, ServerResponse tipoFeedback) {
        setServerResponse(tipoFeedback.toString(), feedback);
    }

    /**
     * Define a requisição do cliente RMI na mensagem.
     * 
     * @param tipoServico o tipo de serviço.
     */
    public void setClientRequest(String tipoServico) {
        StringBuilder messageCreator = new StringBuilder();
        messageCreator.append("{\n");
        messageCreator.append("\"serviceRequest\": \"");
        messageCreator.append(tipoServico);
        messageCreator.append("\",\n\"");
        messageCreator.append("bookCount\": ");
        messageCreator.append(this.listaLivros.size());
        messageCreator.append("\n}\n");
        setMensagem(messageCreator.toString());
    }

    /**
     * Define a resposta do servidor RMI na mensagem.
     * 
     * @param tipoResposta o tipo de resposta.
     * @param feedback o feedback da operação.
     */
    public void setServerResponse(String tipoResposta, String feedback) {
        StringBuilder messageCreator = new StringBuilder();
        messageCreator.append("{\n");
        messageCreator.append("\"responseStatus\": \"");
        messageCreator.append(tipoResposta);
        messageCreator.append("\",\n");
        messageCreator.append(feedback);
        messageCreator.append("\n}\n");
        setMensagem(messageCreator.toString());
    }

    /**
     * Define uma mensagem de erro na mensagem.
     */
    public void setErrorMesage() {
        StringBuilder messageCreator = new StringBuilder();
        messageCreator.append("{\n");
        messageCreator.append("\"responseStatus\": \"error\"\n");
        messageCreator.append("\"feedback\":\"RemoteException\"\n}\n");
        setMensagem(messageCreator.toString());
    }

    /**
     * Obtém a lista de livros associada à mensagem.
     * 
     * @return a lista de livros.
     */
    public List<String> getListaLivros() {
        return listaLivros;
    }

    /**
     * Obtém a mensagem.
     * 
     * @return a mensagem.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Define a lista de livros associada à mensagem.
     * 
     * @param listaLivros a lista de livros a ser definida.
     */
    public void setListaLivros(List<String> listaLivros) {
        this.listaLivros = listaLivros;
    }

    /**
     * Define a mensagem.
     * 
     * @param mensagem a mensagem a ser definida.
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
