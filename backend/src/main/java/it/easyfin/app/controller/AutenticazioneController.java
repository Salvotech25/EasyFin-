package it.easyfin.app.controller;

import it.easyfin.app.dto.LoginRichiesta;
import it.easyfin.app.dto.LoginRisposta;
import it.easyfin.app.dto.RegistrazioneRichiesta;
import it.easyfin.app.servizio.ServizioAutenticazione;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller REST per la gestione dell'autenticazione degli utenti
 * 
 * Questo controller gestisce le operazioni di registrazione e login
 * per il simulatore di trading EasyFin. Fornisce endpoint per:
 * - Registrazione di nuovi utenti con conto virtuale
 * - Autenticazione tramite email e password
 * - Generazione di token JWT per le sessioni
 * 
 * @author Studente Universitario
 * @version 1.0.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticazione", description = "Gestione login e registrazione utenti")
public class AutenticazioneController {
    
    @Autowired
    private ServizioAutenticazione servizioAutenticazione;
    
    /**
     * Registra un nuovo utente nel sistema
     * 
     * Crea un nuovo utente con un conto virtuale inizializzato con €10.000
     * per il simulatore di trading. Valida l'unicità dell'email e i dati obbligatori.
     * 
     * @param richiesta Dati di registrazione (nome, email, password)
     * @return ResponseEntity con messaggio di successo o errore
     * @throws ResponseStatusException se email già esistente o dati non validi
     */
    @PostMapping("/register")
    @Operation(summary = "Registra nuovo utente", 
               description = "Crea un nuovo utente con conto virtuale da €10.000")
    @ApiResponse(responseCode = "200", description = "Registrazione completata con successo")
    @ApiResponse(responseCode = "409", description = "Email già registrata")
    @ApiResponse(responseCode = "400", description = "Dati mancanti o non validi")
    public ResponseEntity<Map<String, String>> registra(@RequestBody RegistrazioneRichiesta richiesta) {
        // Delega la logica di registrazione al servizio
        servizioAutenticazione.registra(richiesta.nome, richiesta.email, richiesta.password);
        
        // Prepara la risposta di successo
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registrazione completata con successo");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Autentica un utente esistente
     * 
     * Verifica le credenziali fornite e restituisce un token JWT
     * per le successive richieste autenticate.
     * 
     * @param richiesta Credenziali di accesso (email, password)
     * @return ResponseEntity con token JWT o errore
     * @throws ResponseStatusException se credenziali non valide
     */
    @PostMapping("/login")
    @Operation(summary = "Effettua login", 
               description = "Autentica un utente e restituisce un token JWT")
    @ApiResponse(responseCode = "200", description = "Login effettuato con successo")
    @ApiResponse(responseCode = "401", description = "Credenziali non valide")
    public ResponseEntity<LoginRisposta> login(@RequestBody LoginRichiesta richiesta) {
        // Delega l'autenticazione al servizio
        LoginRisposta risposta = servizioAutenticazione.login(richiesta.email, richiesta.password);
        return ResponseEntity.ok(risposta);
    }
}
