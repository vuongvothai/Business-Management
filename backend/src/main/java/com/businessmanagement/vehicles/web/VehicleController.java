package com.businessmanagement.vehicles.web;

import com.businessmanagement.vehicles.dto.VehicleRequest;
import com.businessmanagement.vehicles.dto.VehicleResponse;
import com.businessmanagement.vehicles.entity.Vehicle;
import com.businessmanagement.vehicles.repository.VehicleRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleRepository repository;
    public VehicleController(VehicleRepository repository) { this.repository = repository; }

    @GetMapping
    public List<VehicleResponse> list(@RequestParam(defaultValue = "true") boolean active) {
        return (active ? repository.findByActiveTrueOrderByLicensePlateAsc() : repository.findAll())
                .stream().map(VehicleResponse::from).toList();
    }
    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable Long id) { return VehicleResponse.from(find(id)); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody VehicleRequest request) {
        return save(new Vehicle(request.licensePlate()));
    }
    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = find(id); vehicle.updateLicensePlate(request.licensePlate()); return save(vehicle);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { Vehicle vehicle = find(id); vehicle.deactivate(); repository.save(vehicle); }
    private VehicleResponse save(Vehicle vehicle) {
        try { return VehicleResponse.from(repository.save(vehicle)); }
        catch (DataIntegrityViolationException ex) { throw new ResponseStatusException(HttpStatus.CONFLICT, "License plate already exists"); }
    }
    private Vehicle find(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found: " + id)); }
}
