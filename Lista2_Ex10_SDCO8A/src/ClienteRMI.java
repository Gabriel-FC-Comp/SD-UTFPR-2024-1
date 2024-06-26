
/**
 * Laboratorio 4
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

import java.rmi.registry.LocateRegistry; // Importa a classe LocateRegistry para localizar o registro RMI
import java.rmi.registry.Registry; // Importa a classe Registry para o registro RMI
import java.security.SecureRandom; // Importa a classe SecureRandom para geração de números aleatórios seguros
import java.util.ArrayList; // Importa a classe ArrayList para armazenar uma lista de pares
import java.util.Scanner; // Importa a classe Scanner para entrada de dados

/**
 * Classe ClienteRMI para interagir com um servidor RMI.
 */
public class ClienteRMI {

    /**
     * Método privado para decodificar a resposta do servidor e extrair a
     * fortuna.
     *
     * @param resposta A resposta do servidor.
     * @return A fortuna decodificada.
     */
    private static String decodeFortuna(String resposta) {
        String fortuna = ""; // Inicializa a variável fortuna como uma string vazia
        if (resposta.contains("\"result\":")) { // Verifica se a resposta contém a sequência de caracteres "\"result\":"
            int fortune_start_index = (resposta.indexOf("\"result\":") + "\"result\":".length()); // Calcula o índice inicial da fortuna
            int fortune_end_index = resposta.length() - 1; // Calcula o índice final da fortuna
            fortuna = resposta.substring(fortune_start_index, fortune_end_index).trim(); // Extrai a fortuna da resposta e remove espaços em branco
            // Remove as aspas duplas do início e do fim, se presentes
            if (fortuna.startsWith("\"") && fortuna.endsWith("\"")) {
                fortuna = fortuna.substring(1, fortuna.length() - 1);
            }
        } else {
            System.err.println("Mensagem recebida do servidor com formato incorreto!\n" + resposta); // Exibe uma mensagem de erro
            fortuna = "Formatação incorreta da fortuna!"; // Define a fortuna como uma mensagem de erro padrão
        }
        return fortuna; // Retorna a fortuna
    }

    /**
     * Método privado para solicitar ao usuário inserir uma nova fortuna.
     *
     * @param leitura O Scanner para entrada de dados.
     * @return A nova fortuna inserida pelo usuário.
     */
    private static String askUser_newFortuna(Scanner leitura) {
        // Limpa o buffer do teclado
        leitura.nextLine();
        // Espera o usuário inserir a nova fortuna
        StringBuffer new_fortune = new StringBuffer();
        String inserir_flag = "n";
        while (!inserir_flag.equalsIgnoreCase("S")) {
            // Reinicia o construtor da nova fortuna
            new_fortune = new StringBuffer();
            // Solicita ao usuário fornecer a nova fortuna
            System.out.println("Insira a nova fortuna para o arquivo:");
            // Enquanto o usuário não fornecer uma linha vazia
            String new_line;
            do {
                // Lê e a outra linha da nova fortuna
                new_line = leitura.nextLine();
                // Adiciona a nova linha a fortuna
                new_fortune.append(new_line).append("\n");
            } while (!new_line.isEmpty());// do While
            // Solicita ao usuário se essa é mesmo a fortuna que deseja inserir:
            System.out.println("Deseja inserir ao arquivo a fortuna:");
            System.out.println("\"" + new_fortune.toString().substring(0, new_fortune.toString().length() - 1) + "\" ? (s/n)");
            inserir_flag = leitura.nextLine().strip();

        }// while

        return new_fortune.toString();
    }

    /**
     * Método privado para decodificar e escrever a resposta do servidor.
     *
     * @param resposta A resposta do servidor.
     */
    private static void decode_writeResponse(String resposta) {
        // Verifica o retorno do servidor
        if (resposta.contains("false")) {
            System.err.println("Por favor, deixe uma quebra de linha ao final" + " da nova fortuna para manter o padrão dos registros!");
        } else if (resposta.contains("true")) {
            // Se deu tudo certo sai do loop e finaliza a função
            System.out.println("Nova fortuna adicionada com sucesso!");
        } else {
            System.err.println("Ouve um erro com a solicitação!\n" + resposta);
        }
    }

