package com.example.billing.repository;

import java.sql.Date;
import java.util.List;

import com.example.billing.entity.Billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer>{
  @Query(value = "select * from (select distinct on (application) * from billing) distT order by distT.id", nativeQuery = true)
  List<Billing> findAppList();
  List<Billing> findByApplicationOrderByDateAsc(String application);
  List<Billing> findByDateBetween(Date from, Date to);
}
