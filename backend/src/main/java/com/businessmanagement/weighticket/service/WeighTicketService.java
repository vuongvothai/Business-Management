package com.businessmanagement.weighticket.service;

import com.businessmanagement.weighticket.dto.WeighTicketRequest;
import com.businessmanagement.weighticket.dto.WeighTicketResponse;
import com.businessmanagement.weighticket.entity.WeighTicket;
import com.businessmanagement.weighticket.entity.WeighTicketLine;
import com.businessmanagement.weighticket.repository.WeighTicketLineRepository;
import com.businessmanagement.weighticket.repository.WeighTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeighTicketService {

    private final WeighTicketRepository ticketRepository;
    private final WeighTicketLineRepository lineRepository;

    public WeighTicketService(WeighTicketRepository ticketRepository,
                              WeighTicketLineRepository lineRepository) {
        this.ticketRepository = ticketRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public WeighTicketResponse create(WeighTicketRequest req) {
        WeighTicket t = new WeighTicket();
            t.setCode(generateCode());
        t.setDirection(req.direction());
        t.setWeighedAt(req.weighedAt());
        t.setWarehouseId(req.warehouseId());
        t.setVehicleId(req.vehicleId());
        t.setTruckLicensePlate(req.truckLicensePlate());
        t.setMoisturePct(req.moisturePct());
        t.setImpurityPct(req.impurityPct());
        if (req.moistureStd() != null) t.setMoistureStd(req.moistureStd());
        if (req.impurityStd() != null) t.setImpurityStd(req.impurityStd());
        t.setSampleTestResult(req.sampleTestResult());
        t.setPhotoUrl(req.photoUrl());
        t.setNote(req.note());
        t.setCreatedBy(req.createdBy());

        int totalBags = 0;
        BigDecimal gross = BigDecimal.ZERO;
        BigDecimal tare = BigDecimal.ZERO;
        List<WeighTicketLine> lines = new ArrayList<>();
        if (req.lines() != null) {
            for (WeighTicketRequest.LineRequest lr : req.lines()) {
                WeighTicketLine line = new WeighTicketLine();
                line.setLineNo(lr.lineNo());
                line.setWeightKg(lr.weightKg());
                line.setBagCount(lr.bagCount());
                line.setBagType(lr.bagType());
                line.setTarePerBagKg(lr.tarePerBagKg());
                line.setCreatedBy(req.createdBy());
                lines.add(line);
                totalBags += lr.bagCount();
                gross = gross.add(lr.weightKg());
                if (lr.tarePerBagKg() != null) {
                    tare = tare.add(lr.tarePerBagKg().multiply(BigDecimal.valueOf(lr.bagCount())));
                }
            }
        }
        t.setTotalBags(totalBags);
        t.setGrossWeightKg(gross);
        t.setTareWeightKg(tare);
        t.setNetWeightKg(gross.subtract(tare));
        t.setStandardWeightKg(StandardWeightCalculator.calculate(
                t.getNetWeightKg(), t.getMoisturePct(), t.getImpurityPct(),
                t.getMoistureStd(), t.getImpurityStd()));

        WeighTicket saved = ticketRepository.save(t);
        for (WeighTicketLine l : lines) {
            l.setWeighTicketId(saved.getWeighTicketId());
            lineRepository.save(l);
        }
        return get(saved.getWeighTicketId());
    }

    public WeighTicketResponse get(Long id) {
        WeighTicket t = ticketRepository.findById(id).orElseThrow();
        List<WeighTicketResponse.LineResponse> lines = lineRepository
                .findByWeighTicketIdOrderByLineNo(id).stream()
                .map(l -> new WeighTicketResponse.LineResponse(
                        l.getWeighLineId(), l.getLineNo(), l.getWeightKg(),
                        l.getBagCount(), l.getBagType(), l.getTarePerBagKg()))
                .toList();
        return WeighTicketResponse.from(t, lines);
    }

    public List<WeighTicketResponse> list(String status, String direction) {
        List<WeighTicket> all;
        if (status != null && !status.isBlank()) {
            all = ticketRepository.findByStatusOrderByWeighedAtDesc(status);
        } else if (direction != null && !direction.isBlank()) {
            all = ticketRepository.findByDirectionOrderByWeighedAtDesc(direction);
        } else {
            all = ticketRepository.findAll();
        }
        return all.stream().map(t -> WeighTicketResponse.from(t, List.of())).toList();
    }

    @Transactional
    public WeighTicketResponse confirm(Long id) {
        WeighTicket t = ticketRepository.findById(id).orElseThrow();
        t.setStatus("CONFIRMED");
        return get(t.getWeighTicketId());
    }

        private String generateCode() {
            String prefix = "PC";
            long count = ticketRepository.count() + 1;
            return prefix + String.format("%08d", count);
        }
}