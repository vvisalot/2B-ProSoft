package grupo2b.odiroute.controller;

import grupo2b.odiroute.service.OficinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OficinaController {
    private final OficinaService oficinaService;

    @Autowired
    public OficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @PostMapping("/cargarOficinas")
    public String cargarOficinas(@RequestParam String filePath) {
        oficinaService.guardarOficinasDesdeArchivo(filePath);
        return "Oficinas cargadas y guardadas en la base de datos.";
    }
}
