package com.bangtalboys.BTS_Backend.city.service;

import com.bangtalboys.BTS_Backend.city.domain.City;
import com.bangtalboys.BTS_Backend.city.dto.CityResponse;
import com.bangtalboys.BTS_Backend.city.repository.CityRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<CityResponse> getAllCity() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(CityResponse::new).collect(Collectors.toList());
    }

    public CityResponse getOneCity(Long id) {
        Optional<City> city = cityRepository.findById(id);
        return city.map(CityResponse::new).orElseThrow(NotFoundException::new);
    }
}
