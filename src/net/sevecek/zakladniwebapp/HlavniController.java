package net.sevecek.zakladniwebapp;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class HlavniController {

    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public ModelAndView zobrazUkazku() {
        ModelAndView modelAndView = new ModelAndView(
                "/WEB-INF/view/ukazka.jsp");
        modelAndView.addObject("jmeno", "Java");
        return modelAndView;
    }
    
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.POST)
    public ModelAndView zpracujUkazku(Pes vyplnenyFormular){
        ModelAndView modelAndView = new ModelAndView(
                "/WEB-INF/view/vysledky.jsp");



        modelAndView.addObject("vysledek", ziskejVysledek(vyplnenyFormular));

        return modelAndView;
    }

    private Integer ziskejVysledek (Pes vyplnenyFormular){
        return vyplnenyFormular.getVelikost()+ vyplnenyFormular.getSrst()+vyplnenyFormular.getUsi()+vyplnenyFormular.getRoky() ;
    }
}
