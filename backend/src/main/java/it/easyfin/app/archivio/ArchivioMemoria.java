package it.easyfin.app.archivio;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Archivio in memoria per la gestione dei token di autenticazione
 */
@Component
public class ArchivioMemoria {
    
    private final Map<String, String> tokenToEmail = new ConcurrentHashMap<>();
    
    /**
     * Associa un token a un'email
     */
    public void associa(String token, String email) {
        tokenToEmail.put(token, email);
    }
    
    /**
     * Ottiene l'email associata a un token
     */
    public String emailPer(String token) {
        return tokenToEmail.get(token);
    }
    
    /**
     * Rimuove un token dall'archivio
     */
    public void rimuovi(String token) {
        tokenToEmail.remove(token);
    }
    
    /**
     * Verifica se un token esiste
     */
    public boolean esiste(String token) {
        return tokenToEmail.containsKey(token);
    }
    
    /**
     * Conta il numero di token attivi
     */
    public int contaToken() {
        return tokenToEmail.size();
    }
}
