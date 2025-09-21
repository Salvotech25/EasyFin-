package it.easyfin.app.dto;

/**
 * DTO per i totali del portafoglio
 */
public class TotaliDto {
    public double valoreStrumenti;
    public double saldoContante;
    public double nav;
    public double pnlNonRealizzatoTotale;
    
    public TotaliDto() {}
    
    public TotaliDto(double valoreStrumenti, double saldoContante, 
                     double nav, double pnlNonRealizzatoTotale) {
        this.valoreStrumenti = valoreStrumenti;
        this.saldoContante = saldoContante;
        this.nav = nav;
        this.pnlNonRealizzatoTotale = pnlNonRealizzatoTotale;
    }
}
