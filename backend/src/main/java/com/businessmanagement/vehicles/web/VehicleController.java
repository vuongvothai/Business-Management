package com.businessmanagement.vehicles.web;

import com.businessmanagement.vehicles.dto.VehicleRequest;
import com.businessmanagement.vehicles.dto.VehicleResponse;
import com.businessmanagement.vehicles.entity.Vehicle;
import com.businessmanagement.vehicles.repository.VehicleRepository;
import com.businessmanagement.audit.service.AuditLogService;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.repository.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuditLogService auditLogService;
    private final AppUserRepository users;
    public VehicleController(VehicleRepository repository, AuditLogService auditLogService, AppUserRepository users) {
        this.repository = repository; this.auditLogService = auditLogService; this.users = users;
    }

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
        Vehicle vehicle = saveEntity(new Vehicle(request.licensePlate()));
        auditLogService.record(currentUser(), "CREATE", "VEHICLE", vehicle.getId(), "Vehicle created");
        return VehicleResponse.from(vehicle);
    }
    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = find(id);
        vehicle.updateLicensePlate(request.licensePlate());
        Vehicle saved = saveEntity(vehicle);
        auditLogService.record(currentUser(), "UPDATE", "VEHICLE", id, "Vehicle updated");
        return VehicleResponse.from(saved);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { Vehicle vehicle = find(id); vehicle.deactivate(); repository.save(vehicle); auditLogService.record(currentUser(), "HIDE", "VEHICLE", id, "Vehicle hidden"); }
    private Vehicle saveEntity(Vehicle vehicle) {
        try { return repository.save(vehicle); }
        catch (DataIntegrityViolationException ex) { throw new ResponseStatusException(HttpStatus.CONFLICT, "License plate already exists"); }
    }
    private AppUser currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
    private Vehicle find(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found: " + id)); }
}
