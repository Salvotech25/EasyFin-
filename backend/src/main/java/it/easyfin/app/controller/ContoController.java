package it.easyfin.app.controller;

import it.easyfin.app.dto.ContoRisposta;
import it.easyfin.app.dto.MovimentoCassaDto;
import it.easyfin.app.servizio.ServizioAutenticazione;
import it.easyfin.app.servizio.ServizioPortafoglio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller per la gestione del conto
 */
@RestController
@RequestMapping("/api/conto")
@CrossOrigin(origins = "*")
@Tag(name = "Conto", description = "Gestione conto corrente")
public class ContoController {
    
    @Autowired
    private ServizioAutenticazione servizioAutenticazione;
    
    @Autowired
    private ServizioPortafoglio servizioPortafoglio;
    
    @GetMapping("/me")
    @Operation(summary = "Ottieni dettagli conto", 
               description = "Restituisce saldo, IBAN, movimenti e NAV")
    @ApiResponse(responseCode = "200", description = "Dettagli conto")
    @ApiResponse(responseCode = "401", description = "Non autorizzato")
    public ResponseEntity<ContoRisposta> getConto(@RequestHeader(value = "Authorization", required = false) String token) {
        // Verifica formato token
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante. Clicca 'Authorize' in alto a destra per autenticarti.");
        }
        
        String tokenValue = token.substring(7);
        var utente = servizioAutenticazione.trovaUtentePerToken(tokenValue);
        
        // Ottieni portafoglio per calcolare totali
        var portafoglio = servizioPortafoglio.portafoglioPer(tokenValue);
        
        // Converti movimenti
        List<MovimentoCassaDto> movimenti = utente.getConto().getMovimenti().stream()
            .map(m -> new MovimentoCassaDto(
                m.getData().toString(),
                m.getDescrizione(),
                m.getImporto()
            ))
            .collect(Collectors.toList());
        
        ContoRisposta risposta = new ContoRisposta(
            utente.getConto().getIban(),
            utente.getConto().getSaldoContante(),
            movimenti,
            portafoglio.totali.valoreStrumenti,
            portafoglio.totali.nav
        );
        
        return ResponseEntity.ok(risposta);
    }
}
