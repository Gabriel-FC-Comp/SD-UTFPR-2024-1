
/**
 * Lab0: Leitura de Base de Dados N�o-Distribuida
 * Autor: Gabriel Finger Conte
 * Ultima atualizacao: 26/03/2024
 * 
 * Adaptado de Lucio A. Rocha 
 *
 * Referencias: https://docs.oracle.com/javase/tutorial/essential/io
 *
 */
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal_v0 {

    // Determinando o caminho para as "fortunas"
    public final static Path path = Paths
            .get("src//fortune-br.txt");
    // Controle do número de fortunas
    private int NUM_FORTUNES = 0;

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

                System.out.println(lineCount);
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
                    System.out.println(fortune.toString());

                    System.out.println(lineCount);
                }// fim while

            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public void read(HashMap<Integer, String> hm)
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
                    System.out.println(rand_fortune);
                } else {
                    // Informa que houve um problema selecionando a fortuna
                    System.out.println("Infelizmente a fortuna selecionada não está disponível!");
                }// rand_fortune != null

            }// !hm.isEmpty()

        }// read

        public void write(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            //SEU CODIGO AQUI
            // Demarca no Terminal a função
            System.out.println("############## Write ##########");
            
            // Instancia o objeto para abrir o arquivo
            FileWriter file = null;
            
            // Tenta abrir o arquivo de fortunas para adicionar conteúdo ao final do mesmo
            try {
                file = new FileWriter(path.toString(), Boolean.TRUE);

                // Cria o scanner para ler a nova fortuna no terminal
                Scanner sc = new Scanner(System.in);

                // Espera o usuário inserir a nova fortuna
                StringBuffer new_fortune = new StringBuffer();
                String inserir_flag = "n";
                while (!inserir_flag.equalsIgnoreCase("S")) {
                    // Reinicia o construtor da nova fortuna
                    new_fortune = new StringBuffer();
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
                    System.out.println("\"" + new_fortune.toString().substring(0,new_fortune.toString().length()-1) + "\" ? (s/n)");
                    inserir_flag = sc.nextLine().strip();
                    
                }// while

                // Tenta adicionar a nova fortuna no arquivo
                file.append(new_fortune.toString().substring(0,new_fortune.toString().length()-1) + "\n%\n");
                
                // Tenta fechar o arquivo para salvar as alterações
                file.close();

            } catch (IOException ex) {
                System.err.println("Erro ao adicionar a nova fortuna!");
                Logger.getLogger(Principal_v0.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }

        }// write
    }

    public void iniciar() {
        
        FileReader fr = new FileReader();
        try {
            NUM_FORTUNES = fr.countFortunes();
            HashMap hm = new HashMap<Integer, String>();
            fr.parser(hm);
            fr.read(hm);
            fr.write(hm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Principal_v0().iniciar();
    }

}
