/**
 * EasyFin Portafogli - Simulatore di Trading
 * Frontend JavaScript per applicazione web di trading simulato
 * 
 * @author Studente Universitario
 * @version 1.0.0
 */

// ============================================================================
// CONFIGURAZIONE E VARIABILI GLOBALI
// ============================================================================

/**
 * URL base per le chiamate API al backend Spring Boot
 * @constant {string}
 */
const API_BASE_URL = 'http://localhost:8082/api';

/**
 * Token di autenticazione JWT per le richieste autenticate
 * @type {string|null}
 */
let authToken = null;

/**
 * Cache degli strumenti finanziari disponibili
 * @type {Array<Object>}
 */
let strumenti = [];

/**
 * Cache delle posizioni del portafoglio
 * @type {Array<Object>}
 */
let posizioni = [];

// ============================================================================
// RIFERIMENTI ELEMENTI DOM
// ============================================================================

/**
 * Riferimenti agli elementi DOM delle sezioni principali
 * @type {HTMLElement}
 */
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const tradingSection = document.getElementById('trading-section');
const portafoglioSection = document.getElementById('portafoglio-section');
const strumentiSection = document.getElementById('strumenti-section');
const storicoSection = document.getElementById('storico-section');

// ============================================================================
// INIZIALIZZAZIONE APPLICAZIONE
// ============================================================================

/**
 * Inizializza l'applicazione al caricamento della pagina
 * Verifica se esiste un token salvato e carica i dati appropriati
 */
document.addEventListener('DOMContentLoaded', function() {
    const savedToken = localStorage.getItem('ef_token');
    
    if (savedToken) {
        authToken = savedToken;
        // Verifica la validità del token prima di mostrare l'app
        verificaTokenValido();
    } else {
        // Nasconde il menu di navigazione se l'utente non è autenticato
        document.getElementById('nav').style.display = 'none';
    }
});

/**
 * Verifica se il token di autenticazione è ancora valido
 * Se non valido, effettua il logout automatico
 */
async function verificaTokenValido() {
    try {
        // Prova a chiamare un endpoint che richiede autenticazione
        await api('/conto/me');
        // Se la chiamata ha successo, il token è valido
        mostraApp();
        caricaDatiIniziali();
    } catch (error) {
        console.warn('Token non valido o scaduto:', error);
        // Token non valido, effettua logout
        logout();
        mostraNotifica('Sessione scaduta. Effettua nuovamente il login', 'warning');
    }
}

// ============================================================================
// UTILITÀ PER CHIAMATE API
// ============================================================================

/**
 * Esegue una chiamata API al backend con gestione automatica degli errori
 * @param {string} path - Percorso dell'endpoint API
 * @param {Object} options - Opzioni per la richiesta fetch
 * @returns {Promise<Object|string>} Risposta dell'API (JSON o testo)
 * @throws {Error} Errore se la richiesta fallisce
 */
async function api(path, options = {}) {
    const config = {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        },
        ...options
    };
    
    // Aggiunge il token di autenticazione se presente
    if (authToken) {
        config.headers['Authorization'] = `Bearer ${authToken}`;
    } else {
        console.warn('Nessun token di autenticazione disponibile');
    }
    
    const response = await fetch(`${API_BASE_URL}${path}`, config);
    
    // Legge il corpo della risposta una sola volta
    const contentType = response.headers.get('content-type');
    let responseBody;
    
    try {
        if (contentType && contentType.includes('application/json')) {
            responseBody = await response.json();
        } else {
            responseBody = await response.text();
        }
    } catch (e) {
        console.error('Errore nella lettura della risposta:', e);
        responseBody = null;
    }
    
    if (!response.ok) {
        // Estrae il messaggio di errore dal corpo già letto
        let errorMessage = `Errore ${response.status}`;
        
        if (responseBody) {
            if (typeof responseBody === 'object' && responseBody.error) {
                errorMessage = responseBody.error;
            } else if (typeof responseBody === 'string' && responseBody.trim()) {
                errorMessage = responseBody;
            }
        }
        
        // Gestisce errori di autenticazione solo se NON è una richiesta di login
        if (response.status === 401 && !path.includes('/auth/login')) {
            logout();
            throw new Error('Sessione scaduta');
        }
        
        throw new Error(errorMessage);
    }
    
    return responseBody;
}

