package it.easyfin.app.dto;

/**
 * DTO per la richiesta di registrazione
 */
public class RegistrazioneRichiesta {
    public String nome;
    public String email;
    public String password;
    
    public RegistrazioneRichiesta() {}
    
    public RegistrazioneRichiesta(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }
}
