package pe.isil.saturno_4631.controller;

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
import pe.isil.saturno_4631.model.Llave;
import pe.isil.saturno_4631.repository.KaratecaRepository;
import pe.isil.saturno_4631.repository.LigaRepository;
import pe.isil.saturno_4631.repository.LlaveRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("")
public class KaratecaController {

    @Autowired
    private KaratecaRepository karatecaRepository;
    @Autowired
    private LigaRepository ligaRepository;
    @Autowired
    private LlaveRepository llaveRepository;

    @GetMapping("")
    public String index(Model model) {

        return "/index";
    }

    @GetMapping("/encuentros")
    public String encuentros(Model model) {

        return "/encuentros";
    }


    @GetMapping("/nuevoKarateca")
    String nuevoContacto(Model model)
    {
        model.addAttribute("karateca", new Karateca());
        model.addAttribute("ligas", ligaRepository.findAll()); // Lista de ligas
        return "/nuevoKarateca";
    }

    @PostMapping("/nuevoKarateca")
    String guardar(Model model, Karateca karateca)
    {
        try {
            karatecaRepository.save(karateca);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("karateca", karateca);
            model.addAttribute("error", "Revisar y rellanar el formulario correctamente.");
            return "nuevoKarateca"; // volver al formulario
        }
    }
}
