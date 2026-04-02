package com.finance.dashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "records")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private RecordType type;

    private String category;

    private LocalDate date;

    private String notes;

    private String userEmail;

    // Soft Delete Field
    @Column(nullable = false)
    private boolean deleted = false;

    public Long getId() {
        return this.id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public RecordType getType() {
        return this.type;
    }

    public String getCategory() {
        return this.category;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
}