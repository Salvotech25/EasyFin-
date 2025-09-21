package it.easyfin.app.servizio;

import it.easyfin.app.dominio.Conto;
import it.easyfin.app.dominio.Utente;
import it.easyfin.app.dto.LoginRisposta;
import it.easyfin.app.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

/**
 * Servizio per l'autenticazione degli utenti
 */
@Service
@Transactional
public class ServizioAutenticazione {
    
    @Autowired
    private UtenteRepository utenteRepository;
    
    @Autowired
    private ServizioToken servizioToken;
    
    /**
     * Registra un nuovo utente
     */
    public void registra(String nome, String email, String password) {
        // Validazione campi
        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome richiesto");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email richiesta");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password richiesta");
        }
        
        // Verifica se l'email esiste già
        if (utenteRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già registrata");
        }
        
        // Crea utente con conto
        Utente utente = new Utente(nome, email, password);
        
        // Crea conto con IBAN mock
        String iban = generaIbanMock();
        Conto conto = new Conto(iban);
        utente.setConto(conto);
        
        // Salva utente (cascade salva anche il conto)
        utenteRepository.save(utente);
    }
    
    /**
     * Effettua il login di un utente
     */
    public LoginRisposta login(String email, String password) {
        // Trova utente per email
        Utente utente = utenteRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide"));
        
        // Verifica password (in produzione: hash)
        if (!password.equals(utente.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
        }
        
        // Genera token e associa
        String token = servizioToken.generaToken();
        servizioToken.associa(token, email);
        
        return new LoginRisposta(token);
    }
    
    /**
     * Trova un utente per token
     */
    public Utente trovaUtentePerToken(String token) {
        String email = servizioToken.emailPer(token);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido");
        }
        
        return utenteRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato"));
    }
    
    /**
     * Effettua il logout
     */
    public void logout(String token) {
        servizioToken.rimuovi(token);
    }
    
    /**
     * Genera un IBAN mock italiano
     */
    private String generaIbanMock() {
        Random random = new Random();
        StringBuilder iban = new StringBuilder("IT");
        
        // Aggiungi 2 cifre di controllo
        iban.append(String.format("%02d", random.nextInt(100)));
        
        // Aggiungi lettera per identificativo banca
        iban.append((char) ('A' + random.nextInt(26)));
        
        // Aggiungi 22 cifre per conto corrente
        for (int i = 0; i < 22; i++) {
            iban.append(random.nextInt(10));
        }
        
        return iban.toString();
    }
}
