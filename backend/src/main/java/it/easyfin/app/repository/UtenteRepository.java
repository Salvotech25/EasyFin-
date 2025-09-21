package it.easyfin.app.repository;

import it.easyfin.app.dominio.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per la gestione degli utenti
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    
    /**
     * Trova un utente per email
     */
    Optional<Utente> findByEmail(String email);
    
    /**
     * Verifica se esiste un utente con l'email specificata
     */
    boolean existsByEmail(String email);
}
