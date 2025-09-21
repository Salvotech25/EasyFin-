package it.easyfin.app.servizio;

import it.easyfin.app.archivio.ArchivioMemoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servizio per la gestione dei token di autenticazione
 */
@Service
public class ServizioToken {
    
    @Autowired
    private ArchivioMemoria archivioMemoria;
    
    /**
     * Genera un nuovo token UUID
     */
    public String generaToken() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Associa un token a un'email nell'archivio
     */
    public void associa(String token, String email) {
        archivioMemoria.associa(token, email);
    }
    
    /**
     * Ottiene l'email associata a un token
     */
    public String emailPer(String token) {
        return archivioMemoria.emailPer(token);
    }
    
    /**
     * Rimuove un token dall'archivio
     */
    public void rimuovi(String token) {
        archivioMemoria.rimuovi(token);
    }
    
    /**
     * Verifica se un token esiste
     */
    public boolean esiste(String token) {
        return archivioMemoria.esiste(token);
    }
}
