package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	private static final LocalDate TODAY = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	private static final LocalDate ONE_YEAR_AGO = TODAY.minusYears(1L);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<SaleReportDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate parsedMinDate = parseOrDefault(minDate, ONE_YEAR_AGO);
		LocalDate parsedMaxDate = parseOrDefault(maxDate, TODAY);
		return repository.getSalesReport(parsedMinDate, parsedMaxDate, name, pageable);
	}

	@Transactional(readOnly = true)
	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate parsedMinDate = parseOrDefault(minDate, ONE_YEAR_AGO);
		LocalDate parsedMaxDate = parseOrDefault(maxDate, TODAY);
		return repository.getSalesSummary(parsedMinDate, parsedMaxDate);
	}

	private LocalDate parseOrDefault(String date, LocalDate defaultDate) {
		return (date == null || date.isEmpty()) ? defaultDate : LocalDate.parse(date, DATE_FORMATTER);
	}
}
