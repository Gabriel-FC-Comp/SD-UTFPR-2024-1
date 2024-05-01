
/**
 * Laboratorio 4
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 29/04/2023
 */

import java.rmi.Remote; // Importa a interface Remote para suporte a comunicação remota
import java.rmi.RemoteException; // Importa a exceção RemoteException para lidar com exceções de comunicação remota

/**
 * A interface IMensagem define o contrato para enviar mensagens remotamente.
 */
public interface IMensagem extends Remote {

    /**
     * Método para enviar uma mensagem remotamente.
     *
     * @param mensagem A mensagem a ser enviada.
     * @return A resposta da mensagem enviada.
     * @throws RemoteException Se ocorrer um erro durante a comunicação remota.
     */
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;
}
