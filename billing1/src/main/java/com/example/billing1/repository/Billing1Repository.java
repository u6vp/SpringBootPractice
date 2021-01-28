package com.example.billing1.repository;

import com.example.billing1.entity.Billing1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Billing1Repository extends JpaRepository<Billing1, Integer>{
}