// ============================================================================
// GESTIONE AUTENTICAZIONE
// ============================================================================

/**
 * Gestisce il processo di login dell'utente
 * @param {Event} event - Evento del form di login
 */
async function handleLogin(event) {
    event.preventDefault();
    
    // Estrae le credenziali dal form
    const email = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    
    // Validazione lato client
    if (!email || !password) {
        mostraNotifica('Inserisci email e password', 'error');
        return;
    }
    
    try {
        mostraLoading(true);
        
        // Chiama l'API di login
        const risposta = await api('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        
        // Salva il token e aggiorna lo stato
        authToken = risposta.token;
        localStorage.setItem('ef_token', authToken);
        
        // Mostra l'applicazione e carica i dati
        mostraApp();
        caricaDatiIniziali();
        mostraNotifica('Login effettuato con successo!', 'success');
        
    } catch (error) {
        console.error('Errore durante il login:', error);
        mostraNotifica(error.message || 'Credenziali non valide', 'error');
    } finally {
        mostraLoading(false);
    }
}

/**
 * Gestisce il processo di registrazione di un nuovo utente
 * @param {Event} event - Evento del form di registrazione
 */
async function handleRegister(event) {
    event.preventDefault();
    
    // Estrae i dati dal form
    const nome = document.getElementById('reg-nome').value.trim();
    const email = document.getElementById('reg-email').value.trim();
    const password = document.getElementById('reg-password').value;
    
    // Validazione lato client
    if (!nome || !email || !password) {
        mostraNotifica('Compila tutti i campi obbligatori', 'error');
        return;
    }
    
    if (password.length < 6) {
        mostraNotifica('La password deve essere di almeno 6 caratteri', 'error');
        return;
    }
    
    try {
        mostraLoading(true);
        
        // Chiama l'API di registrazione
        const response = await api('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ nome, email, password })
        });
        
        // Mostra messaggio di successo e passa al login
        const message = response.message || 'Registrazione completata con successo';
        mostraNotifica(message, 'success');
        mostraTab('login');
        
        // Pulisce il form di registrazione
        document.getElementById('register-form').reset();
        
    } catch (error) {
        console.error('Errore durante la registrazione:', error);
        mostraNotifica(error.message || 'Errore durante la registrazione', 'error');
    } finally {
        mostraLoading(false);
    }
}

/**
 * Effettua il logout dell'utente e resetta l'interfaccia
 */
function logout() {
    // Pulisce il token di autenticazione
    authToken = null;
    localStorage.removeItem('ef_token');
    
    // Nasconde tutte le sezioni dell'applicazione
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Nasconde il menu di navigazione
    document.getElementById('nav').style.display = 'none';
    
    // Mostra la sezione di autenticazione
    authSection.classList.add('active');
    
    // Pulisce i form di autenticazione
    document.getElementById('login-form').reset();
    document.getElementById('register-form').reset();
    
    // Pulisce le cache dei dati
    strumenti = [];
    posizioni = [];
}

// ============================================================================
// GESTIONE NAVIGAZIONE E INTERFACCIA
// ============================================================================

/**
 * Mostra una sezione specifica dell'applicazione e carica i dati necessari
 * @param {string} sezioneNome - Nome della sezione da mostrare
 */
function mostraSezione(sezioneNome) {
    // Nasconde tutte le sezioni
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Mostra la sezione richiesta
    const sezione = document.getElementById(`${sezioneNome}-section`);
    if (sezione) {
        sezione.classList.add('active');
        
        // Carica i dati specifici della sezione
        switch(sezioneNome) {
            case 'dashboard':
                caricaDashboard();
                break;
            case 'trading':
                caricaTrading();
                break;
            case 'portafoglio':
                caricaPortafoglio();
                break;
            case 'strumenti':
                caricaStrumenti();
                break;
            case 'storico':
                caricaStorico();
                break;
        }
    }
}

