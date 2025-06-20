// src/main/java/com/eep/service/CpuComparator.java
package com.eep.service;

import com.eep.model.Cpu;
import com.eep.repository.CpuRepository;

import java.util.*;
import java.util.regex.*;

public class CpuComparator {

    private final CpuRepository cpuRepo;

    public CpuComparator(CpuRepository cpuRepo) {
        this.cpuRepo = cpuRepo;
    }

   
    public boolean isUserCpuBetterOrEqual(String userCpuName, String refCpuDescription, StringBuilder log) {
    	 
        Cpu userCpu = findCpuByName(userCpuName);
        String cleanedDescription = cleanCpuDescription(refCpuDescription);
        Cpu refCpu = findCpuByDescription(cleanedDescription);

        if (userCpu == null) {
            log.append("⚠️ No se encontró la CPU del usuario: ").append(userCpuName).append("\n");
            return false;
        }

        if (refCpu == null) {
            log.append("⚠️ No se encontró la CPU de referencia: ").append(refCpuDescription)
               .append(". Se utilizará una estimación basada en la descripción.\n");
            refCpu = estimateCpuFromDescription(refCpuDescription);
            if (refCpu == null) {
                log.append("❌ No se pudo estimar una CPU válida para comparar.\n");
                return false;
            }
        }

        int userScore = getCpuScore(userCpu);
        int refScore  = getCpuScore(refCpu);
        double userGHz = extractClockFromName(userCpu.getName());
        double refGHz  = extractClockFromName(refCpuDescription);

     // 5. Log de ambas CPUs
        log.append("CPU usuario:    ").append(userCpu.getName())
           .append(" (Score: ").append(userScore)
           .append(", GHz: ").append(userGHz).append(")\n");
        log.append("CPU referencia: ").append(refCpu.getName())
           .append(" (Score: ").append(refScore)
           .append(", GHz: ").append(refGHz).append(")\n");

        // 6. Comprobamos frecuencia
        if (userGHz < refGHz) {
            log.append("❌ Frecuencia insuficiente: ")
               .append(userGHz).append("GHz < ").append(refGHz).append("GHz\n");
            return false;
        } else {
            log.append("✅ Frecuencia suficiente: ")
               .append(userGHz).append("GHz ≥ ").append(refGHz).append("GHz\n");
        }

        // 7. Comprobamos puntaje
        if (userScore < refScore) {
            log.append("❌ Puntaje insuficiente: ")
               .append(userScore).append(" < ").append(refScore).append("\n");
            return false;
        } else {
            log.append("✅ Puntaje suficiente: ")
               .append(userScore).append(" ≥ ").append(refScore).append("\n");
        }

        return true;
    }

    /** Limpia la descripción de CPU para extraer el modelo base. */
    public String cleanCpuDescription(String text) {
        if (text == null) return "";
        Matcher m = Pattern.compile("(Intel|AMD|Ryzen|Pentium|Core|Xeon|Athlon)[^\\n,\\(\\r]*", Pattern.CASE_INSENSITIVE)
                          .matcher(text);
        return m.find() ? m.group().trim() : text;
    }

    /** Busca una CPU en repositorio por nombre (contiene, ignore case). */
    private Cpu findCpuByName(String name) {
        return cpuRepo.findByNameContainingIgnoreCase(name).stream().findFirst().orElse(null);
    }

    /**
     * Intenta encontrar la mejor coincidencia por descripción (serie y número).
     */
    public Cpu findCpuByDescription(String desc) {
        if (desc == null || desc.length() < 4) return null;

        Matcher m = Pattern.compile("(i\\d|Ryzen|Pentium|Athlon|Xeon)[^\\d]*(\\d{3,4})", Pattern.CASE_INSENSITIVE)
                          .matcher(desc);
        if (m.find()) {
            String keyword = m.group(1);
            String number  = m.group(2);
            String pattern = keyword + ".*" + number;

            return cpuRepo.findAll().stream()
                .filter(cpu -> cpu.getName() != null && cpu.getName().toLowerCase().matches(".*" + pattern.toLowerCase() + ".*"))
                .max(Comparator.comparingInt(this::getCpuScore))
                .orElse(null);
        }

        // Fallback: fragmentos decrecientes
        String[] tokens = desc.split("\\s+");
        for (int i = tokens.length; i >= 2; i--) {
            String fragment = String.join(" ", Arrays.copyOf(tokens, i));
            Optional<Cpu> match = cpuRepo.findByNameContainingIgnoreCase(fragment).stream().findFirst();
            if (match.isPresent()) return match.get();
        }
        return null;
    }

