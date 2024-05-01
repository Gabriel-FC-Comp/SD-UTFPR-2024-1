
/**
 * Laboratorio 3
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

import java.io.*; // Importa as classes para manipulação de arquivos
import java.nio.file.Path; // Importa a classe Path para lidar com caminhos de arquivo
import java.nio.file.Paths; // Importa a classe Paths para criar instâncias de Path
import java.security.SecureRandom; // Importa a classe SecureRandom para geração de números aleatórios seguros
import java.util.HashMap; // Importa a classe HashMap para mapeamento de chave-valor

/**
 * Esta classe representa a classe principal que contém métodos para leitura e escrita de fortunas em um arquivo.
 */
public class Principal {

    public final static Path path = Paths.get("src\\fortune-br.txt"); // Caminho do arquivo de fortunas
    private int NUM_FORTUNES = 0; // Número total de fortunas

    private FileReader fr; // Instância da classe FileReader para manipulação de arquivos

    /**
     * Classe interna para leitura e escrita de arquivos.
     */
    public class FileReader {

        /**
         * Método para contar o número de fortunas no arquivo.
         * @return O número de fortunas no arquivo.
         * @throws FileNotFoundException Se o arquivo não for encontrado.
         */
        public int countFortunes() throws FileNotFoundException {

            int lineCount = 0; // Contador de linhas

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString())); // Cria um fluxo de entrada para o arquivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) { // Cria um leitor de arquivo

                String line = ""; // Inicializa a linha como uma string vazia
                while (!(line == null)) { // Loop enquanto a linha não for nula

                    if (line.equals("%")) { // Verifica se a linha é um separador de fortuna
                        lineCount++; // Incrementa o contador de linhas
                    }

                    line = br.readLine(); // Lê a próxima linha

                }// fim while

