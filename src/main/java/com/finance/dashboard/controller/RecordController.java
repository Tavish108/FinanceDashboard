package com.finance.dashboard.controller;

import com.finance.dashboard.dto.ApiResponse;
import com.finance.dashboard.dto.DashboardSummary;
import com.finance.dashboard.dto.RecordRequest;
import com.finance.dashboard.entity.Record;
import com.finance.dashboard.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "02. Records", description = "Financial record management")
@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    // 2.1 Create Record
    @Operation(summary = "2.1 Create Record - ADMIN ONLY", operationId = "createRecord")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> createRecord(@Valid @RequestBody RecordRequest request,
                                            Authentication auth) {
        return new ApiResponse<>(
                true,
                recordService.createRecord(request, auth.getName()),
                null
        );
    }

    // 2.2 Get All Records with Pagination
    @Operation(summary = "2.2 Get All Records with Pagination", operationId = "getRecords")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ApiResponse<Page<Record>> getRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {

        return new ApiResponse<>(
                true,
                "Records fetched successfully",
                recordService.getRecords(auth.getName(), page, size)
        );
    }

    // 2.3 Filter Records
    @Operation(summary = "2.3 Filter Records", operationId = "filterRecords")
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ApiResponse<List<Record>> filterRecords(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication auth) {

        return new ApiResponse<>(
                true,
                "Filtered records returned successfully",
                recordService.filterRecords(auth.getName(), category, type, startDate, endDate)
        );
    }

    // 2.4 Get Dashboard Summary
    @Operation(summary = "2.4 Get Dashboard Summary", operationId = "getSummary")
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ApiResponse<DashboardSummary> getSummary(Authentication auth) {
        return new ApiResponse<>(
                true,
                "Summary fetched successfully",
                recordService.getSummary(auth.getName())
        );
    }

    // 2.5 Category Breakdown
    @Operation(summary = "2.5 Category Breakdown", operationId = "getCategoryBreakdown")
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ApiResponse<Map<String, Double>> getCategoryBreakdown(Authentication auth) {
        return new ApiResponse<>(
                true,
                "Category breakdown fetched successfully",
                recordService.getCategoryBreakdown(auth.getName())
        );
    }

    // 2.6 Update Record
    @Operation(summary = "2.6 Update Record - ADMIN ONLY", operationId = "updateRecord")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateRecord(@PathVariable Long id,
                                            @Valid @RequestBody RecordRequest request,
                                            Authentication auth) {
        return new ApiResponse<>(
                true,
                recordService.updateRecord(id, request, auth.getName()),
                null
        );
    }

    // 2.7 Delete Record
    @Operation(summary = "2.7 Delete Record - ADMIN ONLY (Soft Delete)", operationId = "deleteRecord")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteRecord(@PathVariable Long id, Authentication auth) {
        return new ApiResponse<>(
                true,
                recordService.deleteRecord(id, auth.getName()),
                null
        );
    }

    // 2.8 Search Records
    @Operation(summary = "2.8 Search Records by Keyword", operationId = "searchRecords")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ApiResponse<List<Record>> searchRecords(
            @RequestParam String keyword,
            Authentication auth) {

        return new ApiResponse<>(
                true,
                "Search results",
                recordService.searchRecords(auth.getName(), keyword)
        );
    }
}