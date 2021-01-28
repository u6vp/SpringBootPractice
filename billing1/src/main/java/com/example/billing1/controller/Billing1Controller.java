package com.example.billing1.controller;

import java.util.List;

import com.example.billing1.entity.Billing1;
import com.example.billing1.repository.Billing1Repository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class Billing1Controller {
  private final Billing1Repository billing1Repository;

  // Lombok Annotations Support for VS Code 拡張追加しないと
  // 上記のような1行でのコンストラクタインジェクションの記述がビルド通らない
  // public Billing1Controller(Billing1Repository todoRepository) {
  //   this.billing1Repository = billing1Repository;
  // }

  @GetMapping("/applicationhistory")
  public ModelAndView showTodoList(ModelAndView mv) {
    // 一覧を検索して表示する
    mv.setViewName("applicationhistory");
    List<Billing1> billing1List = billing1Repository.findAll(); // 3
    mv.addObject("billing1List", billing1List); // 4
    return mv;
  }
}
