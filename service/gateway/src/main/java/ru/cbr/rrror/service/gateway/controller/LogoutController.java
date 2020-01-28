package ru.cbr.rrror.service.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
    @Slf4j
    @Controller
    static class ErrorControllerImpl implements ErrorController {

        @RequestMapping("/error")
        public String handleError(HttpServletRequest request) {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            log.debug("status code: " + status);
            return "error";
        }

        @Override
        public String getErrorPath() {
            return "error";
        }
    }
*/
    @Slf4j
    @Controller
    public class LogoutController {
        @RequestMapping("/logoutSuccessfull")
        public String logout() {
            return "logout";
        }
    }
