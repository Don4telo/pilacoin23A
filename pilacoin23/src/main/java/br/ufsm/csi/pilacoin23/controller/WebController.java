package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.BlocoRepositorio;
import br.ufsm.csi.pilacoin23.Persistencia.BlocoValidadoRepositorio;
import br.ufsm.csi.pilacoin23.Persistencia.PilaRepositorio;
import br.ufsm.csi.pilacoin23.Persistencia.PilaValidadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    PilaRepositorio pilaRepositorio;
    @Autowired
    PilaValidadoRepositorio pilaValidadoRepositorio;
    @Autowired
    BlocoRepositorio blocoRepositorio;

    @Autowired
    BlocoValidadoRepositorio blocoValidadoRepositorio;


    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/pilacoins")
    public String showPilacoins(Model model) {
        model.addAttribute("pilacoins", pilaRepositorio.findAll());
        return "pilacoins";
    }

    @GetMapping("/validados")
    public String showValidados(Model model) {
        model.addAttribute("pilasValidados", pilaValidadoRepositorio.findAll());
        return "validados";
    }

    @GetMapping("/blocos")
    public String showBlocos(Model model) {
        model.addAttribute("blocos", blocoRepositorio.findAll());
        return "blocos";
    }

    @GetMapping("/blocosValidados")
    public String showBlocosValidados(Model model) {
        model.addAttribute("blocosValidados", blocoValidadoRepositorio.findAll());
        return "blocosValidados";
    }
}


