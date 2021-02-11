package com.example.billing.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.billing.entity.Billing;
import com.example.billing.form.BillingData;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class BillingService {

  // 任意の課金履歴表から課金額の総和を計算
  public int sumBilling(List<Billing> billingList) {
    int sumBilling = 0;
    for (Billing billing : billingList) {
      sumBilling += billing.getBilling();
    }
    return sumBilling;
  }
  
  // 日付書式のチェック（dateがyyyy/MM/ddであるかどうか）
  public boolean isValid(BillingData billingData, BindingResult result) {
    boolean ans = true;
    String date = billingData.getDate();
    DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    if (!date.equals("")) {
      LocalDate billingDate = null;
      try {
        // date の入力書式が「yyyy/mm/dd」でない場合（テキストが解析できない場合）,
        // LocalDate型に変換できず，DateTimeParseExceptionが返される
        billingDate = LocalDate.parse(date, dtFormat);

        if (billingDate != null) {
          ans = true;
        }
      } catch (Exception e) {
        // フィールドエラー
        FieldError fieldError = new FieldError(
                                      result.getObjectName(),
                                      "date",
                                      "課金日はyyyy/mm/dd形式で入力してください");
        result.addError(fieldError);
        ans = false;
      }
    }
    return ans;
  }
}
