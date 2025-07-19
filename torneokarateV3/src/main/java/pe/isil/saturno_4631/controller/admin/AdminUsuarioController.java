package pe.isil.saturno_4631.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.saturno_4631.model.Liga;
import pe.isil.saturno_4631.model.Usuario;
import pe.isil.saturno_4631.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    String index (Model model)
    {
        List<Usuario> usuarios;

        usuarios =usuarioRepository.findAll();

        model.addAttribute("usuarios", usuarios);

        return "admin/usuarios/index";
    }

    @GetMapping("/nuevo") //localhost:8080/cursos/nuevo
    String nuevo(Model model)
    {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/nuevo"; //se retorna la vista: nuevo.html
    }

    @PostMapping("/nuevo") //localhost:8080/cursos/nuevo
    public String registrar(@Validated Usuario usuario, BindingResult bindingResult, RedirectAttributes ra, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("usuario",usuario);
            return "/nuevo";
        }

        //Validar si email existe
        String email = usuario.getEmail();
        boolean existeUsurio = usuarioRepository.existsByEmail(email);

        if (existeUsurio){
            bindingResult.rejectValue("email","EmailAlreadyExists");
        }

        //Validar las contraseñas
        if (!usuario.getPassword1().equals(usuario.getPassword2())){
            bindingResult.rejectValue("password1","PasswordNotEquals");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("usuario",usuario);
            return "/nuevo";
        }

        //Asignamos lo valores
        //Encryptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        //Asignar un rol de estudiante por defecto
        //usuario.setRol(Usuario.Rol.ADMIN);

        usuarioRepository.save(usuario);
        ra.addFlashAttribute("registroExitoso","Usuario Registrado");

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    String editar(Model model, @PathVariable Integer id, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuario", opt.get());
        return "admin/usuarios/editar";
    }

    @PostMapping("/editar/{id}")
    String actualizar(Model model, @PathVariable Integer id, Usuario usuario, RedirectAttributes ra) {
        //1. obtener el curso por su id en bd
        Optional<Usuario> usuarioFromDB = usuarioRepository.findById(id);

        //2. asignamos los valores del curso a actualizar
        usuarioFromDB.get().setNombres(usuario.getNombres());
        usuarioFromDB.get().setApellidos(usuario.getApellidos());
        usuarioFromDB.get().setEmail(usuario.getEmail());
        usuarioFromDB.get().setRol(usuario.getRol());
        //3. grabar
        usuarioRepository.save(usuarioFromDB.get());
        ra.addFlashAttribute("msgExito", "¡Usuario actualizada!");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar (@PathVariable Integer id, RedirectAttributes ra)
    {
        usuarioRepository.deleteById(id);
        ra.addFlashAttribute("Usuario Eliminado!!!");
        return "redirect:/admin/usuarios";
    }
}
