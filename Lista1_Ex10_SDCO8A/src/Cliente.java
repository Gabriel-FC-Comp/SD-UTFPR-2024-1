
/**
 * Laboratorio 1 de Sistemas Distribuidos
 * Autor: Gabriel Finger Conte
 *
 * Adaptado de Lucio A. Rocha
 * Ultima atualizacao: 26/03/2024
 *
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private final int porta = 1025;
    private int opcao_usuario;
    private final Scanner sc = new Scanner(System.in);

    public String read() throws IOException {

        // Instancia a mensagem para o servidor
        StringBuilder read_solicitation = new StringBuilder();
        // Constrói a mensagem de solicitação de read para o servidor
        read_solicitation.append("{\n").append("\"method\":\"read\",\n")
                .append("\"args\":[\"\"]\n").append("}");

        // Tenta enviar a solicitação de read para o servidor
        saida.writeUTF(read_solicitation.toString());

        // Espera a resposta do servidor
        String server_response;
        server_response = entrada.readUTF();

        // Instancia o retorno
        String fortuna;
        
        if (server_response.contains("\"result\":")) {

            int fortune_start_index = (server_response.indexOf("\"result\":") + "\"result\":".length());
            int fortune_end_index = server_response.length() - 1;
            fortuna = server_response.substring(fortune_start_index, fortune_end_index).trim();
            // Remove as aspas duplas do início e do fim, se presentes
            if (fortuna.startsWith("\"") && fortuna.endsWith("\"")) {
                fortuna = fortuna.substring(1, fortuna.length() - 1);
            }

        } else {
            System.err.println("Mensagem recebida do servidor com formato incorreto!\n" + server_response);
            fortuna = "Formatação incorreta da fortuna!";
        }

        return fortuna;
    }

    public void write() throws IOException {
        // Instancia a mensagem para o servidor
        StringBuilder write_solicitation = new StringBuilder();
        // Limpa o buffer do teclado
        sc.nextLine();
        do {
            // Constrói o cabeçalho da mensagem de solicitação de read para o servidor
            write_solicitation.append("{\n").append("\"method\":write,\n").append("\"args\":");

            // Espera o usuário inserir a nova fortuna
            StringBuilder new_fortune;
            String inserir_flag;
            do {
                // Reinicia a build da nova fortuna
                new_fortune = new StringBuilder();
                // Adiciona uma quebra de linha inicial
                new_fortune.append("\n");
                // Solicita ao usuário fornecer a nova fortuna
                System.out.println("Insira a nova fortuna para o arquivo:");
                // Enquanto o usuário não fornecer uma linha vazia
                String new_line;
                do {
                    // Lê e a outra linha da nova fortuna
                    new_line = sc.nextLine();
                    // Adiciona a nova linha a fortuna
                    new_fortune.append(new_line).append("\n");
                } while (!new_line.isEmpty());// do While
                // Solicita ao usuário se essa é mesmo a fortuna que deseja inserir:
                System.out.println("Deseja inserir ao arquivo a fortuna:");
                System.out.println("\"" + new_fortune.toString().substring(0, new_fortune.toString().length()-1) + "\" ? (s/n)");
                inserir_flag = sc.nextLine().strip();

            } while (!inserir_flag.equalsIgnoreCase("S"));// while
            
            // Finaliza a escrita da solicitação de write
            write_solicitation.append("[\"").append(new_fortune.toString()).append("\"]\n}");

            // Tenta enviar a solicitação de write para o servidor
            saida.writeUTF(write_solicitation.toString());

            // Espera a resposta do servidor
            String server_response;
            server_response = entrada.readUTF();

            // Verifica o retorno do servidor
            if (server_response.contains("false")) {
                System.err.println("Por favor, deixe uma quebra de linha ao final"
                        + " da nova fortuna para manter o padrão dos registros!");
            } else if(server_response.contains("true")){
                // Se deu tudo certo sai do loop e finaliza a função
                System.out.println("Nova fortuna adicionada com sucesso!");
                break;
            }else{
                System.err.println("Ouve um erro com a solicitação!\n" + server_response);
                break;
            }

        } while (true);
    }

    public void menu() {

        // Enquanto o usuário desejar permanecer no menu Cliente
        do {
            // Exibe o menu para o usuário
            System.out.println("Bem vindo ao Software Cliente!");
            System.out.println("Menu de Opções:");
            System.out.println("1 - Obter fortuna aleatória");
            System.out.println("2 - Adicionar nova fortuna ao banco de dados");
            System.out.println("3 - Finalizar Conexão");
            System.out.print("Digite sua opção: ");

            // Tenta converter a entrada do usuário para um inteiro
            try {
                opcao_usuario = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Por favor, digite uma opção válida!");
                continue;
            } catch (Exception e) {
                System.err.println("Ouve um erro, tente novamente!");
                continue;
            }

            // Verifica a opção escolhida pelo usuário
            switch (opcao_usuario) {
                // Caso tenha optado por ler uma fortuna aleatória
                case 1 -> {
                    try {
                        String fortuna = read();

                        System.out.println("\n################\n");
                        System.out.println(fortuna);
                        System.out.println("################\n\n");

                    } catch (IOException ex) {
                        System.err.println("Erro na solicitação de uma nova fortuna.");
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }// case 1

                // Caso tenha optado por enviar uma nova fortuna para o servidor
                case 2 -> {
                    try {
                        write();
                    } catch (IOException ex) {
                        System.err.println("Erro na tentativa de escrita de uma nova fortuna.");
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }// case 2

// Caso tenha optado por sair simplesmente não faz nada
                case 3 -> {
                }// case 3

                default ->
                    System.err.println("Por favor, digite uma opção válida!");
            }// switch

        } while (opcao_usuario != 3);
    }

    public void iniciar() {
        System.out.println("Cliente iniciado na porta: " + porta);

        try {

            // Estabelece conexão com o servidor
            socket = new Socket("127.0.0.1", porta);

            // Separa os canais de entrada e saída do socket
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());

            menu();

            System.out.println("Cliente finalizado!");
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cliente().iniciar();
    }

}
