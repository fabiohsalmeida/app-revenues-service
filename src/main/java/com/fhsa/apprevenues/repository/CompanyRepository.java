package com.fhsa.apprevenues.repository;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

}
