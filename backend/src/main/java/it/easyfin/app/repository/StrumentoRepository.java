package it.easyfin.app.repository;

import it.easyfin.app.dominio.Strumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per la gestione degli strumenti finanziari
 */
@Repository
public interface StrumentoRepository extends JpaRepository<Strumento, String> {
}
