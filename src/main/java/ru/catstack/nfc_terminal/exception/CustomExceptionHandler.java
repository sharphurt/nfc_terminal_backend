package ru.catstack.nfc_terminal.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.catstack.nfc_terminal.model.payload.response.ApiErrorResponse;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;

import javax.validation.constraints.NotNull;
import java.util.Locale;

public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String resolveLocalizedErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
        logger.info(localizedErrorMessage);
        return localizedErrorMessage;
    }

    private String pathFromRequest(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest().getAttribute("javax.servlet.forward.request_uri").toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    @ResponseBody
    public ApiResponse handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 226, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 400, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserLoginException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserLoginException(UserLoginException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserRegistrationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserRegistrationException(@NotNull UserRegistrationException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

}
