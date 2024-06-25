/**
 * Título do Arquivo: CustomLock.java
 * 
 * Descrição Breve: Este arquivo contém a classe que representa um bloqueio personalizado em um recurso.
 * 
 * Autor: Gabriel Finger Conte
 * Data de Criação: 24/06/2024
 * Última Modificação: 24/06/2024
 * Versão: 1.0
 */


package br.data;

/**
 * Classe que representa um bloqueio personalizado.
 */
public class CustomLock {
    
    private LockType type;
    private String lockedResource;
    private String clientName;
    private String logginInfo;
    
    /**
     * Construtor para um bloqueio compartilhado.
     * 
     * @param resourceName o nome do recurso bloqueado.
     * @param clientName o nome do cliente que bloqueou o recurso.
     */
    public CustomLock(String resourceName, String clientName) {
        this.type = LockType.SHARED;
        this.lockedResource = resourceName;
        this.clientName = clientName;
        this.logginInfo = "";
    }// CustomLock
    
    /**
     * Construtor para um bloqueio exclusivo.
     * 
     * @param resourceName o nome do recurso bloqueado.
     * @param clientName o nome do cliente que bloqueou o recurso.
     * @param logginInfo informações adicionais de log relacionadas ao bloqueio.
     */
    public CustomLock(String resourceName, String clientName, String logginInfo) {
        this.type = LockType.EXCLUSIVE;
        this.lockedResource = resourceName;
        this.clientName = clientName;
        this.logginInfo = logginInfo;
    }// CustomLock
    
    /**
     * Verifica se a operação é válida para este bloqueio.
     * 
     * @param op a operação a ser verificada.
     * @return true se a operação for válida, false caso contrário.
     */
    public boolean verifyOperation(Operation op){
        return verifyOwnership(op.clientName) && matchOperationType(op.type) && verifyLockedContent(op.targetedResource);
    }// verifyOperation
    
    /**
     * Verifica se o cliente possui a propriedade deste bloqueio.
     * 
     * @param possibleClient o possível cliente que possui a propriedade do bloqueio.
     * @return true se o cliente possuir a propriedade, false caso contrário.
     */
    public boolean verifyOwnership(String possibleClient){
        return this.clientName.equals(possibleClient);
    }// verifyOwnership
    
    /**
     * Verifica se o tipo de operação corresponde ao tipo de bloqueio.
     * 
     * @param type o tipo de operação a ser verificado.
     * @return true se o tipo de operação corresponder ao tipo de bloqueio, false caso contrário.
     */
    public boolean matchOperationType(OperationType type){
        return ((type == OperationType.READ && this.type == LockType.SHARED) ||
           (type == OperationType.WRITE && this.type == LockType.EXCLUSIVE));
    }// matchOperationType 
    
    /**
     * Verifica se o conteúdo bloqueado corresponde ao recurso alvo da operação.
     * 
     * @param resourceName o nome do recurso alvo da operação.
     * @return true se o conteúdo bloqueado corresponder ao recurso alvo, false caso contrário.
     */
    public boolean verifyLockedContent(String resourceName){
        return resourceName.equals(this.lockedResource);
    }// verifyLockContent
    
    /**
     * Verifica se há conflito de bloqueio com outro tipo de operação.
     * 
     * @param otherLock o tipo de operação a ser verificado quanto a conflito.
     * @return true se houver conflito de bloqueio, false caso contrário.
     */
    public boolean verifyLockConflict(OperationType otherLock){
        if(this.type == LockType.SHARED && otherLock == OperationType.READ){
            return false;
        }// if
        return true;
    }// verifyLockConflict

    /**
     * Retorna uma representação em string do bloqueio.
     * 
     * @return uma string representando o bloqueio.
     */
    @Override
    public String toString() {
        if(this.logginInfo.equals("")){
            return this.type + this.clientName + "l[" + this.lockedResource + "]";
        }// if
        return this.type + this.clientName + "l[" + this.lockedResource + ", " + this.logginInfo + "]";
    }// toString
    
    /**
     * Retorna um registro de desbloqueio em formato de string.
     * 
     * @return uma string representando o registro de desbloqueio.
     */
    public String unlockLog(){
        if(this.logginInfo.equals("")){
            return this.type + this.clientName + "u[" + this.lockedResource + "]";
        }// if
        return this.type + this.clientName + "u[" + this.lockedResource + ", " + this.logginInfo + "]";
    }// unlockLog
    
}
