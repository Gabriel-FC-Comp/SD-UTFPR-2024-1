/**
 * Título do Arquivo: EJBSoapRequest.java
 * 
 * Descrição Breve: Este arquivo contém a classe EJB responsável por realizar requisições SOAP para consultar e reservar livros.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.ejb;

import br.interfaces.MessageRMI;
import br.soap.ISoapServices;
import br.soap.SoapServiceService;
import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;

/**
 * Classe EJB responsável por realizar requisições SOAP para consultar e reservar livros.
 */
@Stateless
@LocalBean
public class EJBSoapRequest {

    private SoapServiceService service = new SoapServiceService();
    private ISoapServices port = this.service.getSoapServicePort();

    /**
     * Método para consultar um livro usando o serviço SOAP.
     * @param titulo O título do livro a ser consultado.
     * @return Uma string representando a resposta do serviço.
     */
    public String consultBook(String titulo) {
        try {
            if (titulo != null) {
                MessageRMI request = new MessageRMI();
                request.setMensagem("consulta");
                request.getListaLivros().add(titulo);
                MessageRMI response = this.port.consultaLivro(request);

                return response != null ? response.getMensagem() : "Erro na consulta";
            } else {
                return "Título inválido";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na comunicação com o serviço";
        }
    }

    /**
     * Método para reservar um livro usando o serviço SOAP.
     * @param titulo O título do livro a ser reservado.
     * @return Uma string representando a resposta do serviço.
     */
    public String reserveBook(String titulo) {

        try {
            if (titulo != null) {
                MessageRMI request = new MessageRMI();
                request.setMensagem("reserva");
                request.getListaLivros().add(titulo);
                MessageRMI response = this.port.reservaLivros(request);

                return response != null ? response.getMensagem() : "Erro na reserva";
            } else {
                return "Título inválido";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na comunicação com o serviço";
        }
    }

}
