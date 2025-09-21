const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');

const app = express();
const PORT = 3000;

// Middleware CORS
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
    
    if (req.method === 'OPTIONS') {
        res.sendStatus(200);
    } else {
        next();
    }
});

// Middleware per il proxy delle API
app.use('/api', createProxyMiddleware({
    target: 'http://localhost:8082',
    changeOrigin: true,
    logLevel: 'debug',
    onProxyReq: (proxyReq, req, res) => {
        console.log('Proxying request:', req.method, req.url);
    },
    onProxyRes: (proxyRes, req, res) => {
        console.log('Proxy response:', proxyRes.statusCode, req.url);
    },
    onError: (err, req, res) => {
        console.error('Errore proxy:', err);
        res.status(500).json({ 
            errore: 'Servizio backend non disponibile',
            messaggio: err.message 
        });
    }
}));

// Servi file statici dalla cartella frontend
app.use(express.static(path.join(__dirname, '..', 'frontend')));

// Fallback per SPA - tutte le altre richieste vanno a index.html
app.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'frontend', 'index.html'));
});

// Avvio del server
app.listen(PORT, () => {
    console.log(' EasyFin Frontend Server avviato');
    console.log(`Frontend disponibile su: http://localhost:${PORT}`);
    console.log(` Proxy API verso: http://localhost:8082`);
});

// Gestione chiusura server
process.on('SIGINT', () => {
    console.log('\n🛑 Chiusura server in corso...');
    process.exit(0);
});

process.on('SIGTERM', () => {
    console.log('\n🛑 Chiusura server in corso...');
    process.exit(0);
});