                System.out.println(lineCount); // Exibe o número de fortunas
            } catch (IOException e) { // Captura exceções de E/S
                System.out.println("SHOW: Excecao na leitura do arquivo."); // Exibe uma mensagem de erro
            }
            return lineCount; // Retorna o número de fortunas
        }

        /**
         * Método para analisar o arquivo e armazenar as fortunas em um HashMap.
         * @param hm O HashMap para armazenar as fortunas.
         * @throws FileNotFoundException Se o arquivo não for encontrado.
         */
        public void parser(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString())); // Cria um fluxo de entrada para o arquivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) { // Cria um leitor de arquivo

                int lineCount = 0; // Contador de linhas

                String line = ""; // Inicializa a linha como uma string vazia
                while (!(line == null)) { // Loop enquanto a linha não for nula

                    if (line.equals("%")) { // Verifica se a linha é um separador de fortuna
                        lineCount++; // Incrementa o contador de linhas
                    }

                    line = br.readLine(); // Lê a próxima linha
                    StringBuffer fortune = new StringBuffer(); // Cria um StringBuffer para armazenar a fortuna
                    while (!(line == null) && !line.equals("%")) { // Loop enquanto a linha não for nula e não for um separador
                        fortune.append(line + "\n"); // Adiciona a linha à fortuna
                        line = br.readLine(); // Lê a próxima linha
                    }

                    hm.put(lineCount, fortune.toString()); // Adiciona a fortuna ao HashMap

                    //System.out.println(lineCount);
                }// fim while

                NUM_FORTUNES = lineCount; // Define o número total de fortunas
            } catch (IOException e) { // Captura exceções de E/S
                System.out.println("SHOW: Excecao na leitura do arquivo."); // Exibe uma mensagem de erro
            }
        }

        /**
         * Método para ler uma fortuna aleatória do arquivo.
         * @param hm O HashMap contendo as fortunas.
         * @return A fortuna lida.
         * @throws FileNotFoundException Se o arquivo não for encontrado.
         */
        public String read(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            String result = "-2"; // Valor padrão para resultado

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString())); // Cria um fluxo de entrada para o arquivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) { // Cria um leitor de arquivo

                SecureRandom sr = new SecureRandom(); // Cria uma instância de SecureRandom para geração de números aleatórios

                int lineSelected = sr.nextInt(NUM_FORTUNES); // Gera um número aleatório baseado no número de fortunas

                System.out.println("Read line ".concat(Integer.toString(lineSelected))); // Exibe a linha lida

                System.out.println(hm.get(lineSelected)); // Exibe a fortuna lida

                result = hm.get(lineSelected); // Define o resultado como a fortuna lida

            } catch (IOException e) { // Captura exceções de E/S
                System.out.println("SHOW: Excecao na leitura do arquivo."); // Exibe uma mensagem de erro
            }
            return result; // Retorna a fortuna lida
        }

        /**
         * Método para adicionar uma nova fortuna ao arquivo.
         * @param hm O HashMap contendo as fortunas.
         * @param fortune A nova fortuna a ser adicionada.
         * @return Verdadeiro se a adição for bem-sucedida, falso caso contrário.
         * @throws FileNotFoundException Se o arquivo não for encontrado.
         */
        public Boolean write(HashMap<Integer, String> hm, String fortune)
                throws FileNotFoundException {

            OutputStream os = new BufferedOutputStream(new FileOutputStream(
                    path.toString(), true)); // Cria um fluxo de saída para o arquivo (modo de anexo)
            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(os))) { // Cria um escritor de arquivo

                NUM_FORTUNES++; // Incrementa o número total de fortunas

                hm.put(NUM_FORTUNES, fortune); // Adiciona a nova fortuna ao HashMap
                
                System.out.println("Wrote: \n");
                System.out.println(hm.get(NUM_FORTUNES)); // Exibe a nova fortuna

                //Append file
                bw.append("%\n" + fortune); // Adiciona a nova fortuna ao arquivo
                return true; // Retorna verdadeiro para indicar sucesso
            } catch (IOException e) { // Captura exceções de E/S
                System.out.println("SHOW: Excecao na leitura do arquivo."); // Exibe uma mensagem de erro
            }// try-catch
            return false; // Retorna falso para indicar falha
        }// write
    }

    /**
     * Construtor da classe Principal.
     */
    public Principal(){
        this.fr = new FileReader(); // Inicializa o FileReader
    }// Construtor
    
    /**
     * Método para escrever uma nova fortuna no arquivo.
     * @param fortune A nova fortuna a ser adicionada.
     * @return Verdadeiro se a adição for bem-sucedida, falso caso contrário.
     */
    public Boolean write(String fortune) {
        fr = new FileReader(); // Inicializa o FileReader
        try {
            NUM_FORTUNES = fr.countFortunes(); // Obtém o número total de fortunas
            HashMap hm = new HashMap<Integer, String>(); // Cria um HashMap para armazenar as fortunas
            fr.parser(hm); // Analisa o arquivo e popula o HashMap
            fr.read(hm); // Lê uma fortuna aleatória do HashMap
            return fr.write(hm, fortune); // Adiciona a nova fortuna ao arquivo
        } catch (FileNotFoundException e) { // Captura exceções de arquivo não encontrado
            System.err.println("Erro ao tentar escrever a nova fortuna!");
            e.printStackTrace(); // Imprime o rastreamento da pilha de exceções
        }
        return false;
    }

    /**
     * Método para ler uma fortuna aleatória do arquivo.
     * @return A fortuna lida.
     */
    public String read() {
        String result = "-1"; // Valor padrão para resultado

        fr = new FileReader(); // Inicializa o FileReader
        try {
            NUM_FORTUNES = fr.countFortunes(); // Obtém o número total de fortunas
            HashMap hm = new HashMap<Integer, String>(); // Cria um HashMap para armazenar as fortunas
            fr.parser(hm); // Analisa o arquivo e popula o HashMap
            result = fr.read(hm); // Lê uma fortuna aleatória do HashMap
        } catch (FileNotFoundException e) { // Captura exceções de arquivo não encontrado
            System.err.println("Erro ao tentar ler a nova fortuna!");
            e.printStackTrace(); // Imprime o rastreamento da pilha de exceções
        }
        return result; // Retorna a fortuna lida
    }

}