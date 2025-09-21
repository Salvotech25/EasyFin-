# Relazione Progetto EasyFin - Simulatore di Trading

## 1. Introduzione

EasyFin è un simulatore di trading finanziario sviluppato per scopi didattici nell'ambito del progetto di tesi universitaria. L'applicazione simula un ambiente di trading semplificato dove gli utenti possono gestire un portafoglio virtuale con un saldo iniziale di €10.000.

L'obiettivo principale è fornire un'esperienza educativa nella gestione di investimenti finanziari, permettendo agli utenti di comprendere i meccanismi di acquisto/vendita di strumenti finanziari, il calcolo del P&L (Profit & Loss) e la diversificazione del portafoglio.

Il progetto dimostra l'applicazione pratica delle competenze acquisite in programmazione web, architettura software e sviluppo full-stack.

## 2. Servizi e Casi d'Uso

### 2.1 Autenticazione
- **Registrazione**: Creazione di un nuovo utente con conto virtuale
- **Login**: Autenticazione tramite token JWT
- **Gestione sessioni**: Logout automatico quando il token scade

### 2.2 Gestione Conto
- **Saldo contante**: Visualizzazione del saldo disponibile
- **Movimenti cassa**: Storico delle operazioni di deposito/prelievo
- **IBAN virtuale**: Identificativo univoco del conto

### 2.3 Listino Strumenti
- **Visualizzazione**: Lista di strumenti finanziari con prezzi correnti
- **Aggiornamento prezzi**: Simulazione di movimenti di mercato con rumore ±2%

### 2.4 Trading
- **Ordini di acquisto**: Acquisto di strumenti con quantità intere
- **Ordini di vendita**: Vendita di posizioni esistenti
- **Esecuzione immediata**: Tutti gli ordini vengono eseguiti a mercato

### 2.5 Portafoglio
- **Posizioni**: Visualizzazione delle posizioni con prezzo medio
- **Valore di mercato**: Calcolo del valore corrente delle posizioni
- **P&L non realizzato**: Profitto/perdita su posizioni aperte
- **NAV (Net Asset Value)**: Valore totale del portafoglio

### 2.6 Storico
- **Ordini eseguiti**: Cronologia completa delle operazioni
- **P&L realizzato**: Profitti/perdite sulle vendite completate

## 3. Architettura

### 3.1 Architettura Generale
L'applicazione segue un'architettura a tre livelli:

```
Frontend (HTML/CSS/JS) ←→ Spring Boot REST API ←→ H2 Database
```

Il frontend comunica direttamente con il backend Spring Boot, eliminando la necessità del proxy Node.js per le chiamate API.

### 3.2 Frontend
- **Tecnologia**: HTML5, CSS3, JavaScript vanilla
- **Server**: Node.js + Express per servire file statici
- **Comunicazione**: Fetch API per chiamate REST al backend
- **Validazione**: Controlli client-side per migliorare l'esperienza utente

### 3.3 Backend
- **Framework**: Spring Boot 3.3.x con Java 17
- **Persistence**: Spring Data JPA + H2 in-memory
- **API**: REST con JSON, autenticazione Bearer token JWT
- **Documentazione**: Swagger/OpenAPI con configurazione personalizzata
- **Gestione errori**: Controller globale per messaggi user-friendly

### 3.4 Database
- **H2**: Database in-memory per semplicità didattica
- **Schema**: Generato automaticamente da Hibernate
- **Console**: Interfaccia web per debugging e ispezione dati
- **Credenziali**: Username 'sa', password vuota

## 4. Design

### 4.1 Diagramma ER (Entità-Relazione)

```
Utente (1) ←→ (1) Conto (1) ←→ (N) MovimentoCassa
   │
   │ (1)
   │
   │ (N)
   ↓
Ordine (N) ←→ (1) Strumento
   │
   │ (N)
   ↓
Posizione (N) ←→ (1) Strumento
```

### 4.2 UML delle Classi Principali

```
Utente
+ id: Long
+ nome: String
+ email: String (UNIQUE)
+ password: String
+ conto: Conto
+ ordini: List<Ordine>
+ posizioni: List<Posizione>

Conto
+ id: Long
+ iban: String
+ saldoContante: double
+ movimenti: List<MovimentoCassa>
+ deposita(importo, descrizione)
+ preleva(importo, descrizione)

Strumento
+ ticker: String (PK)
+ nome: String
+ prezzoCorrente: double

Posizione
+ id: Long
+ utente: Utente
+ strumento: Strumento
+ quantita: int
+ prezzoMedio: double

Ordine
+ id: Long
+ utente: Utente
+ tipo: TipoOrdine
+ ticker: String
+ quantita: int
+ prezzoEsecuzione: double
+ data: LocalDate
+ stato: StatoOrdine
+ pnlRealizzato: Double
```

## 5. Documentazione API

L'API è completamente documentata tramite Swagger/OpenAPI e disponibile su:
http://localhost:8082/swagger-ui/index.html

### Configurazione Swagger:
- **Autorizzazione globale**: Token JWT applicato a tutte le API protette
- **Interfaccia semplificata**: Nessun campo Authorization manuale nelle API
- **Messaggi di errore**: Guida chiara per l'autenticazione

### Endpoints Principali:

**Autenticazione:**
- `POST /api/auth/register` - Registrazione utente
- `POST /api/auth/login` - Login e ottenimento token

**Gestione Conto:**
- `GET /api/conto/me` - Dettagli conto corrente

**Strumenti e Quotazioni:**
- `GET /api/strumenti` - Lista strumenti disponibili
- `POST /api/quotazioni/aggiorna` - Aggiorna prezzi di mercato

