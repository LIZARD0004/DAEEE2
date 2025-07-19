package pe.isil.saturno_4631.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.saturno_4631.model.Usuario;
import pe.isil.saturno_4631.repository.UsuarioRepository;

@Controller
@RequestMapping("")
public class RegistroController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String index(Model model){
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Validated Usuario usuario, BindingResult bindingResult, RedirectAttributes ra, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("usuario",usuario);
            return "login";
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
            return "registro";
        }

        //Asignamos lo valores
        //Encryptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        //Asignar un rol de estudiante por defecto
        usuario.setRol(Usuario.Rol.ADMIN);

        usuarioRepository.save(usuario);
        ra.addFlashAttribute("registroExitoso","Usuario Registrado");

        return "redirect:/login";
    }
}
