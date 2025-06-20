// src/main/java/com/eep/service/GpuComparator.java
package com.eep.service;

import com.eep.model.Gpu;
import com.eep.repository.GpuRepository;

import java.util.*;
import java.util.regex.*;

public class GpuComparator {

    private final GpuRepository gpuRepo;

    public GpuComparator(GpuRepository gpuRepo) {
        this.gpuRepo = gpuRepo;
    }

    /**
     * Compara la GPU del usuario contra la descripción de GPU de referencia.
     * Registra en log los detalles de puntuación.
     */
    public boolean isUserGpuBetterOrEqual(String userGpuName, String refGpuDescription, StringBuilder log) {
        Gpu userGpu = findGpuByName(userGpuName);
        Gpu refGpu  = findGpuByDescription(refGpuDescription);

        if (userGpu == null) {
            log.append("⚠️ No se encontró la GPU del usuario: ").append(userGpuName).append("\n");
            return false;
        }
        if (refGpu == null) {
            log.append("⚠️ No se encontró la GPU de referencia: ").append(refGpuDescription).append("\n");
            return false;
        }

        int userScore = getGpuScore(userGpu);
        int refScore  = getGpuScore(refGpu);

        log.append("GPU usuario:   ").append(userGpu.getName())
           .append(" (G3DMark: ").append(userScore).append(")\n");
        log.append("GPU referencia: ").append(refGpu.getName())
           .append(" (G3DMark: ").append(refScore).append(")\n");

        if (userScore < refScore) {
            log.append("❌ Puntuación insuficiente: ")
               .append(userScore).append(" < ").append(refScore).append("\n");
            return false;
        }
        return true;
    }

    /** Busca una GPU en repositorio por nombre (contiene, ignore case). */
    private Gpu findGpuByName(String name) {
        return gpuRepo.findByNameContainingIgnoreCase(name)
                      .stream().findFirst().orElse(null);
    }

    /**
     * Intenta encontrar la mejor coincidencia por descripción:
     * extrae palabras clave y números para emparejar.
     */
    private Gpu findGpuByDescription(String desc) {
        if (desc == null || desc.length() < 3) return null;

        // Extrae el fabricante + serie eg. "RTX 2070", "RX 580"
        Matcher m = Pattern.compile("(RX|GTX|RTX|Vega)\\s*(\\d{3,4})", Pattern.CASE_INSENSITIVE)
                          .matcher(desc);
        if (m.find()) {
            String keyword = m.group(1);
            String number  = m.group(2);
            String pattern = keyword + ".*" + number;

            return gpuRepo.findAll().stream()
                .filter(gpu -> gpu.getName() != null
                             && gpu.getName().toLowerCase().matches(".*" + pattern.toLowerCase() + ".*"))
                .max(Comparator.comparingInt(this::getGpuScore))
                .orElse(null);
        }

        // Fallback: fragmentos decrecientes
        String[] tokens = desc.split("\\s+");
        for (int i = tokens.length; i >= 1; i--) {
            String fragment = String.join(" ", Arrays.copyOf(tokens, i));
            Optional<Gpu> match = gpuRepo.findByNameContainingIgnoreCase(fragment).stream().findFirst();
            if (match.isPresent()) return match.get();
        }
        return null;
    }

    /** Obtiene la puntuación de una GPU (G3DMark). */
    public int getGpuScore(Gpu gpu) {
        return gpu.getG3dMark() != null ? gpu.getG3dMark() : 0;
    }
}
