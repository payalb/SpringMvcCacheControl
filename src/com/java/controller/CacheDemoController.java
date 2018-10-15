package com.java.controller;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CacheDemoController {

	@RequestMapping("/caching")
	public String data(HttpServletResponse response, Model model) {
		/*
		 * response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		 */
		/*
		 * CacheControl class is based on builder pattern, a very convenient way to
		 * create 'Cache-Control' headers with different directives. In above code we
		 * are specifying that browser cache should expire after 10 secs.
		 */
		String headerValue = CacheControl.maxAge(10, TimeUnit.MINUTES).getHeaderValue();
		System.out.println("In controller");
		response.addHeader("Cache-Control", headerValue);
	/*	response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);*/
		model.addAttribute("message", "From caching controller"+ LocalDateTime.now());
		return "data";
	}
}
