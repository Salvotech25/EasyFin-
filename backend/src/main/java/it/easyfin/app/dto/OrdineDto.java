package it.easyfin.app.dto;

/**
 * DTO per un ordine
 */
public class OrdineDto {
    public Long id;
    public String tipo;
    public String ticker;
    public int quantita;
    public double prezzoEsecuzione;
    public String data;
    public String stato;
    public Double pnlRealizzato;
    
    public OrdineDto() {}
    
    public OrdineDto(Long id, String tipo, String ticker, int quantita, 
                     double prezzoEsecuzione, String data, String stato, Double pnlRealizzato) {
        this.id = id;
        this.tipo = tipo;
        this.ticker = ticker;
        this.quantita = quantita;
        this.prezzoEsecuzione = prezzoEsecuzione;
        this.data = data;
        this.stato = stato;
        this.pnlRealizzato = pnlRealizzato;
    }
}
