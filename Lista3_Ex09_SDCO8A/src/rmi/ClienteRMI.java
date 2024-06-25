package rmi;

/**
 * Lab05: Sistema P2P
 *
 * Autor: Gabriel Finger Conte
 * 
 * Adaptado de Lucio A. Rocha
 *
 * Referencias: https://docs.oracle.com/javase/tutorial/essential/io
 * http://fortunes.cat-v.org/
 * https://docs.oracle.com/javase%2F8%2Fdocs%2Fapi%2F%2F/java/util/concurrent/CountDownLatch.html
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingUtilities.html#invokeLater(java.lang.Runnable)
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;
import ui.clientUI;

/**
 * Classe que implementa a lógica do cliente RMI para interagir com os pares do sistema P2P.
 */
public class ClienteRMI {

    /**
     * Método para iniciar a lógica do cliente RMI.
     */
    public void iniciarCliente() {

        // Cria uma lista de pares disponíveis
        List<PeerLista> listaPeers = new ArrayList<PeerLista>();
        for (PeerLista peer : PeerLista.values()) {
            listaPeers.add(peer);
        }

        try {

            // Obtém o registro RMI
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);

            // Cria um latch de contagem para aguardar o fechamento da interface gráfica
            CountDownLatch latch = new CountDownLatch(1);

            // Inicia a interface gráfica do cliente em uma nova thread
            SwingUtilities.invokeLater(() -> {
                clientUI connectionUI = new clientUI(registro, listaPeers);
                connectionUI.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        latch.countDown(); // Notify main thread that the UI is closing
                    }
                });
            });

            // Aguarda o fechamento da interface gráfica
            latch.await(); // Wait until the UI is closed
            
            // Obtém o peer conectado e o stub do cliente
            clientUI connectionUI = clientUI.getInstance();
            PeerLista connectedPeer = connectionUI.getConnectedPeer();
            IMensagem stub = connectionUI.getStub();

            // Verifica se a conexão foi estabelecida corretamente
            if (connectedPeer == null || stub == null) {
                System.err.println("\n\nError - UI closed without connecting to a peer!");
                System.err.println("Killing ClientRMI...");
                System.exit(1);
            }
            
            // Lê opções do usuário para interagir com o peer
            String opcao = "";
            Scanner leitura = new Scanner(System.in);
            do {
                System.out.println("1) Read");
                System.out.println("2) Write");
                System.out.println("x) Exit");
                System.out.print(">> ");
                opcao = leitura.next();
                switch (opcao) {
                    case "1": {
                        Mensagem mensagem = new Mensagem("", opcao);
                        Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'read'
                        System.out.println("@Cliente > Resposta recebida: " + resposta.getMensagem());
                        break;
                    }
                    case "2": {
                        //Monta a mensagem                		
                        System.out.print("Add fortune: ");
                        String fortune = leitura.next();
                        Mensagem mensagem = new Mensagem(fortune, opcao);
                        Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
                        System.out.println("@Cliente > Resposta recebida: " + resposta.getMensagem());
                        break;
                    }
                }
            } while (!opcao.equals("x"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*public static void main(String[] args) {
                
    	new ClienteRMI().iniciarCliente();
    	        
    }*/
}