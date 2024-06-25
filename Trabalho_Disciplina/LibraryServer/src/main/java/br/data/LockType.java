/**
 * Título do Arquivo: LockType.java
 * 
 * Descrição Breve: Este arquivo contém a enumeração que representa os tipos de bloqueio.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */


package br.data;

/**
 * Enumeração que representa os tipos de bloqueio.
 */
public enum LockType {
    
    /**
     * Bloqueio compartilhado.
     */
    SHARED("r"),

    /**
     * Bloqueio exclusivo.
     */
    EXCLUSIVE("w");
    
    private final String type;

    /**
     * Construtor que inicializa o tipo de bloqueio.
     * 
     * @param type o tipo de bloqueio.
     */
    LockType(String type) {
        this.type = type;
    }// LockType

    /**
     * Retorna o tipo de bloqueio como uma string.
     * 
     * @return o tipo de bloqueio.
     */
    @Override
    public String toString() {
        return type;
    }// toString

}
