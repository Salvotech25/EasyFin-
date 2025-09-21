package it.easyfin.app.dto;

/**
 * DTO per la richiesta di vendita
 */
public class VenditaRichiesta {
    public String ticker;
    public int quantita;
    
    public VenditaRichiesta() {}
    
    public VenditaRichiesta(String ticker, int quantita) {
        this.ticker = ticker;
        this.quantita = quantita;
    }
}
