package it.easyfin.app.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità Conto per la gestione del conto corrente dell'utente
 */
@Entity
@Table(name = "conti")
public class Conto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String iban;
    
    @Column(nullable = false)
    private double saldoContante = 10000.00;
    
    @OneToMany(mappedBy = "conto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimentoCassa> movimenti = new ArrayList<>();
    
    // Costruttore vuoto
    public Conto() {}
    
    // Costruttore con parametri
    public Conto(String iban) {
        this.iban = iban;
        this.saldoContante = 10000.00;
    }
    
    /**
     * Deposita denaro sul conto e crea un movimento
     */
    public void deposita(double importo, String descrizione) {
        if (importo <= 0) {
            throw new IllegalArgumentException("Importo deve essere positivo");
        }
        
        this.saldoContante += importo;
        
        MovimentoCassa movimento = new MovimentoCassa();
        movimento.setData(LocalDate.now());
        movimento.setDescrizione(descrizione);
        movimento.setImporto(importo);
        movimento.setConto(this);
        
        this.movimenti.add(movimento);
    }
    
    /**
     * Preleva denaro dal conto e crea un movimento
     */
    public void preleva(double importo, String descrizione) {
        if (importo <= 0) {
            throw new IllegalArgumentException("Importo deve essere positivo");
        }
        
        if (this.saldoContante < importo) {
            throw new IllegalArgumentException("Fondi insufficienti");
        }
        
        this.saldoContante -= importo;
        
        MovimentoCassa movimento = new MovimentoCassa();
        movimento.setData(LocalDate.now());
        movimento.setDescrizione(descrizione);
        movimento.setImporto(-importo);
        movimento.setConto(this);
        
        this.movimenti.add(movimento);
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIban() {
        return iban;
    }
    
    public void setIban(String iban) {
        this.iban = iban;
    }
    
    public double getSaldoContante() {
        return saldoContante;
    }
    
    public void setSaldoContante(double saldoContante) {
        this.saldoContante = saldoContante;
    }
    
    public List<MovimentoCassa> getMovimenti() {
        return movimenti;
    }
    
    public void setMovimenti(List<MovimentoCassa> movimenti) {
        this.movimenti = movimenti;
    }
    
    @Override
    public String toString() {
        return "Conto{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", saldoContante=" + saldoContante +
                '}';
    }
}
