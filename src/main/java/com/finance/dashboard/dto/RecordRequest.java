package com.finance.dashboard.dto;


import com.finance.dashboard.entity.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Type is required")
    private RecordType type;

    private String category;
    private LocalDate date;
    private String notes;
}