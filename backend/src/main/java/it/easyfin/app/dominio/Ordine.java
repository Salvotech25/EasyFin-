package it.easyfin.app.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entità Ordine per rappresentare gli ordini di trading
 */
@Entity
@Table(name = "ordini")
public class Ordine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOrdine tipo;
    
    @Column(nullable = false)
    private String ticker;
    
    @Column(nullable = false)
    private int quantita;
    
    @Column(nullable = false)
    private double prezzoEsecuzione;
    
    @Column(nullable = false)
    private LocalDate data;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato;
    
    @Column
    private Double pnlRealizzato; // solo per vendite, altrimenti null
    
    // Costruttore vuoto
    public Ordine() {}
    
    // Costruttore con parametri
    public Ordine(Utente utente, TipoOrdine tipo, String ticker, int quantita, 
                  double prezzoEsecuzione, LocalDate data, StatoOrdine stato) {
        this.utente = utente;
        this.tipo = tipo;
        this.ticker = ticker;
        this.quantita = quantita;
        this.prezzoEsecuzione = prezzoEsecuzione;
        this.data = data;
        this.stato = stato;
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
    
    public TipoOrdine getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoOrdine tipo) {
        this.tipo = tipo;
    }
    
    public String getTicker() {
        return ticker;
    }
    
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    
    public int getQuantita() {
        return quantita;
    }
    
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
    
    public double getPrezzoEsecuzione() {
        return prezzoEsecuzione;
    }
    
    public void setPrezzoEsecuzione(double prezzoEsecuzione) {
        this.prezzoEsecuzione = prezzoEsecuzione;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public StatoOrdine getStato() {
        return stato;
    }
    
    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }
    
    public Double getPnlRealizzato() {
        return pnlRealizzato;
    }
    
    public void setPnlRealizzato(Double pnlRealizzato) {
        this.pnlRealizzato = pnlRealizzato;
    }
    
    @Override
    public String toString() {
        return "Ordine{" +
                "id=" + id +
                ", utente=" + (utente != null ? utente.getEmail() : "null") +
                ", tipo=" + tipo +
                ", ticker='" + ticker + '\'' +
                ", quantita=" + quantita +
                ", prezzoEsecuzione=" + prezzoEsecuzione +
                ", data=" + data +
                ", stato=" + stato +
                ", pnlRealizzato=" + pnlRealizzato +
                '}';
    }
}
