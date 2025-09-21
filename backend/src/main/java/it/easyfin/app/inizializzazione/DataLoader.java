package it.easyfin.app.inizializzazione;

import it.easyfin.app.dominio.Strumento;
import it.easyfin.app.repository.StrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component per caricare dati iniziali all'avvio dell'applicazione
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private StrumentoRepository strumentoRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Verifica se gli strumenti sono già presenti
        if (strumentoRepository.count() > 0) {
            return; // Dati già caricati
        }
        
        // Carica strumenti iniziali
        caricaStrumenti();
        
        System.out.println("📊 Strumenti finanziari caricati: " + strumentoRepository.count());
    }
    
    /**
     * Carica gli strumenti finanziari predefiniti
     */
    private void caricaStrumenti() {
        Strumento[] strumenti = {
            new Strumento("GOOGL", "Alphabet Inc.", 140.00),
            new Strumento("AAPL", "Apple Inc.", 185.00),
            new Strumento("MSFT", "Microsoft Corporation", 410.00),
            new Strumento("AMZN", "Amazon.com Inc.", 155.00),
            new Strumento("NVDA", "NVIDIA Corporation", 450.00),
            new Strumento("META", "Meta Platforms Inc.", 320.00),
            new Strumento("TSLA", "Tesla Inc.", 220.00)
        };
        
        for (Strumento strumento : strumenti) {
            strumentoRepository.save(strumento);
        }
    }
}
