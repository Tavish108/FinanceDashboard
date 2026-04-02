package com.finance.dashboard.repository;

import com.finance.dashboard.entity.Record;
import com.finance.dashboard.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByUserEmailAndDeletedFalse(String email);

    Page<Record> findByUserEmailAndDeletedFalse(String email, Pageable pageable);

    List<Record> findByUserEmailAndTypeAndDeletedFalse(String email, RecordType type);

    List<Record> findByUserEmailAndDateBetweenAndDeletedFalse(String email, LocalDate start, LocalDate end);

    List<Record> findByUserEmailAndCategoryAndDeletedFalse(String email, String category);

    // Search by category or notes
    @Query("SELECT r FROM Record r WHERE r.userEmail = :email " +
            "AND r.deleted = false " +
            "AND (LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.notes) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Record> searchRecords(@Param("email") String email, @Param("keyword") String keyword);

    // Soft Delete
    @Modifying
    @Query("UPDATE Record r SET r.deleted = true WHERE r.id = :id AND r.userEmail = :email AND r.deleted = false")
    int softDeleteByIdAndUserEmail(@Param("id") Long id, @Param("email") String email);
}