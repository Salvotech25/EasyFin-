package it.easyfin.app.repository;

import it.easyfin.app.dominio.Posizione;
import it.easyfin.app.dominio.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per la gestione delle posizioni
 */
@Repository
public interface PosizioneRepository extends JpaRepository<Posizione, Long> {
    
    /**
     * Trova una posizione per utente e ticker dello strumento
     */
    Optional<Posizione> findByUtenteAndStrumento_Ticker(Utente utente, String ticker);
    
    /**
     * Trova tutte le posizioni di un utente
     */
    List<Posizione> findByUtente(Utente utente);
}
