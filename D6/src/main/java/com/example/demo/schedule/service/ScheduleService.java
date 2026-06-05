package com.example.demo.schedule.service;

import com.example.demo.schedule.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    List<ScheduleDTO> getArrivingSchedule(String iata);

    List<ScheduleDTO> getDepartingSchedule(String iata);
}
