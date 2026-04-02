package com.finance.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummary {

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
}