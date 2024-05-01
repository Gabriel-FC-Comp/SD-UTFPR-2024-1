
/**
 * Laboratorio 4
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

import java.io.Serializable; // Importa a interface Serializable para serialização de objetos

/**
 * A classe Mensagem representa uma mensagem que pode ser enviada entre cliente
 * e servidor. Implementa a interface Serializable para permitir a serialização
 * dos objetos.
 */
public class Mensagem implements Serializable {

    String mensagem; // Variável para armazenar a mensagem

    /**
     * Construtor para mensagem enviada do cliente para o servidor.
     *
     * @param mensagem A mensagem a ser enviada.
     * @param opcao A opção que indica a ação a ser realizada.
     */
    public Mensagem(String mensagem, String opcao) {
        setMensagem(mensagem, opcao); // Chama o método setMensagem para definir a mensagem com base na opção
    }

    /**
     * Construtor para mensagem enviada do servidor para o cliente.
     *
     * @param mensagem A mensagem a ser enviada.
     */
    public Mensagem(String mensagem) {
        // Cria a mensagem no formato JSON com base na mensagem recebida
        String newMessage = "{\n\"result\": \"" + mensagem + "\"\n}";
        this.mensagem = newMessage; // Define a mensagem
    }

    /**
     * Método para obter a mensagem.
     *
     * @return A mensagem.
     */
    public String getMensagem() {
        return this.mensagem; // Retorna a mensagem
    }

    /**
     * Método para definir a mensagem com base na fortuna e na opção.
     *
     * @param fortune A fortuna a ser incluída na mensagem.
     * @param opcao A opção que indica a ação a ser realizada.
     */
    public void setMensagem(String fortune, String opcao) {
        // Constrói a mensagem JSON com base na opção e na fortuna
        String mensagem = "{\n";
        mensagem += "\"method\":\"" + opcao + "\",\n";
        mensagem += "\"args\":[\"" + fortune + "\"]\n";
        mensagem += "}";
        this.mensagem = mensagem; // Define a mensagem
    }

}