**Portafoglio e Trading:**
- `GET /api/portafoglio` - Posizioni portafoglio
- `GET /api/portafoglio/ordini` - Storico ordini
- `POST /api/portafoglio/ordini/acquisto` - Ordine di acquisto
- `POST /api/portafoglio/ordini/vendita` - Ordine di vendita

## 6. Snippet di Codice Notevoli

### 6.1 Calcolo Prezzo Medio
```java
// Aggiornamento prezzo medio in caso di acquisti aggiuntivi
double prezzoMedioNuovo = (posizioneEsistente.getQuantita() * posizioneEsistente.getPrezzoMedio() + 
                          quantita * prezzoCorrente) / 
                         (posizioneEsistente.getQuantita() + quantita);
```

### 6.2 Calcolo P&L Realizzato
```java
// P&L realizzato su vendite
double pnlRealizzato = (prezzoCorrente - posizione.getPrezzoMedio()) * quantita;
```

### 6.3 Simulazione Rumore Mercato
```java
// Applicazione rumore ±2% ai prezzi
Random random = new Random();
double delta = (random.nextDouble() - 0.5) * 0.04; // [-2%, +2%]
double nuovoPrezzo = strumento.getPrezzoCorrente() * (1 + delta);
strumento.setPrezzoCorrente(Math.round(nuovoPrezzo * 100.0) / 100.0);
```

## 7. Processo di Sviluppo

### 7.1 Metodologia
Sviluppo iterativo con focus su:
1. **Prototipazione rapida**: Struttura base con H2 in-memory
2. **Test incrementali**: Verifica funzionalità una alla volta
3. **Refactoring continuo**: Miglioramento del codice durante lo sviluppo

### 7.2 Fasi di Sviluppo
1. **Setup progetto**: Configurazione Spring Boot e dipendenze
2. **Modello dati**: Creazione entità JPA e repository
3. **Servizi business**: Logica di trading e calcoli finanziari
4. **API REST**: Controller e documentazione Swagger
5. **Frontend**: Interfaccia utente con JavaScript vanilla
6. **Integrazione**: Test end-to-end e debugging

## 8. Test Funzionale

### 8.1 Scenari di Test
1. **Registrazione e Login**: Verifica creazione account e autenticazione
2. **Acquisto strumento**: Test ordine di acquisto con saldo sufficiente
3. **Vendita posizione**: Test ordine di vendita con posizione esistente
4. **Aggiornamento prezzi**: Verifica simulazione movimenti di mercato
5. **Calcoli P&L**: Verifica correttezza calcoli profitti/perdite

### 8.2 Screenshot Suggeriti
- Dashboard principale con saldo e posizioni
- Listino strumenti con prezzi aggiornati
- Form di acquisto/vendita
- Storico ordini con P&L realizzato
- Console H2 per verifica dati

## 9. Istruzioni di Esecuzione

### 9.1 Prerequisiti
```bash
# Verifica versioni
java --version    # Java 17+
mvn --version     # Maven 3.6+
node --version    # Node.js 18+
```

### 9.2 Avvio Backend
```bash
cd backend
mvn spring-boot:run
# Server disponibile su http://localhost:8082
```

### 9.3 Avvio Frontend
```bash
cd server
npm install
npm start
# Frontend disponibile su http://localhost:3000
```

### 9.4 Accesso Strumenti
- **Applicazione**: http://localhost:3000
- **Swagger UI**: http://localhost:8082/swagger-ui/index.html
- **H2 Console**: http://localhost:8082/h2-console

### 9.5 Test Completo dell'Applicazione

**Test dell'interfaccia web:**
1. Vai su http://localhost:3000
2. Registrati con una nuova email
3. Effettua il login
4. Compra alcune azioni (es. 10 azioni Apple)
5. Controlla il portafoglio e i profitti/perdite
6. Vendi parte delle posizioni
7. Verifica lo storico degli ordini

**Test delle API con Swagger:**
1. Vai su http://localhost:8082/swagger-ui/index.html
2. Registra un utente con POST /api/auth/register
3. Fai login con POST /api/auth/login e copia il token
4. Clicca "Authorize" in alto a destra e inserisci il token
5. Testa le API protette (GET /api/conto/me, GET /api/portafoglio, etc.)

**Test del database H2:**
1. Vai su http://localhost:8082/h2-console
2. JDBC URL: jdbc:h2:mem:easyfin
3. Username: sa, Password: (vuota)
4. Esegui query per verificare i dati salvati

## 10. Conclusioni

EasyFin rappresenta un esempio completo di applicazione full-stack moderna, sviluppata per dimostrare le competenze acquisite in programmazione web e architettura software. Il progetto evidenzia:

**Aspetti Tecnici:**
- **Architettura moderna**: Spring Boot 3, Java 17, H2 database
- **API-first approach**: Documentazione completa con Swagger/OpenAPI
- **Gestione errori**: Messaggi user-friendly e validazioni robuste
- **Autenticazione**: Sistema JWT con gestione sessioni

**Aspetti Didattici:**
- **Logica finanziaria**: Calcoli realistici di P&L e gestione portafoglio
- **Validazioni**: Controlli sia client-side che server-side
- **User Experience**: Interfaccia intuitiva e responsive
- **Debugging**: Console H2 per ispezione dati in tempo reale

**Competenze Dimostrate:**
- Sviluppo full-stack (frontend + backend)
- Integrazione tra tecnologie diverse
- Gestione errori e validazioni
- Documentazione API professionale
- Test e debugging di applicazioni web

Il progetto è strutturato per essere facilmente comprensibile e modificabile, rendendolo ideale per scopi educativi e come base per progetti più complessi. La separazione delle responsabilità e l'uso di tecnologie moderne rendono il codice mantenibile e estendibile.

**Studente Universitario**  
*Progetto di Tesi - Anno Accademico 2025*