/**
 * Mostra l'applicazione principale dopo l'autenticazione
 */
function mostraApp() {
    // Nasconde la sezione di autenticazione
    authSection.classList.remove('active');
    
    // Mostra il menu di navigazione
    document.getElementById('nav').style.display = 'flex';
    
    // Mostra la dashboard come pagina iniziale
    dashboardSection.classList.add('active');
}

/**
 * Gestisce il cambio di tab nel form di autenticazione
 * @param {string} tab - Nome del tab da mostrare ('login' o 'register')
 */
function mostraTab(tab) {
    // Rimuove la classe active da tutti i tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Rimuove la classe active da tutti i form
    document.querySelectorAll('.auth-form').forEach(form => {
        form.classList.remove('active');
    });
    
    // Attiva il tab selezionato
    document.querySelector(`[onclick="mostraTab('${tab}')"]`).classList.add('active');
    document.getElementById(`${tab}-form`).classList.add('active');
}

// ============================================================================
// CARICAMENTO E GESTIONE DATI
// ============================================================================

/**
 * Carica i dati iniziali necessari per l'applicazione
 */
async function caricaDatiIniziali() {
    await caricaStrumenti();
    caricaDashboard();
}

/**
 * Carica e aggiorna i dati della dashboard
 * Mostra saldo contante, valore strumenti, NAV e P&L
 */
async function caricaDashboard() {
    try {
        const conto = await api('/conto/me');
        
        // Aggiorna i valori della dashboard
        document.getElementById('saldo-contante').textContent = formattaImporto(conto.saldoContante);
        document.getElementById('valore-strumenti').textContent = formattaImporto(conto.valoreStrumenti);
        document.getElementById('nav-totale').textContent = formattaImporto(conto.nav);
        document.getElementById('iban').textContent = conto.iban;
        
        // Calcola P&L non realizzato (differenza tra NAV e saldo iniziale di €10.000)
        const pnlNonRealizzato = conto.nav - 10000;
        document.getElementById('pnl-non-realizzato').textContent = formattaImporto(pnlNonRealizzato);
        
    } catch (error) {
        console.error('Errore durante il caricamento della dashboard:', error);
        mostraNotifica('Errore nel caricamento della dashboard', 'error');
    }
}

async function caricaStrumenti() {
    try {
        strumenti = await api('/strumenti/');
        
        // Aggiorna tabella strumenti
        const tbody = document.getElementById('strumenti-tbody');
        if (strumenti.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="text-center">Nessuno strumento disponibile</td></tr>';
        } else {
            tbody.innerHTML = strumenti.map(strumento => `
                <tr>
                    <td><strong>${strumento.ticker}</strong></td>
                    <td>${strumento.nome}</td>
                    <td>${formattaImporto(strumento.prezzoCorrente)}</td>
                    <td>
                        <button class="btn btn-primary btn-small" onclick="aggiornaPrezzi()">
                            <i class="fas fa-sync-alt"></i>
                        </button>
                    </td>
                </tr>
            `).join('');
        }
        
        // Aggiorna select acquisto
        const selectAcquisto = document.getElementById('acquisto-ticker');
        selectAcquisto.innerHTML = '<option value="">Seleziona strumento</option>' +
            strumenti.map(s => `<option value="${s.ticker}">${s.ticker} - ${s.nome}</option>`).join('');
        
        // Aggiungi listener per aggiornare prezzo
        selectAcquisto.addEventListener('change', aggiornaPrezzoAcquisto);
        
    } catch (error) {
        console.error('Errore caricamento strumenti:', error);
        mostraNotifica('Errore nel caricamento degli strumenti', 'error');
    }
}

