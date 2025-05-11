package com.cobanoglu.scriptrunnerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    @PostMapping("/manual")
    public ResponseEntity<String> runManualInput(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("co") double co,
            @RequestParam("no") double no,
            @RequestParam("no2") double no2,
            @RequestParam("o3") double o3,
            @RequestParam("so2") double so2,
            @RequestParam("pm25") double pm25,
            @RequestParam("pm10") double pm10,
            @RequestParam("nh3") double nh3) {

        return runScript(
                "/bin/sh", "/scripts/manual-input.sh",
                String.valueOf(lat),
                String.valueOf(lon),
                String.valueOf(co),
                String.valueOf(no),
                String.valueOf(no2),
                String.valueOf(o3),
                String.valueOf(so2),
                String.valueOf(pm25),
                String.valueOf(pm10),
                String.valueOf(nh3)
        );
    }

    @PostMapping("/autotest")
    public ResponseEntity<String> runAutoTest(
            @RequestParam(name = "duration", defaultValue = "30") int duration,
            @RequestParam(name = "rate", defaultValue = "2") int rate,
            @RequestParam(name = "anomalyChance", defaultValue = "30") int anomalyChance) {

        return runScript(
                "/bin/bash", "/scripts/auto-test.sh",
                String.valueOf(duration),
                String.valueOf(rate),
                String.valueOf(anomalyChance)
        );
    }

    @Async
    public void runScriptAsync(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[auto-test] " + line);
            }

            process.waitFor();
        } catch (Exception e) {
            System.err.println("Auto-test error: " + e.getMessage());
        }
    }

    private ResponseEntity<String> runScript(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            return ResponseEntity.ok("Exit code: " + exitCode + "\n" + output);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
