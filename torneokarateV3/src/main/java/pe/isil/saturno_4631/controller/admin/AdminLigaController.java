package pe.isil.saturno_4631.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.saturno_4631.model.Liga;
import pe.isil.saturno_4631.repository.LigaRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/ligas")
public class AdminLigaController {

    @Autowired
    private LigaRepository ligaRepository;

    @GetMapping("") //http://localhost:8080/cursos
    String index(Model model)
    {
        //Listar los cursos obtenidos de la base de datos en la pagina de inicio
        //1. Crear un objeto de listado de cursos
        List<Liga> ligas;

        //2. Obtener los cursos de la base de datos y guardarlos en la variable cursos
        ligas = ligaRepository.findAll();

        //3. Creamos un atributo en el modelo a enviar a la vista
        model.addAttribute("ligas", ligas);


        return "admin/ligas/index"; //retorna la vista o pagina HTML
    }

    //metodo o funcion para mostrar un formulario de ingreso de nuevos cursos
    @GetMapping("/nuevo") //localhost:8080/cursos/nuevo
    String nuevo(Model model)
    {
        model.addAttribute("liga", new Liga());
        return "admin/ligas/nuevo"; //se retorna la vista: nuevo.html
    }

    //metodo o funcion es para registrar o insertar un nuevo curso en la base de datos
    @PostMapping("/nuevo") //localhost:8080/cursos/nuevo
    String guardar(Model model, Liga liga)
    {
        try {
            ligaRepository.save(liga);
            return "redirect:/admin/ligas";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("liga", liga);
            model.addAttribute("error", "El RUC ingresado ya está registrado.");
            return "admin/ligas/nuevo"; // volver al formulario
        }
    }

    //Obtener los datos del curso y mostrarlo en un formulario o html
    @GetMapping("/editar/{id}")
    String editar(Model model, @PathVariable Integer id)
    {
        Optional<Liga> liga = ligaRepository.findById(id);
        model.addAttribute("liga", liga);
        return "admin/ligas/editar";
    }

    @PostMapping("/editar/{id}")
    String actualizar(Model model, @PathVariable Integer id, Liga liga, RedirectAttributes ra) {
        //1. obtener el curso por su id en bd
        Optional<Liga> ligaFromDB = ligaRepository.findById(id);

        //2. asignamos los valores del curso a actualizar
        ligaFromDB.get().setNombre(liga.getNombre());
        ligaFromDB.get().setRuc(liga.getRuc());
        //3. grabar
        ligaRepository.save(ligaFromDB.get());
        ra.addFlashAttribute("msgExito", "¡Academia actualizada!");
        return "redirect:/admin/ligas";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar (@PathVariable Integer id, RedirectAttributes ra)
    {
        ligaRepository.deleteById(id);
        ra.addFlashAttribute("Academia Eliminada!!!");
        return "redirect:/admin/ligas";
    }
}
