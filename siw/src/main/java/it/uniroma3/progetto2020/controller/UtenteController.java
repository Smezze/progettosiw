package it.uniroma3.progetto2020.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.progetto2020.model.Utente;
import it.uniroma3.progetto2020.service.UtenteService;
import it.uniroma3.progetto2020.session.SessionData;

@Controller
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private SessionData session;
	
	
	@RequestMapping("/utente/me")
	public String myProfile(Model model) {
		model.addAttribute("utente",this.utenteService.getUtenteById(this.session.getLoggedUser().getId()).get());
		return "utenti/utente";
	}
	@RequestMapping("/utenti")
	public String allUtenti(Model model) {
		model.addAttribute("utenti",this.utenteService.getAllUtenti());
		return "utenti/utenti";
	}
	
	@RequestMapping("/utente/{id}")
	public String getUtenteByUsername(Model model,@PathVariable Long id) {
		model.addAttribute("utente",this.utenteService.getUtenteById(id).get());
		return "utenti/utente";
	}
	
	@RequestMapping(value="/utenti/update/{id}",method=RequestMethod.GET)
	public String updateUtenteById(Model model,@PathVariable("id") Long id) {
		model.addAttribute("utente", this.utenteService.getUtenteById(id).get());
		return "utenti/updateUtente";
	}
	
	@RequestMapping(value="/utenti/update",method=RequestMethod.POST)
	public String processUpdateUtenteById(@ModelAttribute("updateUtente") Utente utente) {
		
		utente.setModifica(LocalDateTime.now());
		this.utenteService.saveUtente(utente);
		return "redirect:/";
	}
	
	@RequestMapping(value="/utenti/delete/{id}",method=RequestMethod.GET)
	public String delete(Model model,@PathVariable("id") Long id) {
		model.addAttribute("utente",this.utenteService.getUtenteById(id).get());
		return "utenti/deleteUtente";
	}
	
	@RequestMapping(value="/utenti/delete",method=RequestMethod.POST)
	public String processDelete(@ModelAttribute("utente") Utente utente) {
		this.utenteService.deleteUtente(utente.getId());
		return "redirect:/utenti";
	}

}
