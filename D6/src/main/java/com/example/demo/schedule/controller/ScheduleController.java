package com.example.demo.schedule.controller;

import com.example.demo.schedule.dto.ScheduleDTO;
import com.example.demo.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Расписание")
@RestController
@RequestMapping("/airports")
@RequiredArgsConstructor
@Validated
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{iata}/schedule/inbound")
    @Operation(summary = "Расписание прилетающих рейсов (прибытие)")
    public ResponseEntity<List<ScheduleDTO>> getInboundSchedule(
            @PathVariable("iata")
            @NotBlank
            @Size(min = 3, max = 3)
            @Parameter(description = "IATA-код аэропорта", example = "SVO") String iata) {
        List<ScheduleDTO> schedule = scheduleService.getArrivingSchedule(iata);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{iata}/schedule/outbound")
    @Operation(summary = "Расписание вылетающих рейсов (вылет)")
    public ResponseEntity<List<ScheduleDTO>> getOutboundSchedule(
            @PathVariable("iata")
            @NotBlank
            @Size(min = 3, max = 3)
            @Parameter(description = "IATA-код аэропорта", example = "SVO") String iata) {
        List<ScheduleDTO> schedule = scheduleService.getDepartingSchedule(iata);
        return ResponseEntity.ok(schedule);
    }
}