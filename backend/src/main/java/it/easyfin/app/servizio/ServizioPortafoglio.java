package it.easyfin.app.servizio;

import it.easyfin.app.dominio.*;
import it.easyfin.app.dto.*;
import it.easyfin.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servizio per la gestione del portafoglio
 */
@Service
@Transactional
public class ServizioPortafoglio {
    
    @Autowired
    private ServizioAutenticazione servizioAutenticazione;
    
    @Autowired
    private ServizioQuotazioni servizioQuotazioni;
    
    @Autowired
    private PosizioneRepository posizioneRepository;
    
    @Autowired
    private OrdineRepository ordineRepository;
    
    /**
     * Ottiene il portafoglio di un utente
     */
    public PortafoglioRisposta portafoglioPer(String token) {
        Utente utente = servizioAutenticazione.trovaUtentePerToken(token);
        
        List<Posizione> posizioni = posizioneRepository.findByUtente(utente);
        
        List<PosizioneDto> posizioniDto = posizioni.stream()
            .map(this::convertiPosizione)
            .collect(Collectors.toList());
        
        TotaliDto totali = calcolaTotali(utente, posizioni);
        
        return new PortafoglioRisposta(posizioniDto, totali);
    }
    
    /**
     * Ottiene lo storico ordini di un utente
     */
    public List<OrdineDto> storicoOrdini(String token) {
        Utente utente = servizioAutenticazione.trovaUtentePerToken(token);
        
        return ordineRepository.findByUtenteOrderByDataDesc(utente).stream()
            .map(this::convertiOrdine)
            .collect(Collectors.toList());
    }
    
    /**
     * Esegue un ordine di acquisto
     */
    public PortafoglioRisposta acquista(String token, AcquistoRichiesta richiesta) {
        // Validazione
        if (richiesta.quantita < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantità non valida");
        }
        
        Utente utente = servizioAutenticazione.trovaUtentePerToken(token);
        String ticker = richiesta.ticker.toUpperCase();
        
        // Trova strumento e prezzo
        double prezzoCorrente = servizioQuotazioni.prezzoCorrente(ticker);
        double costo = richiesta.quantita * prezzoCorrente;
        
        // Verifica fondi sufficienti
        if (utente.getConto().getSaldoContante() < costo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                String.format("Saldo insufficiente. Disponibile: €%.2f, Richiesto: €%.2f", 
                    utente.getConto().getSaldoContante(), costo));
        }
        
        // Trova o crea posizione
        Posizione posizioneEsistente = posizioneRepository.findByUtenteAndStrumento_Ticker(utente, ticker).orElse(null);
        
        if (posizioneEsistente != null) {
            // Aggiorna posizione esistente
            int quantitaTotale = posizioneEsistente.getQuantita() + richiesta.quantita;
            double prezzoMedioNuovo = (posizioneEsistente.getQuantita() * posizioneEsistente.getPrezzoMedio() + 
                                     richiesta.quantita * prezzoCorrente) / quantitaTotale;
            
            posizioneEsistente.setQuantita(quantitaTotale);
            posizioneEsistente.setPrezzoMedio(prezzoMedioNuovo);
            posizioneRepository.save(posizioneEsistente);
        } else {
            // Crea nuova posizione
            Strumento strumento = servizioQuotazioni.elencoStrumenti().stream()
                .filter(s -> s.getTicker().equals(ticker))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("Strumento '%s' non trovato", ticker)));
            
            Posizione nuovaPosizione = new Posizione(utente, strumento, richiesta.quantita, prezzoCorrente);
            posizioneRepository.save(nuovaPosizione);
        }
        
        // Preleva denaro dal conto
        utente.getConto().preleva(costo, "Acquisto " + ticker);
        
        // Crea ordine
        Ordine ordine = new Ordine(utente, TipoOrdine.ACQUISTO, ticker, richiesta.quantita, 
                                   prezzoCorrente, LocalDate.now(), StatoOrdine.ESEGUITO);
        ordineRepository.save(ordine);
        
        return portafoglioPer(token);
    }
    
    /**
     * Esegue un ordine di vendita
     */
    public PortafoglioRisposta vendi(String token, VenditaRichiesta richiesta) {
        // Validazione
        if (richiesta.quantita < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantità non valida");
        }
        
        Utente utente = servizioAutenticazione.trovaUtentePerToken(token);
        String ticker = richiesta.ticker.toUpperCase();
        
        // Trova posizione
        Posizione posizione = posizioneRepository.findByUtenteAndStrumento_Ticker(utente, ticker)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Posizione non trovata"));
        
        // Verifica quantità disponibile
        if (posizione.getQuantita() < richiesta.quantita) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                String.format("Quantità non disponibile. Possedute: %d, Richieste: %d", 
                    posizione.getQuantita(), richiesta.quantita));
        }
        
        double prezzoCorrente = servizioQuotazioni.prezzoCorrente(ticker);
        double incasso = richiesta.quantita * prezzoCorrente;
        
        // Calcola P&L realizzato
        double pnlRealizzato = (prezzoCorrente - posizione.getPrezzoMedio()) * richiesta.quantita;
        
        // Aggiorna o rimuovi posizione
        if (posizione.getQuantita() == richiesta.quantita) {
            // Rimuovi posizione
            posizioneRepository.delete(posizione);
        } else {
            // Riduci quantità
            posizione.setQuantita(posizione.getQuantita() - richiesta.quantita);
            posizioneRepository.save(posizione);
        }
        
        // Deposita denaro sul conto
        utente.getConto().deposita(incasso, "Vendita " + ticker);
        
        // Crea ordine con P&L realizzato
        Ordine ordine = new Ordine(utente, TipoOrdine.VENDITA, ticker, richiesta.quantita, 
                                   prezzoCorrente, LocalDate.now(), StatoOrdine.ESEGUITO);
        ordine.setPnlRealizzato(pnlRealizzato);
        ordineRepository.save(ordine);
        
        return portafoglioPer(token);
    }
    
    /**
     * Converte una posizione in DTO
     */
    private PosizioneDto convertiPosizione(Posizione posizione) {
        return new PosizioneDto(
            posizione.getStrumento().getTicker(),
            posizione.getQuantita(),
            posizione.getPrezzoMedio(),
            posizione.getStrumento().getPrezzoCorrente(),
            posizione.getValoreMercato(),
            posizione.getPnlNonRealizzato()
        );
    }
    
    /**
     * Converte un ordine in DTO
     */
    private OrdineDto convertiOrdine(Ordine ordine) {
        return new OrdineDto(
            ordine.getId(),
            ordine.getTipo().name(),
            ordine.getTicker(),
            ordine.getQuantita(),
            ordine.getPrezzoEsecuzione(),
            ordine.getData().toString(),
            ordine.getStato().name(),
            ordine.getPnlRealizzato()
        );
    }
    
    /**
     * Calcola i totali del portafoglio
     */
    private TotaliDto calcolaTotali(Utente utente, List<Posizione> posizioni) {
        double valoreStrumenti = posizioni.stream()
            .mapToDouble(Posizione::getValoreMercato)
            .sum();
        
        double saldoContante = utente.getConto().getSaldoContante();
        double nav = valoreStrumenti + saldoContante;
        
        double pnlNonRealizzatoTotale = posizioni.stream()
            .mapToDouble(Posizione::getPnlNonRealizzato)
            .sum();
        
        return new TotaliDto(valoreStrumenti, saldoContante, nav, pnlNonRealizzatoTotale);
    }
}
