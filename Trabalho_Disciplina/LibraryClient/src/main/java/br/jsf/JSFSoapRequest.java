/**
 * Título do Arquivo: JSFSoapRequest.java
 * 
 * Descrição Breve: Este arquivo contém a classe responsável por gerenciar as requisições SOAP em uma aplicação JSF.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.jsf;

import br.ejb.EJBSoapRequest;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

/**
 * Classe responsável por gerenciar as requisições SOAP em uma aplicação JSF.
 */
@Named(value = "JSFSoapRequest")
@RequestScoped
public class JSFSoapRequest {

    @EJB private EJBSoapRequest ejbSoap;
    private String consultTitle;
    private String reserveTitle;
    private String serverResponse;
    
    /**
     * Construtor padrão da classe.
     */
    public JSFSoapRequest() {}

    /**
     * Método para consultar um livro usando o serviço SOAP.
     */
    public void consultBook(){
        serverResponse = ejbSoap.consultBook(consultTitle);
    }
    
    /**
     * Método para reservar um livro usando o serviço SOAP.
     */
    public void reserveBook(){
        serverResponse = ejbSoap.reserveBook(reserveTitle);
    }
        
    public String getConsultTitle() {
        return consultTitle;
    }

    public String getReserveTitle() {
        return reserveTitle;
    }

    public String getServerResponse() {
        return serverResponse;
    }

    public void setConsultTitle(String consultTitle) {
        this.consultTitle = consultTitle;
    }

    public void setReserveTitle(String reserveTitle) {
        this.reserveTitle = reserveTitle;
    }

    public void setServerResponse(String serverResponse) {
        this.serverResponse = serverResponse;
    }    
    
}