async function caricaPortafoglio() {
    try {
        const portafoglio = await api('/portafoglio/');
        posizioni = portafoglio.posizioni;
        
        // Aggiorna tabella portafoglio
        const tbody = document.getElementById('portafoglio-tbody');
        if (posizioni.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">Nessuna posizione</td></tr>';
        } else {
            tbody.innerHTML = posizioni.map(posizione => `
                <tr>
                    <td><strong>${posizione.ticker}</strong></td>
                    <td>${posizione.quantita}</td>
                    <td>${formattaImporto(posizione.prezzoMedio)}</td>
                    <td>${formattaImporto(posizione.prezzoCorrente)}</td>
                    <td>${formattaImporto(posizione.valoreMercato)}</td>
                    <td class="${posizione.pnlNonRealizzato >= 0 ? 'positive' : 'negative'}">
                        ${formattaImporto(posizione.pnlNonRealizzato)}
                    </td>
                </tr>
            `).join('');
        }
        
        // Aggiorna select vendita
        const selectVendita = document.getElementById('vendita-ticker');
        selectVendita.innerHTML = '<option value="">Seleziona posizione</option>' +
            posizioni.map(p => `<option value="${p.ticker}" data-quantita="${p.quantita}">${p.ticker} (${p.quantita} unità)</option>`).join('');
        
        // Aggiungi listener per aggiornare quantità disponibile
        selectVendita.addEventListener('change', aggiornaQuantitaVendita);
        
    } catch (error) {
        console.error('Errore caricamento portafoglio:', error);
        mostraNotifica('Errore nel caricamento del portafoglio', 'error');
    }
}

async function caricaStorico() {
    try {
        const ordini = await api('/portafoglio/ordini');
        
        // Aggiorna tabella storico
        const tbody = document.getElementById('storico-tbody');
        if (ordini.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">Nessun ordine</td></tr>';
        } else {
            tbody.innerHTML = ordini.map(ordine => `
                <tr>
                    <td>${ordine.data}</td>
                    <td>
                        <span class="badge ${ordine.tipo === 'ACQUISTO' ? 'badge-success' : 'badge-danger'}">
                            ${ordine.tipo}
                        </span>
                    </td>
                    <td><strong>${ordine.ticker}</strong></td>
                    <td>${ordine.quantita}</td>
                    <td>${formattaImporto(ordine.prezzoEsecuzione)}</td>
                    <td class="${ordine.pnlRealizzato >= 0 ? 'positive' : 'negative'}">
                        ${ordine.pnlRealizzato ? formattaImporto(ordine.pnlRealizzato) : '-'}
                    </td>
                </tr>
            `).join('');
        }
        
    } catch (error) {
        console.error('Errore caricamento storico:', error);
        mostraNotifica('Errore nel caricamento dello storico', 'error');
    }
}

async function caricaTrading() {
    // Carica dati necessari per il trading
    await caricaStrumenti();
    await caricaPortafoglio();
}

// ============================================================================
// GESTIONE TRADING
// ============================================================================

/**
 * Gestisce l'ordine di acquisto di strumenti finanziari
 * @param {Event} event - Evento del form di acquisto
 */
async function handleAcquisto(event) {
    event.preventDefault();
    
    // Estrae i dati dal form
    const ticker = document.getElementById('acquisto-ticker').value.trim();
    const quantita = parseInt(document.getElementById('acquisto-quantita').value);
    
    // Validazione lato client
    if (!ticker) {
        mostraNotifica('Seleziona uno strumento da acquistare', 'error');
        return;
    }
    
    if (!quantita || quantita < 1) {
        mostraNotifica('Inserisci una quantità valida (minimo 1)', 'error');
        return;
    }
    
    if (quantita > 10000) {
        mostraNotifica('Quantità massima consentita: 10.000 unità', 'error');
        return;
    }
    
    // Verifica che lo strumento esista nella cache
    const strumento = strumenti.find(s => s.ticker === ticker);
    if (!strumento) {
        mostraNotifica('Strumento non trovato. Aggiorna i prezzi e riprova', 'error');
        return;
    }
    
    // Calcola il costo totale
    const costo = quantita * strumento.prezzoCorrente;
    
    // Conferma l'operazione
    const conferma = confirm(`Confermi l'acquisto di ${quantita} unità di ${ticker} per un totale di ${formattaImporto(costo)}?`);
    if (!conferma) {
        return;
    }
    
    try {
        mostraLoading(true);
        
        await api('/portafoglio/ordini/acquisto', {
            method: 'POST',
            body: JSON.stringify({ ticker, quantita })
        });
        
        mostraNotifica(`Acquisto di ${quantita} unità di ${ticker} completato!`, 'success');
        
        // Ricarica i dati e pulisce il form
        caricaDashboard();
        caricaPortafoglio();
        document.getElementById('acquisto-form').reset();
        document.getElementById('prezzo-acquisto').textContent = '-';
        document.getElementById('costo-acquisto').textContent = '-';
        
    } catch (error) {
        console.error('Errore durante l\'acquisto:', error);
        mostraNotifica(error.message || 'Errore durante l\'acquisto', 'error');
    } finally {
        mostraLoading(false);
    }
}

