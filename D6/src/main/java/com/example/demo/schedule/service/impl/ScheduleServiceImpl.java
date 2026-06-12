package com.example.demo.schedule.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.schedule.dto.ScheduleDTO;
import com.example.demo.schedule.entity.ScheduledFlight;
import com.example.demo.schedule.repository.ScheduledFlightRepository;
import com.example.demo.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduledFlightRepository scheduledFlightRepository;

    @Override
    public List<ScheduleDTO> getArrivingSchedule(String iata) {
        if (iata == null || iata.isBlank()) {
            throw new ResourceNotFoundException("IATA-код аэропорта не может быть пустым");
        }
        List<ScheduledFlight> flights = scheduledFlightRepository.findByDestinationIata(iata);
        if (flights.isEmpty()) {
            throw new ResourceNotFoundException("Расписание прилетающих рейсов для аэропорта '" + iata + "' не найдено");
        }
        return flights.stream()
                .map(this::mapToScheduleDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getDepartingSchedule(String iata) {
        if (iata == null || iata.isBlank()) {
            throw new ResourceNotFoundException("IATA-код аэропорта не может быть пустым");
        }
        List<ScheduledFlight> flights = scheduledFlightRepository.findByOriginIata(iata);
        if (flights.isEmpty()) {
            throw new ResourceNotFoundException("Расписание вылетающих рейсов для аэропорта '" + iata + "' не найдено");
        }
        return flights.stream()
                .map(this::mapToScheduleDto)
                .collect(Collectors.toList());
    }

    private ScheduleDTO mapToScheduleDto(ScheduledFlight flight) {
        return new ScheduleDTO(
                flight.getFlightNo(),
                flight.getOriginIata(),
                flight.getDestinationIata(),
                flight.getOriginName(),
                flight.getDestinationName(),
                flight.getArrivalTime(),
                flight.getDepartureTime(),
                flight.getOperatedBy()
        );
    }
}