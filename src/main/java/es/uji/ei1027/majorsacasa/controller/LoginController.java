package es.uji.ei1027.majorsacasa.controller;


import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.uji.ei1027.majorsacasa.dao.UsuarioDao;
import es.uji.ei1027.majorsacasa.model.Usuario;

class LoginValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Usuario.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        // Combrobar que campos no queden vacíos
        Usuario userDetails = (Usuario) obj;
        if(userDetails.getNick().trim().equals(""))
            errors.rejectValue("nick", "obligatorio", "El nick es un campo obligatorio");
        if(userDetails.getPass().trim().equals(""))
            errors.rejectValue("pass", "obligatorio", "La contraseña es un campo obligatorio");
    }
}

@Controller
public class LoginController {
    @Autowired
    private UsuarioDao userDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new Usuario());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") Usuario user,
                             BindingResult bindingResult, HttpSession session) {
        LoginValidator loginValidator = new LoginValidator();
        loginValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }
        // Comprobar que el login es correcto
        user = userDao.loadUserByNick(user.getNick(), user.getPass());
        if (user == null) {
            bindingResult.rejectValue("pass", "badpw", "Contraseña incorrecta");
            return "login";
        }
        // Autenticación correctat.
        // Guardamos los datos de el usuario autenticado en la session.
        session.setAttribute("user", user);

        // Torna a la pàgina principal
        String nextUrl = (String)session.getAttribute("nextURL");

        if(nextUrl != null) {
            session.removeAttribute("nextURL"); // Borramos atributo que no vamos a volver a usar
            return "redirect:" + nextUrl;
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

