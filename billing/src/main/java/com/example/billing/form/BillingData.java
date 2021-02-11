package com.example.billing.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.example.billing.entity.Billing;

import lombok.Data;

@Data
public class BillingData {
  private Integer id;
  private String date;
  @Min(value = 0, message = "0以上の値を入力してください")
  private Integer billing;
  @NotBlank(message = "アプリ名を入力してください")
  private String application;
  private String result;
  private String remarks;

  // 入力データからEntityを生成して返す
  public Billing toEntity() {
    Billing billing = new Billing();
    billing.setId(id);
    billing.setBilling(this.billing);
    billing.setApplication(application);
    billing.setResult(result);
    billing.setRemarks(remarks);

    // 課金日はyyyy/MM/ddの書式でない場合，nullが返される
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
    long ms;
    try {
      ms = sdFormat.parse(date).getTime();
      billing.setDate(new Date(ms));
    } catch (ParseException e) {
      billing.setDate(null);
    }

    return billing;
  }
}
