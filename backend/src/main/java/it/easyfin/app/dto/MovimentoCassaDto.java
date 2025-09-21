package it.easyfin.app.dto;

/**
 * DTO per un movimento di cassa
 */
public class MovimentoCassaDto {
    public String data;
    public String descrizione;
    public double importo;
    
    public MovimentoCassaDto() {}
    
    public MovimentoCassaDto(String data, String descrizione, double importo) {
        this.data = data;
        this.descrizione = descrizione;
        this.importo = importo;
    }
}
