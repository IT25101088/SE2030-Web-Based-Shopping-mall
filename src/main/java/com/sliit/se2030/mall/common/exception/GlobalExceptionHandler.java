package com.sliit.se2030.mall.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Central error handling for every @Controller in the app. Each
 * @ExceptionHandler method below is Spring's replacement for writing
 * try/catch around the same kind of failure in every single controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("statusCode", 404);
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedForResourceException.class)
    public ModelAndView handleAccessDenied(AccessDeniedForResourceException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ModelAndView mav = new ModelAndView("common/access-denied");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    // A business rule failure isn't a "page can't be shown" error -- the page the
    // user was on is still fine, they just tried something not allowed. So instead
    // of a full error page, we send them back where they came from with a message.
    @ExceptionHandler(BusinessRuleViolationException.class)
    public String handleBusinessRuleViolation(BusinessRuleViolationException ex,
                                               HttpServletRequest request,
                                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    // Spring throws this when a request matches no controller AND no static resource
    // (e.g. hitting "/" before we've built a HomeController). Genuinely a 404, not a
    // 500 -- without this handler it would fall through to the generic one below.
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFound(NoResourceFoundException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("statusCode", 404);
        mav.addObject("message", "Page not found.");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("statusCode", 500);
        mav.addObject("message", "Something went wrong. Please try again.");
        return mav;
    }
}
