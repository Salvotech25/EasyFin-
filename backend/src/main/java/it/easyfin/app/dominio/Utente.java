package it.easyfin.app.dominio;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità Utente per la gestione degli utenti del sistema
 */
@Entity
@Table(name = "utenti")
public class Utente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "conto_id")
    private Conto conto;
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ordine> ordini = new ArrayList<>();
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Posizione> posizioni = new ArrayList<>();
    
    // Costruttore vuoto
    public Utente() {}
    
    // Costruttore con parametri
    public Utente(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Conto getConto() {
        return conto;
    }
    
    public void setConto(Conto conto) {
        this.conto = conto;
    }
    
    public List<Ordine> getOrdini() {
        return ordini;
    }
    
    public void setOrdini(List<Ordine> ordini) {
        this.ordini = ordini;
    }
    
    public List<Posizione> getPosizioni() {
        return posizioni;
    }
    
    public void setPosizioni(List<Posizione> posizioni) {
        this.posizioni = posizioni;
    }
    
    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
