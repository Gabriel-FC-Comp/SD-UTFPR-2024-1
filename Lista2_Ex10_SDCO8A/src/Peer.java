
/**
 * Laboratorio 4
 * Autor: Gabriel Finger Conte
 * Adaptado de: Lucio Agostinho Rocha
 * Ultima atualizacao: 01/05/2023
 */

/**
 * A enumeração Peer representa os diferentes peers disponíveis para conexão.
 * Cada peer tem um nome associado.
 */
public enum Peer {

    // Definição dos peers disponíveis
    PEER1 {
        @Override
        public String getNome() {
            return "PEER1";
        }
    },
    PEER2 {
        @Override
        public String getNome() {
            return "PEER2";
        }
    },
    PEER3 {
        @Override
        public String getNome() {
            return "PEER3";
        }
    };

    /**
     * Método abstrato para obter o nome do peer. Cada constante da enumeração
     * deve implementar este método.
     *
     * @return O nome do peer.
     */
    public abstract String getNome();
}
