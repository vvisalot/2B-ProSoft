package grupo2b.odiroute.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Ajusta según el path de tus endpoints
                .allowedOrigins("http://localhost:5173") // Origen del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permitir todos los headers
                .allowCredentials(true); // Permitir cookies y autenticación
    }
}
