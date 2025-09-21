package it.easyfin.app.dominio;

import jakarta.persistence.*;

/**
 * Entità Posizione per rappresentare le posizioni nel portafoglio
 */
@Entity
@Table(name = "posizioni")
public class Posizione {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strumento_ticker", nullable = false)
    private Strumento strumento;
    
    @Column(nullable = false)
    private int quantita;
    
    @Column(nullable = false)
    private double prezzoMedio;
    
    // Costruttore vuoto
    public Posizione() {}
    
    // Costruttore con parametri
    public Posizione(Utente utente, Strumento strumento, int quantita, double prezzoMedio) {
        this.utente = utente;
        this.strumento = strumento;
        this.quantita = quantita;
        this.prezzoMedio = prezzoMedio;
    }
    
    /**
     * Calcola il valore di mercato della posizione
     */
    public double getValoreMercato() {
        return quantita * strumento.getPrezzoCorrente();
    }
    
    /**
     * Calcola il P&L non realizzato della posizione
     */
    public double getPnlNonRealizzato() {
        return (strumento.getPrezzoCorrente() - prezzoMedio) * quantita;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Utente getUtente() {
        return utente;
    }
    
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    public Strumento getStrumento() {
        return strumento;
    }
    
    public void setStrumento(Strumento strumento) {
        this.strumento = strumento;
    }
    
    public int getQuantita() {
        return quantita;
    }
    
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
    
    public double getPrezzoMedio() {
        return prezzoMedio;
    }
    
    public void setPrezzoMedio(double prezzoMedio) {
        this.prezzoMedio = prezzoMedio;
    }
    
    @Override
    public String toString() {
        return "Posizione{" +
                "id=" + id +
                ", utente=" + (utente != null ? utente.getEmail() : "null") +
                ", strumento=" + (strumento != null ? strumento.getTicker() : "null") +
                ", quantita=" + quantita +
                ", prezzoMedio=" + prezzoMedio +
                '}';
    }
}