/**
 * Gestisce l'ordine di vendita di strumenti finanziari
 * @param {Event} event - Evento del form di vendita
 */
async function handleVendita(event) {
    event.preventDefault();
    
    // Estrae i dati dal form
    const ticker = document.getElementById('vendita-ticker').value.trim();
    const quantita = parseInt(document.getElementById('vendita-quantita').value);
    
    // Validazione lato client
    if (!ticker) {
        mostraNotifica('Seleziona una posizione da vendere', 'error');
        return;
    }
    
    if (!quantita || quantita < 1) {
        mostraNotifica('Inserisci una quantità valida (minimo 1)', 'error');
        return;
    }
    
    // Verifica che la posizione esista
    const posizione = posizioni.find(p => p.ticker === ticker);
    if (!posizione) {
        mostraNotifica('Posizione non trovata nel portafoglio', 'error');
        return;
    }
    
    // Verifica quantità disponibile
    if (quantita > posizione.quantita) {
        mostraNotifica(`Quantità non disponibile. Possedute: ${posizione.quantita}, Richieste: ${quantita}`, 'error');
        return;
    }
    
    // Calcola l'incasso totale
    const incasso = quantita * posizione.prezzoCorrente;
    
    // Conferma l'operazione
    const conferma = confirm(`Confermi la vendita di ${quantita} unità di ${ticker} per un incasso di ${formattaImporto(incasso)}?`);
    if (!conferma) {
        return;
    }
    
    try {
        mostraLoading(true);
        
        await api('/portafoglio/ordini/vendita', {
            method: 'POST',
            body: JSON.stringify({ ticker, quantita })
        });
        
        mostraNotifica(`Vendita di ${quantita} unità di ${ticker} completata!`, 'success');
        
        // Ricarica i dati e pulisce il form
        caricaDashboard();
        caricaPortafoglio();
        caricaStorico();
        document.getElementById('vendita-form').reset();
        document.getElementById('quantita-disponibile').textContent = '-';
        document.getElementById('incasso-vendita').textContent = '-';
        
    } catch (error) {
        console.error('Errore durante la vendita:', error);
        mostraNotifica(error.message || 'Errore durante la vendita', 'error');
    } finally {
        mostraLoading(false);
    }
}

async function aggiornaPrezzi() {
    try {
        mostraLoading(true);
        
        strumenti = await api('/quotazioni/aggiorna', {
            method: 'POST'
        });
        
        mostraNotifica('Prezzi aggiornati con successo!', 'success');
        
        // Ricarica le sezioni
        caricaStrumenti();
        caricaPortafoglio();
        caricaDashboard();
        
    } catch (error) {
        console.error('Errore aggiornamento prezzi:', error);
        mostraNotifica('Errore nell\'aggiornamento dei prezzi', 'error');
    } finally {
        mostraLoading(false);
    }
}

// ============================================================================
// HELPER PER TRADING
// ============================================================================

/**
 * Aggiorna i campi di prezzo e costo per l'acquisto
 * Include validazione in tempo reale
 */
