package com.example.billing.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.billing.common.Utils;
import com.example.billing.entity.Billing;
import com.example.billing.form.BillingData;
import com.example.billing.repository.BillingRepository;
import com.example.billing.service.BillingService;

import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class BillingController {
  private final BillingRepository billingRepository;
  private final BillingService billingService;
  private final HttpSession session;

  // 課金履歴の存在するアプリケーションの一覧表示
  @GetMapping("/applicationhistory")
  public ModelAndView showAppList(ModelAndView mv) {
    mv.setViewName("applicationHistory");
    // 課金履歴にあるアプリケーションの種類に関するリスト
    List<Billing> appList = billingRepository.findAppList();
    mv.addObject("appList", appList);
    // 全てのレコードを抽出し，billing列の総和を計算（累計総課金額の計算）
    List<Billing> billingList = billingRepository.findAll();
    int sumBilling = billingService.sumBilling(billingList);
    mv.addObject("sumBilling", sumBilling);
    // 今月の課金額を計算
    Calendar today = Calendar.getInstance();
    Date firstDayOfThisMonth = Utils.convDate(today, true);
    Date lastDayOfThisMonth = Utils.convDate(today, false);
    List<Billing> thisMonthList = billingRepository.findByDateBetween(firstDayOfThisMonth, lastDayOfThisMonth);
    int sumOfThisMonth = billingService.sumBilling(thisMonthList);
    mv.addObject("sumOfThisMonth", sumOfThisMonth);
    // 先月の課金額を計算
    Calendar todayLastMonth = Calendar.getInstance();
    todayLastMonth.add(Calendar.MONTH, -1);
    Date firstDayOfLastMonth = Utils.convDate(todayLastMonth, true);
    Date lastDayOfLastMonth = Utils.convDate(todayLastMonth, false);
    List<Billing> lastMonthList = billingRepository.findByDateBetween(firstDayOfLastMonth, lastDayOfLastMonth);
    int sumOfLastMonth = billingService.sumBilling(lastMonthList);
    // 前月比を計算
    double mom = 0;
    if (sumOfLastMonth != 0) {
      mom = (double)(sumOfThisMonth) / sumOfLastMonth * 100;
    }
    System.out.print(mom);
    mv.addObject("mom", sumOfLastMonth == 0 ? "-" : Math.round(mom));

    return mv;
  }

  // アプリ毎の課金履歴を表示
  @GetMapping("/billinghistory/{application}")
  public ModelAndView billingByApp(@PathVariable(name = "application") String application, ModelAndView mv) {
    mv.setViewName("billingHistory");
    List<Billing> billingList = billingRepository.findByApplicationOrderByDateAsc(application);
    mv.addObject("billingList", billingList);
    // 累計課金額も表示する
    int sumBilling = billingService.sumBilling(billingList);
    mv.addObject("sumBilling", sumBilling);
    return mv;
  }

  // 新規に課金情報を入力するフォームを表示
  @GetMapping("/input")
  public ModelAndView createBilling(ModelAndView mv) {
    mv.setViewName("input");
    // 課金日の日付として自動的に入力時の日付を与える
    LocalDate today = LocalDate.now();
    // 書式を指定
    DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // 指定の書式に日付データを渡す
    String formatToday = datetimeformatter.format(today);
    BillingData billingData = new BillingData();
    billingData.setDate(formatToday);
    mv.addObject("billingData", billingData);
    // 課金履歴にあるアプリケーションの種類に関するリスト
    List<Billing> appList = billingRepository.findAppList();
    mv.addObject("appList", appList);
    // 新規に追加するセッションに設定
    session.setAttribute("mode", "create");
    return mv;
  }

  // 課金履歴の編集画面を表示
  @GetMapping("/edit/{id}")
  public ModelAndView billingByid(@PathVariable(name = "id") int id, ModelAndView mv) {
    mv.setViewName("edit");
    Billing billing = billingRepository.findById(id).get();
    // Billing型からBillingData型に変換（日付の書式はyyyy/MM/dd）
    BillingData billingData = billing.toBillingData();
    mv.addObject("billingData", billingData);
    // 課金履歴にあるアプリケーションの種類に関するリスト
    List<Billing> appList = billingRepository.findAppList();
    mv.addObject("appList", appList);
    // 既存の履歴を更新するセッションに設定
    session.setAttribute("mode", "update");
    return mv;
  }

  // 課金履歴を削除
  @GetMapping("/delete/{id}")
  public String deleteBilling(@PathVariable(name = "id") int id) throws UnsupportedEncodingException {
    Billing billing = billingRepository.findById(id).get();
    String application = billing.getApplication();
    billingRepository.deleteById(id);
    // アプリケーション名が日本語であることを考慮して，URLエンコードを行う
    String applicationEncode = URLEncoder.encode(application, "UTF-8");
    return "redirect:/billinghistory/" + applicationEncode;
  }

  // 入力画面または編集画面で入力したデータを課金履歴に追加・更新 (旧ver)
  // @PostMapping("/billing/{process}")
  // public ModelAndView updateBilling(@PathVariable(name = "process") String process, 
  //                             @ModelAttribute @Validated BillingData billingData,
  //                             BindingResult result, ModelAndView mv) {
  //   // エラーチェック
  //   boolean isValid = billingService.isValid(billingData, result);
  //   if (!result.hasErrors() && isValid) {
  //     // エラーなし
  //     mv.setViewName("result");
  //     // 冒頭に表示する文章の切り替え
  //     session.setAttribute("mode", process);
  //     // 入力したデータをデータベースに登録
  //     Billing billing = billingData.toEntity();
  //     System.out.print(billing.getApplication());
  //     billingRepository.saveAndFlush(billing);
  //     mv.addObject("billing", billing);
  //     // 追加・更新したアプリでの課金額の合計
  //     String application = billing.getApplication();
  //     List<Billing> billingByAppList = billingRepository.findByApplicationOrderByDateAsc(application);
  //     int sumBillingByApp = billingService.sumBilling(billingByAppList);
  //     mv.addObject("sumBillingByApp", sumBillingByApp);
  //     // 総課金額も表示する
  //     List<Billing> billingList = billingRepository.findAll();
  //     int sumBilling = billingService.sumBilling(billingList);
  //     mv.addObject("sumBilling", sumBilling);
  //     return mv;

  //   } else {
  //     // Integer id = billingData.getId(); // これはaddのとき使えない
  //     // return process.equals("add") ? "/input" : "/edit/" + id;
  //     mv.setViewName("input");
  //     // 課金履歴にあるアプリケーションの種類に関するリスト
  //     List<Billing> appList = billingRepository.findAppList();
  //     mv.addObject("appList", appList);
  //     return mv;
  //   }
  // }

  // 入力画面または編集画面で入力したデータを課金履歴に追加・更新
  @PostMapping("/result")
  public ModelAndView updateBilling(@ModelAttribute @Validated BillingData billingData,
                                    BindingResult result, ModelAndView mv) {
    // エラーチェック
    boolean isValid = billingService.isValid(billingData, result);
    if (!result.hasErrors() && isValid) {
      // エラーなし
      mv.setViewName("result");
      // 入力したデータをデータベースに登録
      Billing billing = billingData.toEntity();
      billingRepository.saveAndFlush(billing);
      mv.addObject("billing", billing);
      // 追加・更新したアプリでの課金額の合計
      String application = billing.getApplication();
      List<Billing> billingByAppList = billingRepository.findByApplicationOrderByDateAsc(application);
      int sumBillingByApp = billingService.sumBilling(billingByAppList);
      mv.addObject("sumBillingByApp", sumBillingByApp);
      // 総課金額も表示する
      List<Billing> billingList = billingRepository.findAll();
      int sumBilling = billingService.sumBilling(billingList);
      mv.addObject("sumBilling", sumBilling);
      return mv;

    } else {
      mv.setViewName("input");
      // 課金履歴にあるアプリケーションの種類に関するリスト
      List<Billing> appList = billingRepository.findAppList();
      mv.addObject("appList", appList);
      return mv;
    }
  }

}
