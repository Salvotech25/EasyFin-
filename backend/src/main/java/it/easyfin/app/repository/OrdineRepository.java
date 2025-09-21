package it.easyfin.app.repository;

import it.easyfin.app.dominio.Ordine;
import it.easyfin.app.dominio.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per la gestione degli ordini
 */
@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    
    /**
     * Trova tutti gli ordini di un utente ordinati per data decrescente
     */
    List<Ordine> findByUtenteOrderByDataDesc(Utente utente);
}
