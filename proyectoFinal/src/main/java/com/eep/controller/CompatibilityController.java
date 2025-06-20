// src/main/java/com/eep/controller/CompatibilityController.java
package com.eep.controller;

import com.eep.dto.CompatibilityForm;
import com.eep.dto.CompatibilityResult;
import com.eep.dto.SystemSpec;
import com.eep.model.Cpu;
import com.eep.model.Gpu;
import com.eep.repository.CpuRepository;
import com.eep.repository.GpuRepository;
import com.eep.service.CompatibilityService;
import com.eep.service.RawgService;
import com.eep.service.RawgService.PlatformInfo;
import com.eep.service.RawgService.RawgGameInfo;
import com.eep.service.RawgService.RawgSearchResult;
import com.eep.service.RawgService.SearchGame;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/compatibility")
public class CompatibilityController {

    private final CpuRepository cpuRepo;
    private final GpuRepository gpuRepo;
    private final CompatibilityService compat;
    private final RawgService rawgService;

    public CompatibilityController(CpuRepository cpuRepo,
                                   GpuRepository gpuRepo,
                                   CompatibilityService compat,
                                   RawgService rawgService) {
        this.cpuRepo     = cpuRepo;
        this.gpuRepo     = gpuRepo;
        this.compat      = compat;
        this.rawgService = rawgService;
    }

    @GetMapping
    public String base() {
        return "redirect:/compatibility/check";
    }

    @GetMapping("/check")
    public String form(Model model) {
        model.addAttribute("form", new CompatibilityForm());
        model.addAttribute("cpus", cpuRepo.findAll());
        model.addAttribute("gpus", gpuRepo.findAll());
        // <-- Añadimos siempre las mismas claves aunque sea a null
        model.addAttribute("result", new CompatibilityResult());

        model.addAttribute("requirements", null);
        return "compatibility";
    }

    @PostMapping("/check")
    public String process(@ModelAttribute CompatibilityForm form, Model model) {
        // Resolver nombres a partir de los IDs
        Cpu cpu = cpuRepo.findById(form.getCpuId()).orElseThrow();
        Gpu gpu = gpuRepo.findById(form.getGpuId()).orElseThrow();

        SystemSpec sys = new SystemSpec(
            cpu.getName(),
            gpu.getName(),
            form.getRam(),
            form.getStorage(),
            form.getGameSlug()
        );

        RawgGameInfo gameInfo = rawgService.fetchGameInfo(form.getGameSlug()).block();


        // <<< Aquí añades el logging >>> 
        gameInfo.platforms().forEach(p ->
            System.out.println("PLATFORM → " 
                + p.platform().name() 
                + " • min=" + p.requirements().minimum() 
                + " • rec=" + p.requirements().recommended()
            )
        );
        
        // luego filtras “PC”
        PlatformInfo pcPlatform = gameInfo.platforms().stream()
            .filter(p -> "PC".equalsIgnoreCase(p.platform().name()))
            .findFirst()
            .orElse(null);
        

        CompatibilityResult result = compat.evaluate(sys, gameInfo);

        model.addAttribute("result", result);
        model.addAttribute("form", form);
        model.addAttribute("cpus", cpuRepo.findAll());
        model.addAttribute("gpus", gpuRepo.findAll());
        model.addAttribute("requirements", gameInfo);
        return "compatibility";
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<List<SearchGame>> searchGames(@RequestParam("query") String q) {
        return rawgService.searchGames(q).map(RawgSearchResult::results);
    }

    @GetMapping(path = "/search/gpus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Gpu> searchGpus(@RequestParam("query") String q) {
        return gpuRepo.findByNameContainingIgnoreCase(q);
    }
    
    @GetMapping(path = "/search/cpus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, Object>> searchCpus(@RequestParam("query") String q) {
        return cpuRepo.findByNameContainingIgnoreCase(q).stream()
            .map(cpu -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id",   cpu.getId());
                m.put("name", cpu.getName());
                return m;
            })
            .collect(Collectors.toList());
    }

    
    
}
