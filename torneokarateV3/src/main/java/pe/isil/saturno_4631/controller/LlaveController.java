package pe.isil.saturno_4631.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.isil.saturno_4631.model.Karateca;
import pe.isil.saturno_4631.model.Llave;
import pe.isil.saturno_4631.model.LlaveFormWrapper;
import pe.isil.saturno_4631.repository.KaratecaRepository;
import pe.isil.saturno_4631.repository.LlaveRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/llaves")
public class LlaveController {

    @Autowired
    private KaratecaRepository karatecaRepository;

    @Autowired
    private LlaveRepository llaveRepository;

    @GetMapping("/generar")
    public String generarLlaves(Model model) {
        List<Karateca> karatecas = karatecaRepository.findAll();

        Map<String, List<Karateca>> grupos = karatecas.stream()
                .collect(Collectors.groupingBy(k -> {
                    int edadGrupo = (k.getEdad() / 5) * 5;
                    int pesoGrupo = (k.getPeso() / 10) * 10;
                    String rangoGrupo = agruparRango(k.getRango());
                    return edadGrupo + "-" + pesoGrupo + "-" + rangoGrupo;
                }));

        List<Llave> llavesGeneradas = new ArrayList<>();

        for (List<Karateca> grupo : grupos.values()) {
            Collections.shuffle(grupo); // mezclar aleatoriamente
            for (int i = 0; i + 1 < grupo.size(); i += 2) {
                Llave llave = new Llave();
                llave.setIdKarateca1(grupo.get(i));
                llave.setIdKarateca2(grupo.get(i + 1));
                llave.setRonda(1); // primera ronda
                llavesGeneradas.add(llave);
            }
        }

        model.addAttribute("llaves", llavesGeneradas);
        return "vistaLlaves"; // muestra los enfrentamientos antes de guardar
    }

    private String agruparRango(int rango) {
        if (rango >= 1 && rango <= 3) return "1-3";
        if (rango >= 4 && rango <= 6) return "4-6";
        if (rango >= 7 && rango <= 8) return "7-8";
        if (rango >= 9 && rango <= 10) return "9-10";
        return "otro"; // para casos fuera de rango
    }

    @PostMapping("/guardar")
    public String guardarLlaves(@ModelAttribute("llaves") LlaveFormWrapper wrapper) {
        llaveRepository.saveAll(wrapper.getLlaves());
        return "redirect:/";
    }

    @GetMapping("/listarLlaves")
    public String listarLlaves(Model model) {
        List<Llave> llaves = llaveRepository.findAll();
        model.addAttribute("llaves", llaves);
        return "listarLlaves";
    }
}
