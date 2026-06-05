package com.example.demo.schedule.controller;

import com.example.demo.schedule.dto.ScheduleDTO;
import com.example.demo.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airports")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{iata}/schedule/inbound")
    public ResponseEntity<List<ScheduleDTO>> getInboundSchedule(@PathVariable("iata") String iata) {
        List<ScheduleDTO> schedule = scheduleService.getArrivingSchedule(iata);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{iata}/schedule/outbound")
    public ResponseEntity<List<ScheduleDTO>> getOutboundSchedule(@PathVariable("iata") String iata) {
        List<ScheduleDTO> schedule = scheduleService.getDepartingSchedule(iata);
        return ResponseEntity.ok(schedule);
    }
}
