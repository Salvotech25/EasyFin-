package it.easyfin.app.controller;

import it.easyfin.app.dto.*;
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

/**
 * Controller per la gestione del portafoglio
 */
@RestController
@RequestMapping("/api/portafoglio")
@CrossOrigin(origins = "*")
@Tag(name = "Portafoglio", description = "Gestione portafoglio e ordini")
public class PortafoglioController {

    @Autowired
    private ServizioPortafoglio servizioPortafoglio;

    @GetMapping("/")
    @Operation(summary = "Ottieni portafoglio", 
               description = "Restituisce posizioni e totali del portafoglio")
    @ApiResponse(responseCode = "200", description = "Portafoglio")
    @ApiResponse(responseCode = "401", description = "Non autorizzato")
    public ResponseEntity<PortafoglioRisposta> getPortafoglio(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante. Clicca 'Authorize' in alto a destra per autenticarti.");
        }
        
        String tokenValue = token.substring(7);
        PortafoglioRisposta portafoglio = servizioPortafoglio.portafoglioPer(tokenValue);
        return ResponseEntity.ok(portafoglio);
    }

    @GetMapping("/ordini")
    @Operation(summary = "Storico ordini", description = "Restituisce lo storico degli ordini eseguiti")
    @ApiResponse(responseCode = "200", description = "Storico ordini")
    @ApiResponse(responseCode = "401", description = "Non autorizzato")
    public ResponseEntity<List<OrdineDto>> getOrdini(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante. Clicca 'Authorize' in alto a destra per autenticarti.");
        }
        
        String tokenValue = token.substring(7);
        List<OrdineDto> ordini = servizioPortafoglio.storicoOrdini(tokenValue);
        return ResponseEntity.ok(ordini);
    }

    @PostMapping("/ordini/acquisto")
    @Operation(summary = "Ordine di acquisto", description = "Esegue un ordine di acquisto")
    @ApiResponse(responseCode = "200", description = "Ordine eseguito")
    @ApiResponse(responseCode = "400", description = "Errore validazione")
    @ApiResponse(responseCode = "401", description = "Non autorizzato")
    public ResponseEntity<PortafoglioRisposta> acquista(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AcquistoRichiesta richiesta) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante. Clicca 'Authorize' in alto a destra per autenticarti.");
        }
        
        String tokenValue = token.substring(7);
        PortafoglioRisposta portafoglio = servizioPortafoglio.acquista(tokenValue, richiesta);
        return ResponseEntity.ok(portafoglio);
    }

    @PostMapping("/ordini/vendita")
    @Operation(summary = "Ordine di vendita", description = "Esegue un ordine di vendita")
    @ApiResponse(responseCode = "200", description = "Ordine eseguito")
    @ApiResponse(responseCode = "400", description = "Errore validazione")
    @ApiResponse(responseCode = "401", description = "Non autorizzato")
    public ResponseEntity<PortafoglioRisposta> vendi(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody VenditaRichiesta richiesta) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante. Clicca 'Authorize' in alto a destra per autenticarti.");
        }
        
        String tokenValue = token.substring(7);
        PortafoglioRisposta portafoglio = servizioPortafoglio.vendi(tokenValue, richiesta);
        return ResponseEntity.ok(portafoglio);
    }
}
