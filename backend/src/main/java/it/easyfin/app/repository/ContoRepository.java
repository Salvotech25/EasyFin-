package it.easyfin.app.repository;

import it.easyfin.app.dominio.Conto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per la gestione dei conti
 */
@Repository
public interface ContoRepository extends JpaRepository<Conto, Long> {
}
