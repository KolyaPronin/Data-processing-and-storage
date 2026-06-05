package com.example.demo.checkin.service;

import com.example.demo.checkin.dto.BoardingPassDTO;
import com.example.demo.checkin.dto.CheckInRequestDTO;

public interface CheckInService {

    BoardingPassDTO checkIn(CheckInRequestDTO request);
}
