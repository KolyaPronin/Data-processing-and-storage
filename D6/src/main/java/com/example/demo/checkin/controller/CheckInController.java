package com.example.demo.checkin.controller;

import com.example.demo.checkin.dto.BoardingPassDTO;
import com.example.demo.checkin.dto.CheckInRequestDTO;
import com.example.demo.checkin.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Регистрация")
@RestController
@RequestMapping("check-in")
@RequiredArgsConstructor
@Validated
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping
    @Operation(summary = "Регистрация на рейс")
    public ResponseEntity<BoardingPassDTO> checkIn(@Valid @RequestBody CheckInRequestDTO request) {
        BoardingPassDTO boardingPass = checkInService.checkIn(request);
        return ResponseEntity.ok(boardingPass);
    }
}