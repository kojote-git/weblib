package com.jkojote.weblib.config;

import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Import(MvcConfig.class)
public class WebConfig {
}
