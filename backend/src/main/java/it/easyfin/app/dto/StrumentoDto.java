package it.easyfin.app.dto;

/**
 * DTO per uno strumento finanziario
 */
public class StrumentoDto {
    public String ticker;
    public String nome;
    public double prezzoCorrente;
    
    public StrumentoDto() {}
    
    public StrumentoDto(String ticker, String nome, double prezzoCorrente) {
        this.ticker = ticker;
        this.nome = nome;
        this.prezzoCorrente = prezzoCorrente;
    }
}
