package rmi;


/**
 * Lab05: Sistema P2P
 *
 * Autor: Gabriel Finger Conte
 * 
 * Adaptado de Lucio A. Rocha
 *
 * Referencias:
 * https://docs.oracle.com/javase/tutorial/essential/io
 * http://fortunes.cat-v.org/
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Peer implements IMensagem {

    /** Lista de pares alocados */
    ArrayList<PeerLista> alocados;

    /**
     * Construtor da classe Peer.
     * Inicializa a lista de pares alocados.
     */
    public Peer() {
        alocados = new ArrayList<>();
    }

    /**
     * Método remoto para enviar mensagem.
     * @param mensagem Mensagem a ser enviada.
     * @return Mensagem de resposta.
     * @throws RemoteException Se ocorrer um erro remoto.
     */
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
            System.out.println("@Servidor > Mensagem recebida: " + mensagem.getMensagem());
            resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
        } catch (Exception e) {
            e.printStackTrace();
            resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
        }
        return resposta;
    }

    /**
     * Analisa mensagem JSON e executa operações.
     * @param json Mensagem JSON.
     * @return Resultado da análise da mensagem.
     */
    public String parserJSON(String json) {
        String result = "false";

        String fortune = "-1";

        String[] v = json.split(":");
        // System.out.println(">>>" + v[1]);
        String[] v1 = v[1].split("\"");
        // System.out.println(">>>" + v1[1]);
        if (v1[1].equals("write")) {
            String[] p = json.split("\\[");
            // System.out.println(p[1]);
            String[] p1 = p[1].split("]");
            // System.out.println(p1[0]);
            String[] p2 = p1[0].split("\"");
            // System.out.println(p2[1]);
            fortune = p2[1];

            // Write in file
            Principal pv2 = new Principal();
            pv2.write(fortune);
        } else if (v1[1].equals("read")) {
            // Read file
            Principal pv2 = new Principal();
            fortune = pv2.read();
        }

        result = "{\n" + "\"result\": \"" + fortune + "\"" + "}";
        System.out.println("@Servidor > Resposta enviada: " + result);

        return result;
    }

    /**
     * Inicia o servidor e cliente RMI.
     */
    public void iniciar() {

        try {
            //Adquire aleatoriamente um ID do PeerList
            List<PeerLista> listaPeers = new ArrayList<>();
            for (PeerLista peer : PeerLista.values()) {
                listaPeers.add(peer);
            }

            Registry servidorRegistro;
            try {
                servidorRegistro = LocateRegistry.createRegistry(1099);
            } catch (java.rmi.server.ExportException e) { //Registro jah iniciado 
                System.out.print("Registro jah iniciado. Usar o ativo.\n");
            }
            servidorRegistro = LocateRegistry.getRegistry(); //Registro eh unico para todos os peers
            String[] listaAlocados = servidorRegistro.list();
            for (int i = 0; i < listaAlocados.length; i++) {
                System.out.println(listaAlocados[i] + " ativo.");
            }

            SecureRandom sr = new SecureRandom();
            PeerLista peer = listaPeers.get(sr.nextInt(listaPeers.size()));

            int tentativas = 0;
            boolean repetido = true;
            boolean cheio = false;
            while (repetido && !cheio) {
                repetido = false;
                peer = listaPeers.get(sr.nextInt(listaPeers.size()));
                for (int i = 0; i < listaAlocados.length && !repetido; i++) {

                    if (listaAlocados[i].equals(peer.getNome())) {
                        System.out.println(peer.getNome() + " ativo. Tentando proximo...");
                        repetido = true;
                        tentativas = i + 1;
                    }

                }
                //System.out.println(tentativas+" "+listaAlocados.length);

                //Verifica se o registro estah cheio (todos alocados)
                if (listaAlocados.length > 0
                        && //Para o caso inicial em que nao ha servidor alocado,
                        //caso contrario, o teste abaixo sempre serah true
                        tentativas == listaPeers.size()) {
                    cheio = true;
                }
            }

            if (cheio) {
                System.out.println("Sistema cheio. Tente mais tarde.");
                System.exit(1);
            }

            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)            

            servidorRegistro.rebind(peer.getNome(), skeleton);
            System.out.println(peer.getNome() + " Servidor RMI: Aguardando conexoes...");

            //---Cliente RMI
            new ClienteRMI().iniciarCliente();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Método principal para iniciar o servidor Peer.
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        // Cria uma instância do servidor Peer
        Peer servidor = new Peer();
        // Inicia o servidor
        servidor.iniciar();
    }
}
