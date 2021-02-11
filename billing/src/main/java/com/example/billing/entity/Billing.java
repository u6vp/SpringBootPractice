package com.example.billing.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.billing.form.BillingData;

import lombok.Data;

@Entity
@Table(name = "billing")
@Data
public class Billing {
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

   // Entityから入力データを生成して返す
   public BillingData toBillingData() {
    BillingData billingData = new BillingData();
    billingData.setId(id);
    billingData.setBilling(this.billing);
    billingData.setApplication(application);
    billingData.setResult(result);
    billingData.setRemarks(remarks);
    // 日付をLocalDateに変換
    LocalDate localDate = date.toLocalDate();
    // 書式を指定
    DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // 指定の書式に日付データを渡す
    String formatDate = datetimeformatter.format(localDate);
    billingData.setDate(formatDate);

    return billingData;
  }
}
