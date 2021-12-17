/*
 * Copyright (C) 2018 -Present Pivotal Software, Inc. All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.gemfire.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

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
  public String greeting(Model model, HttpSession httpSession) {
    if ((httpSession.getAttribute("name")) !=null || httpSession.getAttribute("message") != null){
      Greeting greeting = new Greeting();
      greeting.setContent((String) httpSession.getAttribute("message"));
      greeting.setName((String)(httpSession.getAttribute("name")));
      model.addAttribute("greeting", greeting);
      return "result";
    }
    model.addAttribute("greeting", new Greeting());
    return "greeting";
  }


  @PostMapping("/greeting")
  public String greetingSubmit(@ModelAttribute Greeting greeting, HttpSession httpSession) {
    httpSession.setAttribute("name", greeting.getName());
    httpSession.setAttribute("message", greeting.getContent());
    LOGGER.info("Got the session object :" + httpSession.getAttribute("name"));
    return "result";
  }


}
