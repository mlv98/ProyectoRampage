// src/main/java/com/eep/service/CompatibilityService.java
package com.eep.service;

import com.eep.dto.CompatibilityResult;
import com.eep.dto.SystemSpec;
import com.eep.model.Cpu;
import com.eep.model.Gpu;
import com.eep.repository.CpuRepository;
import com.eep.repository.GpuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompatibilityService {

    private static final Logger logger = LoggerFactory.getLogger(CompatibilityService.class);

    private final RawgService rawg;
    private final CpuRepository cpuRepo;
    private final GpuRepository gpuRepo;

    public CompatibilityService(RawgService rawg, CpuRepository cpuRepo, GpuRepository gpuRepo) {
        this.rawg    = rawg;
        this.cpuRepo = cpuRepo;
        this.gpuRepo = gpuRepo;
    }

    public Mono<CompatibilityResult> check(SystemSpec user, String gameSlug) {
        return rawg.fetchGameInfo(gameSlug)
                   .map(info -> evaluate(user, info));
    }

    public CompatibilityResult evaluate(SystemSpec user, RawgService.RawgGameInfo info) {
        // --- Extraer requisitos mínimos ---
        RawgService.PlatformInfo pc = info.platforms().stream()
            .filter(p -> "PC".equalsIgnoreCase(p.platform().name()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No hay requisitos para PC"));

        String minText     = Optional.ofNullable(pc.requirements().minimum()).orElse("");
        String reqCpuModel = extractModel(minText, "CPU");
        String reqGpuModel = extractModel(minText, "GPU");
        int    minRam      = extractGb(minText, "RAM");
        int    minStorage  = extractGb(minText, "storage");

        CompatibilityResult res = new CompatibilityResult();

     // --- Comparación CPU ---
        Cpu userCpu = cpuRepo.findByNameContainingIgnoreCase(user.getCpuName())
                             .stream().findFirst().orElse(null);
        Cpu refCpu  = cpuRepo.findByNameContainingIgnoreCase(reqCpuModel)
                             .stream().findFirst().orElse(null);

        CpuComparator cpuComparator = new CpuComparator(cpuRepo);
        StringBuilder cpuLog = new StringBuilder();
        boolean okCpu = cpuComparator.isUserCpuBetterOrEqual(
            user.getCpuName(), reqCpuModel, cpuLog
        );

        // Guardamos siempre el log de CPU
        res.setCpuLog(cpuLog.toString());

        // Ahora añadimos el issue correspondiente
        if (okCpu) {
            if (userCpu != null && refCpu != null
                && CpuComparator.compare(userCpu, refCpu) > 0) {
                // score usuario > score referencia
                res.addIssue("✅ Procesador superior: tu CPU supera el mínimo requerido.");
            } 
        } else {
            // no lo cumple, mostramos el detalle
            res.addIssue("❌ Procesador insuficiente:\n" + cpuLog.toString().trim());
        }


        // --- RAM y almacenamiento ---
        if (user.getRam() < minRam) {
            res.addIssue("❌ RAM insuficiente: tienes " + user.getRam() + " GB, se requieren " + minRam + " GB.");
        }
        if (user.getStorage() < minStorage) {
            res.addIssue("❌ Espacio insuficiente: tienes " + user.getStorage() + " GB, se requieren " + minStorage + " GB.");
        }

        StringBuilder gpuLog = new StringBuilder();
        
        Gpu userGpu = gpuRepo.findByNameContainingIgnoreCase(user.getGpuName())
                             .stream().findFirst().orElse(null);

        // 1) Primer intento: por nombre completo
        Gpu refGpu = gpuRepo.findByNameContainingIgnoreCase(reqGpuModel)
                            .stream().findFirst().orElse(null);

        // 2) Fallback: extraer Serie+Número (“RX 460”, “GTX 960”, “RTX 2070”…)
        if (refGpu == null) {
            Matcher m = Pattern.compile("(?i)(GTX|RTX|RX)\\s*(\\d{3,4})")
                               .matcher(reqGpuModel);
            if (m.find()) {
                String code = m.group(1) + " " + m.group(2);
                refGpu = gpuRepo.findByNameContainingIgnoreCase(code)
                                .stream()
                                .max(Comparator.comparing(Gpu::getG3dMark))
                                .orElse(null);
                gpuLog.append("ℹ️ Fallback GPU referencia buscada por código: ").append(code).append("\n");
            }
        }

        // 3) Loguear usuario / referencia
        if (userGpu != null) {
            gpuLog.append("GPU usuario:   ")
                  .append(userGpu.getName())
                  .append(" (G3Dmark: ").append(userGpu.getG3dMark()).append(")\n");
        } else {
            gpuLog.append("⚠️ No se encontró la GPU del usuario: ").append(user.getGpuName()).append("\n");
        }
        if (refGpu != null) {
            gpuLog.append("GPU referencia: ")
                  .append(refGpu.getName())
                  .append(" (G3Dmark: ").append(refGpu.getG3dMark()).append(")\n");
        } else {
            gpuLog.append("⚠️ No se encontró la GPU de referencia: ").append(reqGpuModel).append("\n");
        }

        // 4) Comparar marks
        if (userGpu != null && refGpu != null) {
            if (userGpu.getG3dMark() < refGpu.getG3dMark()) {
                gpuLog.append("❌ G3Dmark insuficiente: ")
                      .append(userGpu.getG3dMark()).append(" < ").append(refGpu.getG3dMark()).append("\n");
                res.addIssue("❌ GPU insuficiente: tu GPU (" + user.getGpuName() + ") no alcanza el mínimo.");
            } else {
                gpuLog.append("✅ GPU suficiente o superior: ")
                      .append(userGpu.getG3dMark()).append(" ≥ ").append(refGpu.getG3dMark()).append("\n");
            }
        }

        res.setGpuLog(gpuLog.toString());

        // … el resto igual …
        return res;
    }
    // --- Métodos auxiliares para extraer GB y modelos ---

    private int extractGb(String text, String key) {
        String pattern = switch (key.toLowerCase()) {
            case "ram"     -> "(?i)(?:RAM|Memoria|Memory)[^\\d]*(\\d+)\\s*GB";
            case "storage" -> "(?i)(?:Storage|Almacenamiento|Espacio)[^\\d]*(\\d+)\\s*GB";
            default         -> "(?i)" + key + "[^\\d]*(\\d+)\\s*GB";
        };
        Matcher m = Pattern.compile(pattern).matcher(text);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    private String extractModel(String text, String label) {
        String synonyms = "CPU".equalsIgnoreCase(label)
            ? "(?:CPU|Processor)"
            : "(?:GPU|Graphics)";
        Pattern p = Pattern.compile("(?i)" + synonyms + "\\s*[:\\-]\\s*([^,\\n(]+)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            String model = m.group(1).trim();
            if (model.toLowerCase().contains(" or ")) {
                model = model.split("(?i)\\s+or\\s+")[0];
            }
            return model;
        }
        return "";
    }
    
    private List<String> extractCpuOptions(String text) {
        // Captura trozos como "AMD Ryzen 3 1200 @ 3. GHz" o "Intel Core i5-4460 @ 3.1 GHz"
        Pattern p = Pattern.compile(
            "(?i)(Intel[^@]+@\\s*[0-9]+(?:\\.[0-9]+)?\\s*GHz|AMD[^@]+@\\s*[0-9]+(?:\\.[0-9]+)?\\s*GHz)"
        );
        Matcher m = p.matcher(text);
        List<String> opts = new ArrayList<>();
        while (m.find()) {
            opts.add(m.group().trim());
        }
        return opts;
    }

    
}
