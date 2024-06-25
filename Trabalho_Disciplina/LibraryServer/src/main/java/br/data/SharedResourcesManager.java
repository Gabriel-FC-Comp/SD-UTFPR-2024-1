/**
 * Título do Arquivo: SharedResourcesManager.java
 * 
 * Descrição Breve: Este arquivo contém a implementação do gerenciador de recursos compartilhados.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */

package br.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Gerenciador de recursos compartilhados.
 */
public class SharedResourcesManager {

    /* Variáveis de Instância */
    private HashMap<String, Book> listaLivros;
    private List<CustomLock> listaTrancas;
    private boolean liberandoTrancas;

    /**
     * Construtor padrão do gerenciador de recursos compartilhados.
     */
    public SharedResourcesManager() {
        this.listaTrancas = new ArrayList<>();
        this.listaLivros = new HashMap<>();
        this.liberandoTrancas = false;
    }// SharedResourcesManager

    /* Funções de Acesso ao Dado Compartilhado */

    /**
     * Realiza a leitura de um livro.
     * 
     * @param titulo o título do livro a ser lido.
     * @return o livro lido.
     */
    private Book read(String titulo) {
        return this.listaLivros.get(titulo);
    }// read

    /**
     * Realiza a escrita em um livro.
     * 
     * @param titulo o título do livro a ser escrito.
     * @param nomeCliente o nome do cliente realizando a escrita.
     * @param action a ação a ser realizada (reserva ou cancelamento).
     * @return true se a operação de escrita foi bem-sucedida, false caso contrário.
     */
    private synchronized boolean write(String titulo, String nomeCliente, ActionType action) {
        Book livro = read(titulo);
        if (livro != null) {
            switch (action) {
                case RESERV:
                    Reserva novaReserva = new Reserva(nomeCliente);
                    livro.setReserva(novaReserva);
                    break;
                case CANCEL:
                    livro.unsetReserva();
                    break;
                default:
                    System.err.println("Erro: " + action.toString() + " não corresponde com uma ação de escrita válida!");
                    return false;
            }// switch
        } else {
            System.err.println("Erro: " + titulo + " não existe para reserva!");
            return false;
        }// if-else
        return true;
    }// write

    /* Funções 2PL */

    /**
     * Adquire um bloqueio para uma operação.
     * 
     * @param operacao a operação para a qual o bloqueio está sendo adquirido.
     * @param threadName o nome da thread executando a operação.
     * @return true se o bloqueio foi adquirido com sucesso, false caso contrário.
     */
    private synchronized boolean lockGather(Operation operacao, String threadName) {
        CustomLock novaTranca;
        System.out.print(threadName + ": ");
        switch (operacao.type) {
            case READ:
                novaTranca = new CustomLock(operacao.targetedResource, operacao.clientName);
                break;
            case WRITE:
                novaTranca = new CustomLock(operacao.targetedResource, operacao.clientName, operacao.action.toString());
                break;
            default:
                System.err.println("Erro: " + operacao.type + " não é um tipo de operação válida!");
                return false;
        }// switch
        System.out.println(novaTranca.toString());
        this.listaTrancas.add(novaTranca);
        return true;
    }// lockGathering

    /**
     * Libera os bloqueios após a execução das operações.
     * 
     * @param listaOperacoes a lista de operações executadas.
     * @param threadName o nome da thread executando as operações.
     */
    private synchronized void lockFreeing(List<Operation> listaOperacoes, String threadName) {
        this.liberandoTrancas = true;
        System.out.print(threadName + ": ");
        for (Operation operacao : listaOperacoes) {
            freeLock(operacao);
        }// for

        System.out.println(threadName + ": liberou trancas.");
        notifyAll(); // Avisa as demais threads que acabou
        this.liberandoTrancas = false; // Indica que terminou a liberacao das trancas
    }// lockFreeing

    /**
     * Coloca uma operação na fila de espera se houver conflito.
     * 
     * @param op a operação a ser enfileirada.
     * @param threadName o nome da thread executando a operação.
     * @return true se a operação foi abortada, false caso contrário.
     */
    private synchronized boolean queueOperation(Operation op, String threadName) {
        boolean abortFlag = false;
        boolean timeoutFlag = false;
        Date initTime = new Date();
        System.out.print(threadName + ": ");

        System.out.println("Enfileira: " + op.toString());

        while (this.liberandoTrancas && !timeoutFlag) {
            try {
                wait();
                Date comparisionTime = new Date();
                if ((comparisionTime.getTime() - initTime.getTime()) > 15000) {
                    timeoutFlag = true;
                }// if
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }// try-catch
        }// while

        if (timeoutFlag) {
            abortFlag = true;
            System.out.println(threadName + ": " + op.toString() + "Abortada");
        } else {
            System.out.println(threadName + ": " + op.toString() + "Saiu da Fila.");
        }// if-else

        return abortFlag;
    }// queueOperation

