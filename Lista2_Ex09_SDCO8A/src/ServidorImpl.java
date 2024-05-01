
/**
 * Laboratorio 3
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

import java.rmi.RemoteException; // Importa a exceção RemoteException para lidar com exceções de comunicação remota
import java.rmi.registry.LocateRegistry; // Importa a classe LocateRegistry para localizar o registro RMI
import java.rmi.registry.Registry; // Importa a classe Registry para o registro RMI
import java.rmi.server.UnicastRemoteObject; // Importa a classe UnicastRemoteObject para exportar objetos remotos

/**
 * Esta classe implementa a interface IMensagem e representa a implementação do servidor RMI.
 */
public class ServidorImpl implements IMensagem {

    Principal serv_principal; // Instância da classe Principal para manipulação de arquivos

    /**
     * Construtor da classe ServidorImpl.
     * Inicializa o servidor.
     */
    public ServidorImpl() {
        this.serv_principal = new Principal(); // Instancia o servidor
    }
    
    //Cliente: invoca o metodo remoto 'enviar'
    //Servidor: invoca o metodo local 'enviar'
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
            resposta = new Mensagem(parserJSON(mensagem.getMensagem())); // Analisa e responde à mensagem recebida
            
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o rastreamento da pilha de exceções
            resposta = new Mensagem("{\n" + "\"result\": false\n" + "}"); // Cria uma mensagem de resposta de erro
        }
        return resposta; // Retorna a resposta do servidor
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
     * Método para analisar um JSON e realizar a operação correspondente.
     * @param json O JSON a ser analisado.
     * @return O resultado da operação em formato JSON.
     */
    public String parserJSON(String json) {
        String result = "";

        if(json.contains("read")){
            result += this.serv_principal.read(); // Realiza operação de leitura
        } else if(json.contains("write")){
            String new_fortune = read_new_fortune(json); // Extrai a nova fortuna da mensagem
            if(serv_principal.write(new_fortune)){
                result += "true"; // Indica sucesso na operação de escrita
            } else {
                result += "false"; // Indica falha na operação de escrita
            }
        } else {
            result += "method_error"; // Indica erro de método
        }
        
        return result; // Retorna o resultado da operação em formato JSON
    }

    /**
     * Método para iniciar o servidor RMI.
     * Cria o registro RMI e vincula o objeto remoto ao registro.
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