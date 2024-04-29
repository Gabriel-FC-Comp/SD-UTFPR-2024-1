
/**
 * Laboratorio 3
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 29/04/2023
 */

import java.io.Serializable; // Importa a interface Serializable para serialização de objetos

/**
 * Esta classe representa uma mensagem que pode ser enviada entre cliente e servidor.
 * Implementa a interface Serializable para permitir a serialização dos objetos.
 */
public class Mensagem implements Serializable {

    String mensagem; // Variável para armazenar a mensagem

    /**
     * Construtor para mensagem enviada do cliente para o servidor.
     * @param mensagem A mensagem a ser enviada.
     * @param opcao A opção que indica a ação a ser realizada.
     */
    public Mensagem(String mensagem, String opcao) {

        setMensagem(mensagem, opcao); // Chama o método setMensagem para definir a mensagem com base na opção

    }

    /**
     * Construtor para mensagem enviada do servidor para o cliente.
     * @param mensagem A mensagem a ser enviada.
     */
    public Mensagem(String mensagem) {
        String newMessage = ""; // Inicializa uma nova mensagem
        newMessage += "{\n"; // Adiciona uma quebra de linha e abre chaves
        newMessage += "\"result\": \"".concat(mensagem).concat("\""); // Adiciona o resultado da mensagem
        newMessage += "\n}"; // Adiciona uma quebra de linha e fecha chaves
        this.mensagem = newMessage; // Define a mensagem
    }

    /**
     * Método para obter a mensagem.
     * @return A mensagem.
     */
    public String getMensagem() {
        return this.mensagem; // Retorna a mensagem
    }

    /**
     * Método para definir a mensagem com base na fortuna e na opção.
     * @param fortune A fortuna a ser incluída na mensagem.
     * @param opcao A opção que indica a ação a ser realizada.
     */
    public void setMensagem(String fortune, String opcao) {
        String mensagem = ""; // Inicializa a mensagem

        switch (opcao) { // Inicia um switch-case com base na opção
            case "read": { // Caso a opção seja "read"

                mensagem += "{\n"; // Adiciona uma quebra de linha e abre chaves
                mensagem += "\"method\":\"read\",\n"; // Adiciona o método de leitura
                mensagem += "\"args\":[\"\"]\n"; // Adiciona os argumentos (vazio neste caso)
                mensagem += "}"; // Adiciona uma quebra de linha e fecha chaves

                break; // Sai do switch-case
            }
            case "write": { // Caso a opção seja "write"

                mensagem += "{\n"; // Adiciona uma quebra de linha e abre chaves
                mensagem += "\"method\":\"write\",\n"; // Adiciona o método de escrita
                mensagem += "\"args\":[\"".concat(fortune).concat("\"]\n"); // Adiciona os argumentos (fortuna)
                mensagem += "}"; // Adiciona uma quebra de linha e fecha chaves
                
                break; // Sai do switch-case
            }
            default: // Caso a opção não seja válida
                System.err.println("Erro - Tipo de mensagem inválido! - ".concat(opcao)); // Exibe uma mensagem de erro
                mensagem += "type_error"; // Define a mensagem como erro de tipo
                break; // Sai do switch-case
        }//fim switch
        this.mensagem = mensagem; // Define a mensagem
    }

}