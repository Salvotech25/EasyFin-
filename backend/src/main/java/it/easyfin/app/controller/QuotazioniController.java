package it.easyfin.app.controller;

import it.easyfin.app.dto.StrumentoDto;
import it.easyfin.app.servizio.ServizioQuotazioni;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller per la gestione delle quotazioni
 */
@RestController
@RequestMapping("/api/quotazioni")
@CrossOrigin(origins = "*")
@Tag(name = "Quotazioni", description = "Gestione quotazioni e prezzi")
public class QuotazioniController {
    
    @Autowired
    private ServizioQuotazioni servizioQuotazioni;
    
    @PostMapping("/aggiorna")
    @Operation(summary = "Aggiorna prezzi", 
               description = "Applica rumore ±2% ai prezzi di tutti gli strumenti")
    public ResponseEntity<List<StrumentoDto>> aggiornaPrezzi() {
        List<StrumentoDto> strumenti = servizioQuotazioni.aggiornaPrezziConRumore().stream()
            .map(s -> new StrumentoDto(s.getTicker(), s.getNome(), s.getPrezzoCorrente()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(strumenti);
    }
}
