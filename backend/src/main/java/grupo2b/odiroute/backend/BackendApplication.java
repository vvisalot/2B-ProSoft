package grupo2b.odiroute.backend;

import grupo2b.odiroute.service.SimulatedAnnealingService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"grupo2b.odiroute"})
@ComponentScan(basePackages = {"grupo2b.odiroute"})
@EnableJpaRepositories(basePackages = "grupo2b.odiroute.repository")
public class BackendApplication {
//    @Autowired
//    private SimulatedAnnealingService simulatedAnnealingRunner;
    
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    
//    @PostConstruct
//    public void runSimulatedAnnealing() {
//        try {
//            simulatedAnnealingRunner.runSimulatedAnnealing(); // Llamar al método
//        } catch (IOException e) {
//            System.err.println("Error al ejecutar Simulated Annealing: " + e.getMessage());
//        }
//    }
}