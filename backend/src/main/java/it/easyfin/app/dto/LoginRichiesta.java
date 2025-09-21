package it.easyfin.app.dto;

/**
 * DTO per la richiesta di login
 */
public class LoginRichiesta {
    public String email;
    public String password;
    
    public LoginRichiesta() {}
    
    public LoginRichiesta(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
