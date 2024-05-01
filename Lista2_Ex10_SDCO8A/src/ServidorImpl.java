
/**
 * Laboratorio 4
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

import java.rmi.RemoteException; // Importa a exceção RemoteException para lidar com operações remotas
import java.rmi.registry.LocateRegistry; // Importa a classe LocateRegistry para localizar o registro de serviços RMI
import java.rmi.registry.Registry; // Importa a interface Registry para interagir com o registro de serviços RMI
import java.rmi.server.UnicastRemoteObject; // Importa a classe UnicastRemoteObject para exportar objetos remotos
import java.security.SecureRandom; // Importa a classe SecureRandom para geração de números aleatórios seguros
import java.util.ArrayList; // Importa a classe ArrayList para armazenar uma lista dinâmica de Peers

/**
 * Classe que implementa a interface IMensagem para oferecer serviços de
 * servidor RMI.
 */
public class ServidorImpl implements IMensagem {

    ArrayList<Peer> alocados; // Lista de Peers disponíveis
    Principal serv_principal; // Instância da classe Principal para manipulação de arquivos

    /**
     * Construtor da classe ServidorImpl. Inicializa a lista de Peers e a
     * instância da classe Principal.
     */
    public ServidorImpl() {
        alocados = new ArrayList<>(); // Inicializa a lista de Peers
        for (Peer newPeer : Peer.values()) { // Itera sobre os valores do enum Peer
            alocados.add(newPeer); // Adiciona cada Peer à lista
        }
        serv_principal = new Principal(); // Inicializa a instância da classe Principal
    }

    // Método remoto da interface IMensagem
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta; // Declaração da variável de resposta
        try {
            System.out.println("\nMensagem recebida: " + mensagem.getMensagem()); // Exibe a mensagem recebida
            resposta = new Mensagem(parserJSON(mensagem.getMensagem())); // Chama o método parserJSON para processar a mensagem
        } catch (Exception e) {
            e.printStackTrace(); // Exibe o rastreamento da pilha de exceções
            resposta = new Mensagem("{\n" + "\"result\": false\n" + "}"); // Cria uma resposta de erro
        }
        return resposta; // Retorna a resposta processada
    }

    /**
     * Método para extrair a nova fortuna da mensagem do cliente.
     * @param client_message A mensagem recebida do cliente.
     * @return A nova fortuna extraída da mensagem.
     */
    private String read_new_fortune(String client_message){
        int new_fortune_start_index = client_message.indexOf("\"args\":[\"") + "\"args\":[\"".length();
        int new_fortune_end_index = client_message.length()-"\"]\n}".length();
        return client_message.substring(new_fortune_start_index,new_fortune_end_index);
    }

    /**
     * Método para analisar um JSON e executar a ação correspondente.
     *
     * @param json O JSON a ser analisado.
     * @return O resultado da análise em formato JSON.
     */
    public String parserJSON(String json) {
        String result = ""; // Declaração da variável de resultado

        if (json.contains("read")) { // Verifica se a mensagem é de leitura
            result += this.serv_principal.read(); // Chama o método de leitura da classe Principal
        } else if (json.contains("write")) { // Verifica se a mensagem é de escrita
            String new_fortune = read_new_fortune(json); // Extrai a nova fortuna da mensagem
            
            if (serv_principal.write(new_fortune)) { // Chama o método de escrita da classe Principal
                result += "true"; // Define o resultado como verdadeiro em caso de sucesso
            } else {
                result += "false"; // Define o resultado como falso em caso de falha
            }
        } else {
            result += "method_error"; // Define o resultado como erro de método desconhecido
        }

        return result; // Retorna o JSON analisado
    }

    /**
     * Método para iniciar o servidor RMI.
     */
    public void iniciar() {

        try {
            Registry servidorRegistro; // Declaração do registro de serviços RMI
            try {
                servidorRegistro = LocateRegistry.createRegistry(1099); // Tenta criar o registro na porta 1099
            } catch (java.rmi.server.ExportException e) { // Captura exceção se o registro já estiver iniciado
                System.out.print("Registro já iniciado. Usar o ativo.\n"); // Exibe mensagem de registro já iniciado
            }
            servidorRegistro = LocateRegistry.getRegistry(); // Obtém o registro de serviços existente
            String[] listaAlocados = servidorRegistro.list(); // Obtém a lista de serviços alocados no registro

            // Exibe os Peers ativos no registro
            for (int i = 0; i < listaAlocados.length; i++) {
                System.out.println(listaAlocados[i] + " ativo.");
            }

            SecureRandom sr = new SecureRandom(); // Instancia um gerador de números aleatórios seguro
            Peer peer = alocados.get(sr.nextInt(alocados.size())); // Declaração da variável para armazenar o Peer selecionado
            int tentativas = 0; // Contador de tentativas de seleção de Peer
            boolean repetido = true; // Flag para verificar se o Peer selecionado já está ativo
            boolean cheio = false; // Flag para verificar se o registro de serviços está cheio

            // Loop para selecionar um Peer não repetido e não ativo
            while (repetido && !cheio) {
                repetido = false; // Inicializa a flag de repetição como falsa
                peer = alocados.get(sr.nextInt(alocados.size())); // Seleciona um Peer aleatório
                // Verifica se o Peer selecionado já está ativo
                for (int i = 0; i < listaAlocados.length && !repetido; i++) {
                    if (listaAlocados[i].equals(peer.getNome())) {
                        System.out.println(peer.getNome() + " ativo. Tentando próximo..."); // Exibe mensagem de Peer ativo
                        repetido = true; // Define a flag de repetição como verdadeira
                        tentativas = i + 1; // Incrementa o contador de tentativas
                    }
                }

                // Verifica se o registro está cheio (todos os Peers estão alocados)
                if (listaAlocados.length > 0 && tentativas == alocados.size()) {
                    cheio = true; // Define a flag de registro cheio como verdadeira
                }
            }

            // Se o registro estiver cheio, exibe mensagem de sistema cheio e encerra o processo
            if (cheio) {
                System.out.println("Sistema cheio. Tente mais tarde.");
                System.exit(1); // Encerra o processo
            }

            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); // Cria um skeleton para o novo Peer
            servidorRegistro.rebind(peer.getNome(), skeleton); // Registra o novo Peer no registro de serviços
            System.out.print(peer.getNome() + " Servidor RMI: Aguardando conexões..."); // Exibe mensagem de ativação do Peer

        } catch (Exception e) { // Captura exceções genéricas
            e.printStackTrace(); // Exibe o rastreamento da pilha de exceções
        }
    }

    /**
     * Método principal que instancia o servidor e inicia o registro de
     * serviços.
     *
     * @param args Os argumentos da linha de comando (não utilizados neste
     * contexto).
     */
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl(); // Instancia o servidor
        servidor.iniciar(); // Inicia o registro de serviços
    }
}
