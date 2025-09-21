package it.easyfin.app.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entità MovimentoCassa per tracciare i movimenti del conto
 */
@Entity
@Table(name = "movimenti_cassa")
public class MovimentoCassa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate data;
    
    @Column(nullable = false)
    private String descrizione;
    
    @Column(nullable = false)
    private double importo; // + entrata, - uscita
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conto_id", nullable = false)
    private Conto conto;
    
    // Costruttore vuoto
    public MovimentoCassa() {}
    
    // Costruttore con parametri
    public MovimentoCassa(LocalDate data, String descrizione, double importo, Conto conto) {
        this.data = data;
        this.descrizione = descrizione;
        this.importo = importo;
        this.conto = conto;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public double getImporto() {
        return importo;
    }
    
    public void setImporto(double importo) {
        this.importo = importo;
    }
    
    public Conto getConto() {
        return conto;
    }
    
    public void setConto(Conto conto) {
        this.conto = conto;
    }
    
    @Override
    public String toString() {
        return "MovimentoCassa{" +
                "id=" + id +
                ", data=" + data +
                ", descrizione='" + descrizione + '\'' +
                ", importo=" + importo +
                '}';
    }
}
