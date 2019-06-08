package edu.csumb.spring19.capstone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.annotation.WebListener;

@Configuration
@WebListener
public class ContextListener extends RequestContextListener {}