package pe.isil.saturno_4631.controller.liga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.saturno_4631.model.Karateca;
import pe.isil.saturno_4631.model.Liga;
import pe.isil.saturno_4631.repository.KaratecaRepository;
import pe.isil.saturno_4631.repository.LigaRepository;

import java.util.Optional;

@Controller
@RequestMapping("/ligas/karatecas")
public class LigaKaratecaController {

    @Autowired
    private KaratecaRepository karatecaRepository;
    @Autowired
    private LigaRepository ligaRepository;

    @GetMapping("")
    String indexKarateca(Model model, @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable,
                         @RequestParam(required = false) String nombre) {
        Page<Karateca> karatecas;

        if (nombre != null && !nombre.trim().isEmpty()){
            karatecas = karatecaRepository.findByNombreCompletoContaining(nombre, pageable);
        } else{
            karatecas = karatecaRepository.findAll(pageable);
        }

        model.addAttribute("karatecas", karatecas);

        return "ligas/karatecas/index";
    }


    @GetMapping("/nuevo")
    String nuevoContacto(Model model)
    {
        model.addAttribute("karateca", new Karateca());
        model.addAttribute("ligas", ligaRepository.findAll()); // Lista de ligas
        return "ligas/karatecas/nuevo";
    }

    @PostMapping("/nuevo")
    String guardar(Model model, Karateca karateca)
    {
        try {
            karatecaRepository.save(karateca);
            return "redirect:/ligas/karatecas";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("karateca", karateca);
            model.addAttribute("error", "Revisar y rellanar el formulario correctamente.");
            return "ligas/karatecas/nuevo"; // volver al formulario
        }
    }

    @GetMapping("/editar/{id}")
    String editar(Model model, @PathVariable Integer id)
    {

        Karateca karateca = karatecaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id inválido: " + id));
        model.addAttribute("karateca", karateca);
        model.addAttribute("ligas", ligaRepository.findAll());
        return "ligas/karatecas/editar";
    }

    @PostMapping("/editar/{id}")
    String actualizar(Model model, @PathVariable Integer id, Karateca karateca, RedirectAttributes ra) {
        //1. obtener el curso por su id en bd
        Optional<Karateca> karatecaFromDB = karatecaRepository.findById(id);

        //2. asignamos los valores del curso a actualizar
        karatecaFromDB.get().setNombreCompleto(karateca.getNombreCompleto());
        karatecaFromDB.get().setDni(karateca.getDni());
        karatecaFromDB.get().setEdad(karateca.getEdad());
        karatecaFromDB.get().setPeso(karateca.getPeso());
        karatecaFromDB.get().setSexo(karateca.getSexo());
        karatecaFromDB.get().setRango(karateca.getRango());
        karatecaFromDB.get().setModalidad(karateca.getModalidad());
        karatecaFromDB.get().setLiga(karateca.getLiga());
        //3. grabar
        karatecaRepository.save(karatecaFromDB.get());
        ra.addFlashAttribute("msgExito", "¡Karateca actualizada!");
        return "redirect:/ligas/karatecas";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar (@PathVariable Integer id, RedirectAttributes ra)
    {
        karatecaRepository.deleteById(id);
        ra.addFlashAttribute("Karateca Eliminada!!!");
        return "redirect:/ligas/karatecas";
    }
}
