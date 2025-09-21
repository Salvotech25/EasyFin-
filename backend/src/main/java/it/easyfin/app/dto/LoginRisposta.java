package it.easyfin.app.dto;

/**
 * DTO per la risposta di login
 */
public class LoginRisposta {
    public String token;
    
    public LoginRisposta() {}
    
    public LoginRisposta(String token) {
        this.token = token;
    }
}
