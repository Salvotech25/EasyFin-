package it.easyfin.app.servizio;

import it.easyfin.app.dominio.Strumento;
import it.easyfin.app.repository.StrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Servizio per la gestione delle quotazioni
 */
@Service
@Transactional
public class ServizioQuotazioni {
    
    @Autowired
    private StrumentoRepository strumentoRepository;
    
    /**
     * Ottiene il prezzo corrente di uno strumento
     */
    public double prezzoCorrente(String ticker) {
        return strumentoRepository.findById(ticker.toUpperCase())
            .map(Strumento::getPrezzoCorrente)
            .orElseThrow(() -> new IllegalArgumentException("Strumento non trovato: " + ticker));
    }
    
    /**
     * Ottiene l'elenco di tutti gli strumenti
     */
    public List<Strumento> elencoStrumenti() {
        return strumentoRepository.findAll();
    }
    
    /**
     * Aggiorna i prezzi degli strumenti con rumore ±2%
     */
    public List<Strumento> aggiornaPrezziConRumore() {
        List<Strumento> strumenti = strumentoRepository.findAll();
        Random random = new Random();
        
        for (Strumento strumento : strumenti) {
            // Applica rumore ±2%
            double delta = (random.nextDouble() - 0.5) * 0.04; // [-2%, +2%]
            double nuovoPrezzo = strumento.getPrezzoCorrente() * (1 + delta);
            
            // Arrotonda a 2 decimali
            nuovoPrezzo = Math.round(nuovoPrezzo * 100.0) / 100.0;
            
            // Assicurati che il prezzo sia positivo
            if (nuovoPrezzo <= 0) {
                nuovoPrezzo = strumento.getPrezzoCorrente() * 0.98; // minimo -2%
            }
            
            strumento.setPrezzoCorrente(nuovoPrezzo);
            strumentoRepository.save(strumento);
        }
        
        return strumenti;
    }
}
