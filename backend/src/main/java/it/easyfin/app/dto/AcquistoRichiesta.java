package it.easyfin.app.dto;

/**
 * DTO per la richiesta di acquisto
 */
public class AcquistoRichiesta {
    public String ticker;
    public int quantita;
    
    public AcquistoRichiesta() {}
    
    public AcquistoRichiesta(String ticker, int quantita) {
        this.ticker = ticker;
        this.quantita = quantita;
    }
}
