package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj.id, obj.date, obj.amount, obj.seller.name AS sellerName) " +
                    "FROM Sale obj " +
                    "WHERE obj.date BETWEEN :minDate AND :maxDate " +
                    "AND (:name IS NULL OR LOWER(obj.seller.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<SaleReportDTO> getSalesReport(
            LocalDate minDate,
            LocalDate maxDate,
            String name,
            Pageable pageable
    );

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name AS sellerName, " +
            "CAST(SUM(obj.amount) AS double) AS total) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :minDate AND :maxDate " +
            "GROUP BY obj.seller.name")
    List<SaleSummaryDTO> getSalesSummary(
            LocalDate minDate,
            LocalDate maxDate
    );
}
