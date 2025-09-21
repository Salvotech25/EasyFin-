package it.easyfin.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Applicazione principale EasyFin Portafogli
 * 
 * Simulatore di trading finanziario con:
 * - Gestione portafoglio virtuale
 * - Ordini di acquisto/vendita
 * - Calcolo P&L realizzato e non realizzato
 * - Simulazione movimenti di mercato
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("🚀 EasyFin Backend avviato su http://localhost:8082");
        System.out.println("📚 Swagger UI: http://localhost:8082/swagger-ui/index.html");
        System.out.println("🗄️ H2 Console: http://localhost:8082/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:easyfin");
    }
}
