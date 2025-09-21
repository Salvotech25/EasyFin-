# EasyFin - Simulatore di Trading

## Cos'è EasyFin?

EasyFin è un'applicazione web che simula un ambiente di trading finanziario. Ho sviluppato questo progetto per la mia tesi universitaria per dimostrare le competenze acquisite in programmazione web.

**Funzionalità principali:**
- Registrazione e login degli utenti
- Gestione di un portafoglio virtuale (saldo iniziale €10.000)
- Acquisto e vendita di azioni di aziende famose
- Visualizzazione profitti e perdite in tempo reale
- Cronologia completa delle operazioni

## Tecnologie Utilizzate

**Backend (Java):**
- Spring Boot per le API REST
- Database H2 in memoria per i dati
- Swagger per documentare le API
- Autenticazione con token JWT
- Gestione centralizzata degli errori

**Frontend (JavaScript):**
- HTML, CSS e JavaScript vanilla
- Design responsive per mobile
- Comunicazione con backend tramite fetch API
- Validazione client-side

**Server:**
- Node.js con Express per servire i file statici

## Come Avviare il Progetto

**Requisiti:**
- Java 17 o superiore
- Node.js 18 o superiore
- Maven installato

**1. Avvia il Backend:**
```bash
cd backend
mvn spring-boot:run
```

**2. Avvia il Frontend:**
```bash
cd server
npm install
npm start
```

**3. Apri il browser su:**
- http://localhost:3000 (applicazione web)
- http://localhost:8082/swagger-ui/index.html (documentazione API)

## Struttura del Progetto

```
easyFin/
├── backend/          # Parte Java (Spring Boot)
│   ├── controller/   # Gestisce le richieste API
│   ├── servizio/     # Logica di business
│   ├── dominio/      # Modelli dei dati
│   └── repository/   # Accesso al database
├── frontend/         # Parte web (HTML/CSS/JS)
│   ├── index.html    # Pagina principale
│   ├── app.js        # Logica JavaScript
│   └── styles.css    # Stili CSS
└── server/           # Server Node.js
    └── server.js     # Configurazione server
```

## Funzionalità Implementate

**Autenticazione:**
- Registrazione nuovi utenti con validazione
- Login con email e password
- Gestione sessioni con token JWT
- Logout automatico quando il token scade

**Dashboard:**
- Visualizzazione saldo contante disponibile
- Valore totale del portafoglio
- Profitti e perdite non realizzati
- Aggiornamento in tempo reale

**Trading:**
- Acquisto di azioni con controllo saldo
- Vendita di posizioni esistenti
- Calcolo automatico costi e commissioni
- Validazione quantità e disponibilità

**Portafoglio:**
- Lista delle posizioni possedute
- Profitti/perdite per ogni strumento
- Valore di mercato aggiornato
- Percentuali di variazione

**Storico:**
- Cronologia completa degli ordini
- Profitti e perdite realizzati
- Filtri per data e tipo operazione

## Azioni Disponibili

Il simulatore include 5 aziende famose:
- AAPL - Apple Inc.
- GOOGL - Alphabet (Google)
- MSFT - Microsoft Corporation
- TSLA - Tesla Inc.
- AMZN - Amazon.com Inc.

I prezzi vengono aggiornati casualmente per simulare un mercato reale.

## Come Testare l'Applicazione

### Test Completo del Flusso

**1. Avvio dell'applicazione:**
```bash
# Terminale 1 - Backend
cd backend
mvn spring-boot:run

# Terminale 2 - Frontend
cd server
npm start
```

**2. Test dell'interfaccia web:**
- Vai su http://localhost:3000
- Registrati con una nuova email
- Effettua il login
- Compra alcune azioni (es. 10 azioni Apple)
- Controlla il portafoglio
- Vendi parte delle posizioni
- Verifica lo storico degli ordini

**3. Test delle validazioni:**
- Prova a comprare più azioni del saldo disponibile
- Prova a vendere più azioni di quelle possedute
- Prova a registrarti con un'email già esistente
- Testa il logout e il login automatico

### Test delle API con Swagger

**1. Apri Swagger:**
- Vai su http://localhost:8082/swagger-ui/index.html

**2. Test di autenticazione:**
- POST /api/auth/register - Registra un nuovo utente
- POST /api/auth/login - Effettua il login e copia il token

**3. Configura l'autorizzazione:**
- Clicca "Authorize" in alto a destra
- Inserisci il token (senza "Bearer ")
- Clicca "Authorize" e poi "Close"

**4. Test delle API protette:**
- GET /api/conto/me - Ottieni dettagli conto
- GET /api/portafoglio/ - Visualizza portafoglio
- POST /api/portafoglio/ordini/acquisto - Acquista azioni
- POST /api/portafoglio/ordini/vendita - Vendi azioni
- GET /api/portafoglio/ordini - Storico ordini

### Test del Database H2

**1. Accedi alla console H2:**
- Vai su http://localhost:8082/h2-console
- JDBC URL: jdbc:h2:mem:easyfin
- Username: sa
- Password: (lascia vuoto)

**2. Query utili per test:**
```sql
-- Visualizza tutti gli utenti
SELECT * FROM UTENTE;

-- Visualizza tutti i conti
SELECT * FROM CONTO;

-- Visualizza tutte le posizioni
SELECT * FROM POSIZIONE;

-- Visualizza tutti gli ordini
SELECT * FROM ORDINE;

-- Visualizza tutti i movimenti di cassa
SELECT * FROM MOVIMENTO_CASSA;
```

### Test dei Profitti e Perdite

**1. Scenario di test:**
- Registra un utente
- Acquista 100 azioni Apple a €150
- Il prezzo sale a €160 (guadagno non realizzato)
- Vendine 50 (guadagno realizzato)
- Verifica i calcoli nel portafoglio

**2. Verifica calcoli:**
- Profitto realizzato: 50 × (160 - 150) = €500
- Profitto non realizzato: 50 × (160 - 150) = €500
- Saldo contante: €10.000 - (100 × 150) + (50 × 160) = €8.000

## Obiettivi del Progetto

Questo progetto dimostra le competenze acquisite durante il corso di studi:
- Sviluppo full-stack (frontend + backend)
- Architettura software moderna con Spring Boot
- Integrazione tra sistemi diversi
- Gestione errori e validazioni robuste
- User experience design responsive
- Documentazione API con Swagger
- Test e debugging di applicazioni web

## Autore

**Studente Universitario**  
Progetto di Tesi - Anno Accademico 2025

*EasyFin - Simulatore di Trading per scopi didattici*