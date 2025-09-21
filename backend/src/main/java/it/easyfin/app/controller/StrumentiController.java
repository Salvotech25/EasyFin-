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
 * Controller per la gestione degli strumenti finanziari
 */
@RestController
@RequestMapping("/api/strumenti")
@CrossOrigin(origins = "*")
@Tag(name = "Strumenti", description = "Gestione strumenti finanziari")
public class StrumentiController {
    
    @Autowired
    private ServizioQuotazioni servizioQuotazioni;
    
    @GetMapping("/")
    @Operation(summary = "Lista strumenti", 
               description = "Restituisce l'elenco di tutti gli strumenti disponibili")
    public ResponseEntity<List<StrumentoDto>> getStrumenti() {
        List<StrumentoDto> strumenti = servizioQuotazioni.elencoStrumenti().stream()
            .map(s -> new StrumentoDto(s.getTicker(), s.getNome(), s.getPrezzoCorrente()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(strumenti);
    }
}
