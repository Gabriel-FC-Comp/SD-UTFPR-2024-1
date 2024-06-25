/**
 * Título do Arquivo: Book.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa um livro.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.data;

/**
 * Classe que representa um livro.
 */
public class Book {
    
    private Reserva reserva;
    private String titulo;

    /**
     * Construtor que inicializa o livro com um título.
     * 
     * @param titulo o título do livro.
     */
    public Book(String titulo) {
        unsetReserva();
        setTitulo(titulo);
    }// Book

    /**
     * Retorna um feedback de leitura do livro.
     * 
     * @return uma string no formato "\"read\": \"título\",\\n".
     */
    public String getReadFeedback() {
        StringBuilder ret = new StringBuilder();
        ret.append("\"read\": \"");
        ret.append(this.titulo);
        ret.append("\",\n");
        return ret.toString();
    }// getReadFeedback
    
    /**
     * Define a reserva do livro.
     * 
     * @param reserva a reserva a ser associada ao livro.
     */
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }// setReserva
    
    /**
     * Remove a reserva do livro.
     */
    public void unsetReserva() {
        this.reserva = null;
    }// unsetReserva

    /**
     * Define o título do livro.
     * 
     * @param titulo o título do livro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }// setTitulo

    /**
     * Retorna a reserva associada ao livro.
     * 
     * @return a reserva do livro.
     */
    public Reserva getReserva() {
        return reserva;
    }// getReserva

    /**
     * Retorna o título do livro.
     * 
     * @return o título do livro.
     */
    public String getTitulo() {
        return titulo;
    }// getTitulo

    /**
     * Retorna a representação em string do livro.
     * 
     * @return o título do livro.
     */
    @Override
    public String toString() {
        return this.titulo;
    }// toString
    
}// Book
