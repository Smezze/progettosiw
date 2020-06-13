package it.uniroma3.progetto2020.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.progetto2020.model.Progetto;
import it.uniroma3.progetto2020.model.Utente;
import it.uniroma3.progetto2020.repository.ProjectRepository;
import it.uniroma3.progetto2020.service.ProjectService;
import it.uniroma3.progetto2020.service.UtenteService;
import it.uniroma3.progetto2020.session.SessionData;

import org.springframework.web.bind.WebDataBinder;

@Controller
public class ProgettoController {

	//	@InitBinder
	//	public void initBinder(WebDataBinder binder) {
	//	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	//	sdf.setLenient(true);
	//	binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	//	}

	@Autowired
	private ProjectService progettoService;

	@Autowired
	private SessionData session;

	@Autowired
	private UtenteService utenteService;

	@RequestMapping(value = "/progetti", method = RequestMethod.GET)
	public String progetto(Model model) {
		Utente u = this.session.getLoggedUser();
		model.addAttribute("progetto", new Progetto());
		model.addAttribute("progetti", this.progettoService.getByProprietario(u.getId()));
		return "progetti/progetto";
	}

	@RequestMapping(value = "/progetti", method = RequestMethod.POST)
	public String submitProgetto(@ModelAttribute("progetto") Progetto progetto, Model model) {
		Utente u = this.session.getLoggedUser();
		progetto.setProprietario(u);
		u.getProgettiPosseduti().add(progetto);
		this.utenteService.saveUtente(u);
		return "redirect:/progetti";
	}

	@RequestMapping(value = "/mod-prog/{id}", method = RequestMethod.GET)
	public String editShowProgetto(@PathVariable("id") Long id, Model model) {
		Progetto mod = this.progettoService.findProgetto(id);
		model.addAttribute("progettomod", mod);
		return "progetti/body-modal-mod-progetto";
	}

	@RequestMapping(value = "/delete-prog/{id}", method = RequestMethod.GET)
	public String deleteProgetto(@PathVariable("id") Long id, Model model) {
		this.progettoService.deleteProgetto(id);
		return "redirect:/progetti";
	}

	@RequestMapping(value = "/progetti-mod-send/{id}", method = RequestMethod.POST)
	public String editProgetto(@ModelAttribute("progettomod") Progetto progetto, @PathVariable("id") Long id, Model model) {
		progetto.setProprietario(this.session.getLoggedUser());
		this.progettoService.saveProgetto(progetto);
		return "redirect:/progetti";
	}
	
	@RequestMapping(value="/share-prog/{id}", method= RequestMethod.GET)
	public String shareShowProgetto(Model model,@PathVariable("id") Long id) {
		model.addAttribute("progettoshare",this.progettoService.findProgetto(id));
		return "progetti/progetti-share-send";
	}
	
	@RequestMapping(value="/progetti-share-send/{id}",method=RequestMethod.POST)
	public String shareProgetto(@ModelAttribute("progettoshare") Progetto progetto,@ModelAttribute("idUtente") Long idUtente) {
		//bisogna mettere sulla pagina la lista di tutti gli utenti a cui è possibile condividere il progetto
		//e bisogna fare una if che controlla che non sia già autorizzato (però si potrebbe fare un controllo anche qui e
		//shallona
		Utente u=this.utenteService.getUtenteById(idUtente).get();
		progetto.getUtentiautorizzati().add(u);
		u.getProgettiAutorizzati().add(progetto);
		return "redirect/progetti";
	}
	
	
	

	@PostMapping("/addProgetto")
	public String processForm(Progetto progetto) {

		return "showMessage";
	}
}
