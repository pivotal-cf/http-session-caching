package io.pivotal.gemfire.demo.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController{

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

   @RequestMapping(value = "/")
   public String index(HttpSession httpSession, Model model) {

        Integer hits = (Integer) httpSession.getAttribute("hits");

        LOGGER.info("No. of Hits: '{}', session id '{}'", hits, httpSession.getId());

        if (hits == null) {
            hits = 0;
        }
        model.addAttribute("greeting", new Greeting());
        httpSession.setAttribute("hits", ++hits);
        return "index";
   }

  @GetMapping("/greeting")
  public String greeting(Model model) {
    model.addAttribute("greeting", new Greeting());
    return "greeting";
  }


  @PostMapping("/greeting")
  public String greetingSubmit(@ModelAttribute Greeting greeting, HttpSession httpSession) {
    httpSession.setAttribute("name", greeting.getId());
    httpSession.setAttribute("message", greeting.getContent());
    LOGGER.info("Got the session object :" + httpSession.getAttribute("name"));
    return "result";
  }


}
