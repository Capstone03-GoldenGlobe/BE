package com.capstone03.goldenglobe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
  @GetMapping("/")
  @ResponseBody
  String main() {
    return "서버 연결 성공~";
  }
}
