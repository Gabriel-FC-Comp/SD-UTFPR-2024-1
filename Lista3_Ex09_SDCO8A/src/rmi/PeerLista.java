package rmi;


/**
 * Lab05: Sistema P2P
 * Autor: Gabriel Finger Conte
 * 
 * Adaptado de Lucio A. Rocha
 *
 * Referencias:
 * https://docs.oracle.com/javase/tutorial/essential/io
 * http://fortunes.cat-v.org/
 */

public enum PeerLista {

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
    },
    PEER4 {
        @Override
        public String getNome() {
            return "PEER4";
        }
    };

    public String getNome() {
        return "NULO";
    }
}