function aggiornaPrezzoAcquisto() {
    const ticker = document.getElementById('acquisto-ticker').value;
    const quantita = parseInt(document.getElementById('acquisto-quantita').value) || 0;
    
    if (ticker) {
        const strumento = strumenti.find(s => s.ticker === ticker);
        if (strumento) {
            document.getElementById('prezzo-acquisto').textContent = formattaImporto(strumento.prezzoCorrente);
            
            if (quantita > 0) {
                const costo = quantita * strumento.prezzoCorrente;
                document.getElementById('costo-acquisto').textContent = formattaImporto(costo);
                
                // Validazione in tempo reale
                if (quantita > 10000) {
                    document.getElementById('costo-acquisto').style.color = 'red';
                    document.getElementById('costo-acquisto').textContent += ' (Quantità eccessiva)';
                } else {
                    document.getElementById('costo-acquisto').style.color = '';
                }
            } else {
                document.getElementById('costo-acquisto').textContent = '-';
                document.getElementById('costo-acquisto').style.color = '';
            }
        }
    } else {
        document.getElementById('prezzo-acquisto').textContent = '-';
        document.getElementById('costo-acquisto').textContent = '-';
        document.getElementById('costo-acquisto').style.color = '';
    }
}

/**
 * Aggiorna i campi di quantità e incasso per la vendita
 * Include validazione in tempo reale
 */
function aggiornaQuantitaVendita() {
    const select = document.getElementById('vendita-ticker');
    const quantitaDisponibile = parseInt(select.options[select.selectedIndex]?.dataset.quantita) || 0;
    const quantitaInput = parseInt(document.getElementById('vendita-quantita').value) || 0;
    
    document.getElementById('quantita-disponibile').textContent = `${quantitaDisponibile} unità`;
    
    if (quantitaInput > 0 && select.value) {
        const strumento = strumenti.find(s => s.ticker === select.value);
        if (strumento) {
            const incasso = quantitaInput * strumento.prezzoCorrente;
            document.getElementById('incasso-vendita').textContent = formattaImporto(incasso);
            
            // Validazione in tempo reale
            if (quantitaInput > quantitaDisponibile) {
                document.getElementById('incasso-vendita').style.color = 'red';
                document.getElementById('incasso-vendita').textContent += ' (Quantità eccessiva)';
            } else {
                document.getElementById('incasso-vendita').style.color = '';
            }
        }
    } else {
        document.getElementById('incasso-vendita').textContent = '-';
        document.getElementById('incasso-vendita').style.color = '';
    }
}

// Aggiungi listener per aggiornare i campi in tempo reale
document.addEventListener('DOMContentLoaded', function() {
    const quantitaAcquisto = document.getElementById('acquisto-quantita');
    if (quantitaAcquisto) {
        quantitaAcquisto.addEventListener('input', aggiornaPrezzoAcquisto);
    }
    
    const quantitaVendita = document.getElementById('vendita-quantita');
    if (quantitaVendita) {
        quantitaVendita.addEventListener('input', aggiornaQuantitaVendita);
    }
});

// ============================================================================
// FUNZIONI DI UTILITÀ
// ============================================================================

/**
 * Formatta un importo numerico in valuta Euro con formato italiano
 * @param {number} importo - Importo da formattare
 * @returns {string} Importo formattato (es. "€ 1.234,56")
 */
function formattaImporto(importo) {
    return new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    }).format(importo);
}

/**
 * Mostra una notifica all'utente con il messaggio specificato
 * @param {string} messaggio - Testo del messaggio da mostrare
 * @param {string} tipo - Tipo di notifica ('success', 'error', 'info', 'warning')
 */
function mostraNotifica(messaggio, tipo = 'info') {
    const container = document.getElementById('notifications');
    
    // Crea l'elemento della notifica
    const notification = document.createElement('div');
    notification.className = `notification ${tipo}`;
    notification.textContent = messaggio;
    
    // Aggiunge la notifica al container
    container.appendChild(notification);
    
    // Rimuove automaticamente la notifica dopo 5 secondi
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

/**
 * Mostra o nasconde l'indicatore di caricamento
 * @param {boolean} mostra
 */
function mostraLoading(mostra) {
    const loading = document.getElementById('loading');
    if (mostra) {
        loading.classList.add('show');
    } else {
        loading.classList.remove('show');
    }
}