    /**
     * Realiza o escalonamento das operações utilizando o protocolo 2PL.
     * 
     * @param listaOperacoes a lista de operações a serem executadas.
     * @param threadName o nome da thread executando as operações.
     * @return um feedback da execução das operações.
     */
    public String scheduler2PL(List<Operation> listaOperacoes, String threadName) {
        List<Operation> acquiredLocks = new ArrayList<>();
        boolean abortFlag = false;
        StringBuilder feedback = new StringBuilder();

        /* Fase de Crescimento */
        for (Operation op : listaOperacoes) {
            // Verifica se há conflito para adicionar a operação na lista de espera
            while (verifyConflict(op)) {
                abortFlag = queueOperation(op, threadName);
                if (abortFlag) {
                    break;
                }
            }// while

            if (!abortFlag) {
                // Tentar adquirir o bloqueio
                if (!lockGather(op, threadName)) {
                    abortFlag = true;
                    break;
                }// if

                switch (op.type) {
                    case READ:
                        System.out.print(threadName + ": " + op.toString() + " -> ");
                        Book readReturn = read(op.targetedResource);
                        if (readReturn == null) {
                            abortFlag = true;
                            System.out.println("abortada!");
                            break;
                        }// if
                        feedback.append(readReturn.getReadFeedback());
                        System.out.println(readReturn);
                        break;
                    case WRITE:
                        System.out.print(threadName + ": " + op.toString() + " -> ");
                        if (!write(op.targetedResource, op.clientName, op.action)) {
                            abortFlag = true;
                            System.out.println("abortada!");
                            break;
                        }// if
                        feedback.append("");
                        System.out.println("sucesso");
                        break;
                    default:
                        System.err.println("Erro: tentando executar operação inválida: " + op.type);
                        abortFlag = true;
                }// switch
                acquiredLocks.add(op); // Adiciona operação à lista de bloqueios adquiridos
            } else {
                break;
            }// if-else
        }// for

        /* Fase de Encolhimento */
        lockFreeing(acquiredLocks, threadName);
        if (abortFlag) {
            // Indica que foi abortada
            System.out.println(threadName + ": Transação abortada.");
            feedback = new StringBuilder();
            feedback.append("abort\",");
        } else {
            // Indica que foi concluída com sucesso
            System.out.println(threadName + ": Transação concluída com sucesso.");
        }// if-else

        return feedback.toString();
    }// scheduler2PL

    /* Funções Complementares 2PL */

    /**
     * Libera um bloqueio após a execução de uma operação.
     * 
     * @param op a operação que liberará o bloqueio.
     */
    private synchronized void freeLock(Operation op) {
        int qtdeTrancas = this.listaTrancas.size();
        if (qtdeTrancas > 0) {
            for (int i = 0; i < qtdeTrancas; i++) {
                CustomLock tranca = getTranca(i);
                if (tranca.verifyOperation(op)) {
                    System.out.println(tranca.unlockLog());
                    deleteLock(tranca);
                    break;
                }// if
            }// for
        }// if
    }// freeLock

    /**
     * Obtém um bloqueio específico da lista de bloqueios.
     * 
     * @param index o índice do bloqueio na lista.
     * @return o bloqueio encontrado.
     */
    private CustomLock getTranca(int index) {
        return this.listaTrancas.get(index);
    }// getTrancas

    /**
     * Remove um bloqueio da lista de bloqueios.
     * 
     * @param tranca o bloqueio a ser removido.
     */
    private void deleteLock(CustomLock tranca) {
        this.listaTrancas.remove(tranca);
    }// delTranca

    /**
     * Verifica se há conflito de bloqueio com alguma operação.
     * 
     * @param op a operação a ser verificada quanto a conflito.
     * @return true se houver conflito, false caso contrário.
     */
    private boolean verifyConflict(Operation op) {
        for (CustomLock trancaExistente : listaTrancas) {
            if (trancaExistente.verifyLockConflict(op.type)) {
                return true;
            }// if
        }// for
        return false;
    }// verifyConflict

    /**
     * Aguarda um curto período de tempo.
     */
    public void silenceThread() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }// try-cathc
    }// silenceThread

}// SharedResourcesManager
