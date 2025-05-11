package com.cobanoglu.scriptrunnerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/scripts")
@Tag(name = "Script Runner API", description = "Triggers shell scripts from the backend")
public class ScriptController {

    @Operation(
            summary = "Trigger manual data injection script",
            description = "Runs the `manual-input.sh` script with provided air pollutant values",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Script executed successfully"),
                    @ApiResponse(responseCode = "500", description = "Script execution failed", content = @Content)
            }
    )
    @PostMapping("/manual")
    public ResponseEntity<String> runManualInput(
            @Parameter(description = "Latitude") @RequestParam("lat") double lat,
            @Parameter(description = "Longitude") @RequestParam("lon") double lon,
            @Parameter(description = "CO value") @RequestParam("co") double co,
            @Parameter(description = "NO value") @RequestParam("no") double no,
            @Parameter(description = "NO₂ value") @RequestParam("no2") double no2,
            @Parameter(description = "O₃ value") @RequestParam("o3") double o3,
            @Parameter(description = "SO₂ value") @RequestParam("so2") double so2,
            @Parameter(description = "PM2.5 value") @RequestParam("pm25") double pm25,
            @Parameter(description = "PM10 value") @RequestParam("pm10") double pm10,
            @Parameter(description = "NH₃ value") @RequestParam("nh3") double nh3) {

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

    @Operation(
            summary = "Trigger automatic test simulation script",
            description = "Runs the `auto-test.sh` script with the specified parameters to generate random air quality data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto-test started"),
                    @ApiResponse(responseCode = "500", description = "Auto-test execution failed", content = @Content)
            }
    )
    @PostMapping("/autotest")
    public ResponseEntity<String> runAutoTest(
            @Parameter(description = "Test duration in seconds") @RequestParam(name = "duration", defaultValue = "30") int duration,
            @Parameter(description = "Request rate per second") @RequestParam(name = "rate", defaultValue = "2") int rate,
            @Parameter(description = "Anomaly probability percentage") @RequestParam(name = "anomalyChance", defaultValue = "30") int anomalyChance) {

        return runScript(
                "/bin/bash", "/scripts/auto-test.sh",
                String.valueOf(duration),
                String.valueOf(rate),
                String.valueOf(anomalyChance)
        );
    }

//    @Async
//    public void runScriptAsync(String... command) {
//        try {
//            ProcessBuilder builder = new ProcessBuilder(command);
//            builder.redirectErrorStream(true);
//            Process process = builder.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("[auto-test] " + line);
//            }
//
//            process.waitFor();
//        } catch (Exception e) {
//            System.err.println("Auto-test error: " + e.getMessage());
//        }
//    }

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
