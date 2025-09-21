package it.easyfin.app.repository;

import it.easyfin.app.dominio.MovimentoCassa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per la gestione dei movimenti di cassa
 */
@Repository
public interface MovimentoCassaRepository extends JpaRepository<MovimentoCassa, Long> {
}
