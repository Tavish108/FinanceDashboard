package com.finance.dashboard.service;

import com.finance.dashboard.dto.DashboardSummary;
import com.finance.dashboard.dto.RecordRequest;
import com.finance.dashboard.entity.Record;
import com.finance.dashboard.entity.RecordType;
import com.finance.dashboard.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public String createRecord(RecordRequest request, String email) {
        Record record = Record.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .userEmail(email)
                .deleted(false)
                .build();

        recordRepository.save(record);
        return "Record created successfully";
    }

    public String updateRecord(Long id, RecordRequest request, String email) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (!record.getUserEmail().equals(email) || record.isDeleted()) {
            throw new RuntimeException("Record not found or unauthorized");
        }

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        recordRepository.save(record);
        return "Record updated successfully";
    }

    @Transactional
    public String deleteRecord(Long id, String email) {
        int count = recordRepository.softDeleteByIdAndUserEmail(id, email);
        if (count == 0) {
            throw new RuntimeException("Record not found or already deleted");
        }
        return "Record deleted successfully (Soft Delete)";
    }

    public DashboardSummary getSummary(String email) {
        List<Record> records = recordRepository.findByUserEmailAndDeletedFalse(email);

        double income = records.stream()
                .filter(r -> r.getType() == RecordType.INCOME)
                .mapToDouble(Record::getAmount)
                .sum();

        double expense = records.stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .mapToDouble(Record::getAmount)
                .sum();

        return new DashboardSummary(income, expense, income - expense);
    }

    public Map<String, Double> getCategoryBreakdown(String email) {
        List<Record> records = recordRepository.findByUserEmailAndDeletedFalse(email);

        return records.stream()
                .collect(Collectors.groupingBy(
                        Record::getCategory,
                        Collectors.summingDouble(Record::getAmount)
                ));
    }

    public Page<Record> getRecords(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return recordRepository.findByUserEmailAndDeletedFalse(email, pageable);
    }

    public List<Record> filterRecords(String email,
                                      String category,
                                      String type,
                                      LocalDate startDate,
                                      LocalDate endDate) {

        List<Record> records = recordRepository.findByUserEmailAndDeletedFalse(email);

        if (category != null && !category.trim().isEmpty()) {
            records = records.stream()
                    .filter(r -> r.getCategory() != null &&
                            r.getCategory().equalsIgnoreCase(category.trim()))
                    .toList();
        }

        if (type != null && !type.trim().isEmpty()) {
            records = records.stream()
                    .filter(r -> r.getType() != null &&
                            r.getType().name().equalsIgnoreCase(type.trim()))
                    .toList();
        }

        if (startDate != null && endDate != null) {
            records = records.stream()
                    .filter(r -> r.getDate() != null &&
                            !r.getDate().isBefore(startDate) &&
                            !r.getDate().isAfter(endDate))
                    .toList();
        } else if (startDate != null) {
            records = records.stream()
                    .filter(r -> r.getDate() != null && !r.getDate().isBefore(startDate))
                    .toList();
        } else if (endDate != null) {
            records = records.stream()
                    .filter(r -> r.getDate() != null && !r.getDate().isAfter(endDate))
                    .toList();
        }

        return records;
    }

    // New: Search by keyword in category or notes
    public List<Record> searchRecords(String email, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return recordRepository.findByUserEmailAndDeletedFalse(email);
        }
        return recordRepository.searchRecords(email, keyword.trim());
    }
}