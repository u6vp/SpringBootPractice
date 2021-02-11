package com.example.billing.common;

import java.sql.Date;
import java.util.Calendar;

public class Utils {
  public static Date convDate(Calendar cal, boolean firstdayFlag) {
    // 現在の年月を取得
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int date = 1; // 月の始め
      
    // 月初日付をセット
    cal.set(year, month, date, 0, 0, 0);
      
    //終了年月日を取得する
    if(!firstdayFlag){
        //月末日を取得する
        date = cal.getActualMaximum(Calendar.DATE);
        cal.set(year, month, date, 0, 0, 0);
    }

    return new Date(cal.getTime().getTime());
  }
}
