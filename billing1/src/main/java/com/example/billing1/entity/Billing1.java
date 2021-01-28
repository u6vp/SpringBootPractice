package com.example.billing1.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "billing")
@Data
public class Billing1 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "date")
  private Date date;

  @Column(name = "billing")
  private Integer billing;

  @Column(name = "application")
  private String application;

  @Column(name = "result")
  private String result;
  
  @Column(name = "remarks")
  private String remarks;
}