    public static void main(String[] args) {

        // Obter a Lista de pares disponiveis do arquivo Peer.java
        ArrayList<Peer> listaPeers = new ArrayList<>();
        for (Peer newPeer : Peer.values()) {
            listaPeers.add(newPeer);
        }// for

        try {

            // Obtém referência para o Brooker
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);

            //Escolhe um peer aleatorio da lista de peers para conectar
            // Variáveis auxiliares
            SecureRandom sr = new SecureRandom();
            IMensagem stub = null;
            Peer peer = null;
            boolean conectou = false;

            // Equanto não se conectar com o servidor
            while (!conectou) {
                // Seleciona um Peer aleatório
                peer = listaPeers.get(sr.nextInt(listaPeers.size()));
                // Tenta se conectar com o peer
                try {
                    stub = (IMensagem) registro.lookup(peer.getNome());
                    conectou = true;
                } catch (java.rmi.ConnectException e) {
                    System.out.println(peer.getNome() + " indisponivel. ConnectException. Tentanto o proximo...");
                } catch (java.rmi.NotBoundException e) {
                    System.out.println(peer.getNome() + " indisponivel. NotBoundException. Tentanto o proximo...");
                }// try-catch
            }// while
            // Informa o Peer que foi conectado
            System.out.println("Conectado no peer: " + peer.getNome());

            String opcao = ""; // Inicializa a variável opcao como uma string vazia
            Scanner leitura = new Scanner(System.in); // Inicializa o Scanner para entrada de dados
            do {
                System.out.println("Menu do Cliente:"); // Exibe o menu do cliente
                System.out.println("1) Read"); // Exibe a opção de leitura
                System.out.println("2) Write"); // Exibe a opção de escrita
                System.out.println("x) Exit"); // Exibe a opção de saída
                System.out.print(">> "); // Exibe o prompt para entrada do usuário
                opcao = leitura.next(); // Lê a opção escolhida pelo usuário

                switch (opcao.toUpperCase()) { // Inicia um switch-case com a opção em letras maiúsculas
                    case "1": { // Caso a opção seja "1"
                        Mensagem mensagem = new Mensagem("", "read"); // Cria uma mensagem de leitura
                        String resposta = stub.enviar(mensagem).getMensagem(); // Envia a mensagem ao servidor e obtém a resposta
                        //System.out.println(resposta.getMensagem());
                        String fortuna = decodeFortuna(resposta); // Decodifica a resposta para obter a fortuna

                        System.out.println("\n################\n"); // Exibe uma linha divisória
                        System.out.println(fortuna); // Exibe a fortuna
                        System.out.println("################\n\n"); // Exibe uma linha divisória

                        break; // Sai do switch-case
                    }
                    case "2": { // Caso a opção seja "2"
                        //Monta a mensagem
                        String fortune = askUser_newFortuna(leitura); // Solicita ao usuário inserir uma nova fortuna
                        Mensagem mensagem = new Mensagem(fortune, "write"); // Cria uma mensagem de escrita com a nova fortuna
                        String resposta = stub.enviar(mensagem).getMensagem(); // Envia a mensagem ao servidor e obtém a resposta
                        //System.out.println(resposta.getMensagem());
                        decode_writeResponse(resposta); // Decodifica e processa a resposta do servidor
                        break; // Sai do switch-case
                    }
                    case "X": // Caso a opção seja "X"
                        System.out.println("Saindo do sistema..."); // Exibe uma mensagem de saída
                        break; // Sai do switch-case
                    default: // Caso a opção não seja válida
                        System.err.println("Opção inválida, digite novamente!"); // Exibe uma mensagem de erro
                        break; // Sai do switch-case
                }// switch

            } while (!opcao.toUpperCase().equals("X")); // Executa o loop até que a opção seja "X"

        } catch (Exception e) {
            e.printStackTrace(); // Exibe o rastreamento da pilha de exceções
        }

    }

}
