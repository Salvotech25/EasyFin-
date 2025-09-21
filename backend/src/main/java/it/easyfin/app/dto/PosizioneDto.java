package it.easyfin.app.dto;

/**
 * DTO per una posizione nel portafoglio
 */
public class PosizioneDto {
    public String ticker;
    public int quantita;
    public double prezzoMedio;
    public double prezzoCorrente;
    public double valoreMercato;
    public double pnlNonRealizzato;
    
    public PosizioneDto() {}
    
    public PosizioneDto(String ticker, int quantita, double prezzoMedio, 
                       double prezzoCorrente, double valoreMercato, double pnlNonRealizzato) {
        this.ticker = ticker;
        this.quantita = quantita;
        this.prezzoMedio = prezzoMedio;
        this.prezzoCorrente = prezzoCorrente;
        this.valoreMercato = valoreMercato;
        this.pnlNonRealizzato = pnlNonRealizzato;
    }
}
