
/**
 * Laboratorio 3  
 * Autor: Lucio Agostinho Rocha
 * Ultima atualizacao: 04/04/2023
 */

import java.rmi.Remote; // Importa a interface Remote para comunicação remota
import java.rmi.RemoteException; // Importa a classe RemoteException para lidar com exceções de comunicação remota

/**
 * Esta interface define o contrato para a comunicação RMI entre cliente e servidor.
 */
public interface IMensagem extends Remote {

    /**
     * Método para enviar uma mensagem ao servidor.
     * @param mensagem A mensagem a ser enviada.
     * @return A resposta do servidor encapsulada em um objeto Mensagem.
     * @throws RemoteException Se ocorrer um erro de comunicação remota.
     */
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;

}

