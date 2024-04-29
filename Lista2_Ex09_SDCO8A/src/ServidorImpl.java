
/**
 * Laboratorio 3
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 29/04/2023
 */

import java.io.FileNotFoundException; // Importa a exceção FileNotFoundException para lidar com arquivos não encontrados
import java.rmi.RemoteException; // Importa a exceção RemoteException para lidar com exceções de comunicação remota
import java.rmi.registry.LocateRegistry; // Importa a classe LocateRegistry para localizar o registro RMI
import java.rmi.registry.Registry; // Importa a classe Registry para o registro RMI
import java.rmi.server.UnicastRemoteObject; // Importa a classe UnicastRemoteObject para exportar objetos remotos
import java.util.HashMap; // Importa a classe HashMap para mapeamento de chave-valor

/**
 * Esta classe implementa a interface IMensagem e representa a implementação do servidor RMI.
 */
public class ServidorImpl implements IMensagem {

    HashMap<Integer, String> fortunes; // HashMap para armazenar as fortunas
    Principal serv_principal; // Instância da classe Principal para manipulação de arquivos

    /**
     * Construtor da classe ServidorImpl.
     */
    public ServidorImpl() {
        // Instancia o servidor
        this.serv_principal = new Principal();
        
        // Instancia o HashMap de fortunas
        this.fortunes = new HashMap<>();
        
        // Tenta ler os dados do arquivo
        try {
            this.serv_principal.getFileReader().parser(this.fortunes);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha de exceções
        }

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
     * Método para analisar a mensagem do cliente e realizar as ações apropriadas.
     * @param client_message A mensagem recebida do cliente.
     * @return A resposta do servidor.
     */
    public String parser(String client_message){
        
        String parser_response = "";
        
        if(client_message.contains("read")){
            try {
                parser_response += this.serv_principal.getFileReader().read(this.fortunes);
            } catch (FileNotFoundException ex) {
                System.err.println("Erro ao tentar ler uma fortuna!");
                ex.printStackTrace(); // Imprime o rastreamento da pilha de exceções
                System.exit(1); // Encerra o programa com código de erro
            }
        } else if(client_message.contains("write")){
            try {
                String new_fortune = read_new_fortune(client_message);
                if(serv_principal.getFileReader().write(this.fortunes, new_fortune)){
                    parser_response += "true";
                } else {
                    parser_response += "false";
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Erro ao tentar escrever a nova fortuna!");
                ex.printStackTrace(); // Imprime o rastreamento da pilha de exceções
                System.exit(1); // Encerra o programa com código de erro
            }
        } else {
            parser_response += "method_error";
        }
        
        return parser_response; // Retorna a resposta do parser
    }

    
    //Cliente: invoca o metodo remoto 'enviar'
    //Servidor: invoca o metodo local 'enviar'
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
            resposta = new Mensagem(parserJSON(parser(mensagem.getMensagem())));
            
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o rastreamento da pilha de exceções
            resposta = new Mensagem("{\n" + "\"result\": false\n" + "}"); // Cria uma mensagem de resposta de erro
        }
        return resposta; // Retorna a resposta do servidor
    }

    /**
     * Método para analisar um JSON (neste caso, apenas retorna o JSON original).
     * @param json O JSON a ser analisado.
     * @return O JSON analisado.
     */
    public String parserJSON(String json) {
        String result = json;

        return result; // Retorna o JSON analisado
    }

    /**
     * Método para iniciar o servidor RMI.
     */
    public void iniciar() {

        try {
            Registry servidorRegistro = LocateRegistry.createRegistry(1099); // Cria um registro RMI na porta 1099
            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); // Exporta o objeto remoto
            servidorRegistro.rebind("servidorFortunes", skeleton); // Vincula o objeto remoto ao registro
            System.out.println("Servidor RMI: Aguardando conexoes..."); // Exibe uma mensagem indicando que o servidor está aguardando conexões

        } catch (Exception e) {
            e.printStackTrace(); // Imprime o rastreamento da pilha de exceções
        }

    }

    /**
     * Método principal para iniciar o servidor.
     * @param args Os argumentos da linha de comando (não utilizados neste caso).
     */
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl(); // Cria uma instância do servidor
        servidor.iniciar(); // Inicia o servidor
    }
}