    /** Estima una CPU adecuada según núcleos y frecuencia mínima. */
    public Cpu estimateCpuFromDescription(String desc) {
        int estimatedCores = extractCores(desc);
        double estimatedGHz = extractClock(desc);

        return cpuRepo.findAll().stream()
            .filter(cpu -> cpu.getCores() != null && cpu.getCores() >= estimatedCores)
            .filter(cpu -> extractClock(cpu.getName()) >= estimatedGHz)
            .filter(cpu -> cpu.getName().toLowerCase().contains("intel") || cpu.getName().toLowerCase().contains("amd"))
            .min(Comparator.comparingInt(this::getCpuScore))
            .orElse(null);
    }

    /** Calcula la puntuación usando cpuMark o fallback (núcleos, threadMark, TDP). */
    public int getCpuScore(Cpu cpu) {
        if (cpu.getCpuMark() != null) return cpu.getCpuMark();
        int score = 0;
        if (cpu.getCores() != null)     score += cpu.getCores() * 1000;
        if (cpu.getThreadMark() != null) score += cpu.getThreadMark();
        if (cpu.getTdp() != null)        score -= cpu.getTdp() * 5;
        return score;
    }

    /** Extrae núcleos de texto (dual, quad, número). */
    public int extractCores(String text) {
        Matcher m = Pattern.compile("(dual|quad|six|eight|\\d+)[ -]?core", Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) {
            String word = m.group(1).toLowerCase();
            return switch (word) {
                case "dual" -> 2;
                case "quad" -> 4;
                case "six"  -> 6;
                case "eight"-> 8;
                default -> {
                    try { yield Integer.parseInt(word); } catch (Exception e) { yield 2; }
                }
            };
        }
        return 2;
    }

    /** Extrae frecuencia en GHz de texto, acepta "3", "3." o "3.1" */
    public double extractClock(String text) {
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d*)?)\\s*GHz",
                                   Pattern.CASE_INSENSITIVE)
                           .matcher(text);
        if (m.find()) {
            String num = m.group(1);
            if (num.endsWith(".")) num = num.substring(0, num.length()-1);
            return Double.parseDouble(num);
        }
        return 0.0;
    }

    /** Fallback para extraer clock de texto estático, acepta "3", "3." o "3.1" */
    public static double extractClockFromName(String name) {
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d*)?)\\s*GHz",
                                   Pattern.CASE_INSENSITIVE)
                           .matcher(name);
        if (m.find()) {
            String num = m.group(1);
            if (num.endsWith(".")) num = num.substring(0, num.length()-1);
            return Double.parseDouble(num);
        }
        return 0.0;
    }

    /** Compara dos CPUs, usado si necesitas comparar fuera de isUserCpuBetterOrEqual. */
    public static int compare(Cpu a, Cpu b) {
        if (a == null || b == null) return 0;
        if (a.getCpuMark() != null && b.getCpuMark() != null) {
            return Integer.compare(a.getCpuMark(), b.getCpuMark());
        }
        int aScore = computeFallbackScore(a);
        int bScore = computeFallbackScore(b);
        if (aScore != bScore) {
            return Integer.compare(aScore, bScore);
        }
        double aGHz = extractClockFromName(a.getName());
        double bGHz = extractClockFromName(b.getName());
        return Double.compare(aGHz, bGHz);
    }

    /** Puntuación auxiliar estática. */
    public static int computeFallbackScore(Cpu cpu) {
        int score = 0;
        if (cpu.getThreadMark() != null) score += cpu.getThreadMark();
        if (cpu.getCores() != null)      score += cpu.getCores() * 1000;
        if (cpu.getTdp() != null)        score -= cpu.getTdp() * 5;
        return score;
    }
}
