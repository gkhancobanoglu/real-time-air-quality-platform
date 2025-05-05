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
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String param,
            @RequestParam double value) {
        return runScript("cmd.exe", "/c", "C:\\Users\\Gokhan\\Desktop\\TASKLER\\kartaca\\real-time-air-quality-platform\\backend\\scripts\\manual-input.bat",
                String.valueOf(lat), String.valueOf(lon), param, String.valueOf(value));
    }

    @PostMapping("/autotest")
    public ResponseEntity<String> runAutoTest(
            @RequestParam(defaultValue = "30") int duration,
            @RequestParam(defaultValue = "2") int rate,
            @RequestParam(defaultValue = "30") int anomalyChance) {

        String command = String.format("C:\\Users\\Gokhan\\Desktop\\TASKLER\\kartaca\\real-time-air-quality-platform\\backend\\scripts\\auto-test.bat %d %d %d", duration, rate, anomalyChance);
        runScriptAsync("cmd.exe", "/c", command); // asenkron çağır
        return ResponseEntity.ok("Auto-test started.");
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
