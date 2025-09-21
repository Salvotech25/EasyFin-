package it.easyfin.app.dominio;

import jakarta.persistence.*;

/**
 * Entità Strumento per rappresentare gli strumenti finanziari
 */
@Entity
@Table(name = "strumenti")
public class Strumento {
    
    @Id
    @Column(name = "ticker")
    private String ticker;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private double prezzoCorrente;
    
    // Costruttore vuoto
    public Strumento() {}
    
    // Costruttore con parametri
    public Strumento(String ticker, String nome, double prezzoCorrente) {
        this.ticker = ticker;
        this.nome = nome;
        this.prezzoCorrente = prezzoCorrente;
    }
    
    // Getters e Setters
    public String getTicker() {
        return ticker;
    }
    
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getPrezzoCorrente() {
        return prezzoCorrente;
    }
    
    public void setPrezzoCorrente(double prezzoCorrente) {
        this.prezzoCorrente = prezzoCorrente;
    }
    
    @Override
    public String toString() {
        return "Strumento{" +
                "ticker='" + ticker + '\'' +
                ", nome='" + nome + '\'' +
                ", prezzoCorrente=" + prezzoCorrente +
                '}';
    }
}
