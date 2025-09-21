package it.easyfin.app.dto;

import java.util.List;

/**
 * DTO per la risposta del conto
 */
public class ContoRisposta {
    public String iban;
    public double saldoContante;
    public List<MovimentoCassaDto> movimenti;
    public double valoreStrumenti;
    public double nav;
    
    public ContoRisposta() {}
    
    public ContoRisposta(String iban, double saldoContante, List<MovimentoCassaDto> movimenti, 
                         double valoreStrumenti, double nav) {
        this.iban = iban;
        this.saldoContante = saldoContante;
        this.movimenti = movimenti;
        this.valoreStrumenti = valoreStrumenti;
        this.nav = nav;
    }
}
