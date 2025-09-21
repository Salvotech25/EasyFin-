package it.easyfin.app.dto;

import java.util.List;

/**
 * DTO per la risposta del portafoglio
 */
public class PortafoglioRisposta {
    public List<PosizioneDto> posizioni;
    public TotaliDto totali;
    
    public PortafoglioRisposta() {}
    
    public PortafoglioRisposta(List<PosizioneDto> posizioni, TotaliDto totali) {
        this.posizioni = posizioni;
        this.totali = totali;
    }
}
