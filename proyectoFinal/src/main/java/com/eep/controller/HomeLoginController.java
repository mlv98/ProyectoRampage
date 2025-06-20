package com.eep.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eep.dto.UsuarioRegistroDto;
import com.eep.model.Game;
import com.eep.service.FavoriteService;
import com.eep.service.GameService;
import com.eep.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class HomeLoginController {

	private final UsuarioService usuarioService;
	private final GameService gameService;
	private final FavoriteService favoriteService;

	public HomeLoginController(UsuarioService usuarioService, GameService gameService,
			FavoriteService favoriteService) {
		this.usuarioService = usuarioService;
		this.gameService = gameService;
		this.favoriteService = favoriteService;
	}

	// Página principal usuario autenticado)
	@GetMapping("/")
	public String homePage() {
		return "redirect:/index"; // Muestra el index.html
	}

	@GetMapping("/index")
	public String indexPage(Model model, Principal principal) {
		
		List<Game> latest = gameService.fetchLatestGames(16);
		model.addAttribute("games", latest);

		
		List<Long> favIds = favoriteService.getFavorites(principal.getName()).stream().map(Game::getId).toList();
		model.addAttribute("favoriteIds", favIds);

		return "index";
	}

	
	@GetMapping("/login")
	public String loginPage() {
		return "login"; 
	}

	
	@GetMapping("/registro")
	public String registroPage(Model model) {
		model.addAttribute("usuario", new UsuarioRegistroDto()); 
		return "registro"; 
	}

	@PostMapping("/registro")
	public String registrarCuentaDeUsuario(@Valid UsuarioRegistroDto usuario, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "registro"; 
		}

		try {
			usuarioService.guardar(usuario);
			
			return "redirect:/login?exito=true";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("error", "El correo ya está registrado");
			return "registro";
		}

	}

}