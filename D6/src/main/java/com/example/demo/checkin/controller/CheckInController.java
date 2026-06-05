package com.example.demo.checkin.controller;

import com.example.demo.checkin.dto.BoardingPassDTO;
import com.example.demo.checkin.dto.CheckInRequestDTO;
import com.example.demo.checkin.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("check-in")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<BoardingPassDTO> checkIn(@RequestBody CheckInRequestDTO request) {
        BoardingPassDTO boardingPass = checkInService.checkIn(request);
        return ResponseEntity.ok(boardingPass);
    }
}
