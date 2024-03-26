
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private static Socket socket;
    private static ServerSocket server;

    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private int porta = 1025;

    // Integrando o código do exercício anterior, adaptado
    // Determinando o caminho para as "fortunas"
    public final static Path path = Paths
            .get("src//fortune-br.txt");
    // Controle do número de fortunas
    private int NUM_FORTUNES = 0;

    // Hashmap com as fortunas
    private HashMap hm = new HashMap<Integer, String>();
    
    // Classe interna para leitura do arquivo de fortunas
    public class FileReader {

        // Função para contagem das fortunas
        public int countFortunes() throws FileNotFoundException {

            // Variável para contar o número de linhas lidas
            int lineCount = 0;

            // Imputstream para leitura dos dados, abrindo o arquivo
            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));

            try ( BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%")) {
                        lineCount++;
                    }

                    line = br.readLine();

                }// fim while

                //System.out.println(lineCount);
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
            return lineCount;
        }

        public void parser(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));
            try ( BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) {

                int lineCount = 0;

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%")) {
                        lineCount++;
                    }

                    line = br.readLine();
                    StringBuffer fortune = new StringBuffer();
                    while (!(line == null) && !line.equals("%")) {
                        fortune.append(line + "\n");
                        line = br.readLine();
                        // System.out.print(lineCount + ".");
                    }

                    hm.put(lineCount, fortune.toString());
                    //System.out.println(fortune.toString());

                    //System.out.println(lineCount);
                }// fim while

            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public String read(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            //SEU CODIGO AQUI
            // Demarca no Terminal a função
            System.out.println("############## Read ##########");

            // Verifica se existem fortunas
            if (!hm.isEmpty()) {
                // Instancia um gerador segudo de números aleatórios
                SecureRandom rGen = new SecureRandom();
                // Define uma seed aleatória de 64 bytes para o gerador
                rGen.setSeed(rGen.generateSeed(64));

                // Pega o número de fortunas lidas do arquivo
                int num_fortunes = hm.size();

                // Escolhe uma fortuna aleatória
                int rand_fortune_key = rGen.nextInt(num_fortunes);
                // Pega a fortuna aleatória
                String rand_fortune = hm.get(rand_fortune_key);

                // Verifica se a fortuna selecionada existe mesmo
                if (rand_fortune != null) {
                    // Exibe no terminal a fortuna aleatória lida
                    // System.out.println(rand_fortune);
                    return (rand_fortune);
                }// rand_fortune != null

            }// !hm.isEmpty()

            // Informa que houve um problema selecionando a fortuna
            return ("Infelizmente a fortuna selecionada não está disponível!");
        }// read

        public boolean write(HashMap<Integer, String> hm,String fortuna)
                throws FileNotFoundException {

            //SEU CODIGO AQUI
            // Demarca no Terminal a função
            System.out.println("############## Write ##########");

            // Instancia o objeto para abrir o arquivo
            FileWriter file;

            // Tenta abrir o arquivo de fortunas para adicionar conteúdo ao final do mesmo
            try {
                file = new FileWriter(path.toString(), Boolean.TRUE);

                // Tenta adicionar a nova fortuna no arquivo
                file.append(fortuna + "\n%\n");

                // Tenta fechar o arquivo para salvar as alterações
                file.close();
                
                // Retorna indicando sucesso
                return true;
            } catch (IOException ex) {
                System.err.println("Erro ao adicionar a nova fortuna!");
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return false;
        }// write
    }// Flie Reader
    
    // Instancia um FileReader
    private final FileReader fr = new FileReader();

    private String read_new_fortune(String client_message){
        int new_fortune_start_index = client_message.indexOf("\"args\":[\"") + "\"args\":[\"".length();
        int new_fortune_end_index = client_message.length()-"\"]\n}".length();
        
        return client_message.substring(new_fortune_start_index,new_fortune_end_index);
    }
    
    public String parser(String client_message){
        
        String parser_response = "";
        
        if(client_message.contains("read")){
            try {
                parser_response = "{\n\"result\":\"" + fr.read(this.hm) + "\"\n}";
            } catch (FileNotFoundException ex) {
                System.err.println("Erro ao tentar ler uma fortuna!");
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }else if(client_message.contains("write")){
            try {
                String new_fortune = read_new_fortune(client_message);
                if(fr.write(hm, new_fortune)){
                    parser_response = "{\n\"result\":\"true\"\n}";
                }else{
                    parser_response = "{\n\"result\":\"false\"\n}";
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Erro ao tentar escrever a nova fortuna!");
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }else{
            parser_response = "{\"error\":\"Método não suportado\"\n}";
        }
        
        //System.out.println("Parser Response" + parser_response);
        return parser_response;
    }
    
    public void iniciar() {

        // Lê os dados das fortunas
        try {
            this.NUM_FORTUNES = fr.countFortunes();
            fr.parser(this.hm);
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler as fortunas!");
            e.printStackTrace();
            System.exit(1);
        }

        // Inicializa o servidor
        System.out.println("Servidor iniciado na porta: " + porta);
        try {

            // Criar porta de recepcao
            server = new ServerSocket(porta);
            
            socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes
            
            // Mantém o servidor aberto e funcionado
            while (true) {
                

                // Criar os fluxos de entrada e saida
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());

                // Lê a mensagem do cliente
                String client_message = entrada.readUTF();
                
                // Processa a mensagem do cliente
                String parser_response = parser(client_message);
                
                // Envia a resposta para o cliente
                saida.writeUTF(parser_response);

            }// while

        }catch (Exception e) {
            System.out.println("Conexão com o cliente perdida, encerrando servidor!");
        }
    }

    public static void main(String[] args) {

        new Servidor().iniciar();

    }

}
