package ru.catstack.nfc_terminal.exception.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.catstack.nfc_terminal.exception.*;
import ru.catstack.nfc_terminal.model.payload.response.ApiErrorResponse;

import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Autowired
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

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    @ResponseBody
    public ApiErrorResponse handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 226, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 400, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = UserLoginException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiErrorResponse handleUserLoginException(UserLoginException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiErrorResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = UserRegistrationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiErrorResponse handleUserRegistrationException(@NotNull UserRegistrationException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = InvalidJwtTokenException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiErrorResponse handleInvalidJwtTokenException(InvalidJwtTokenException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
    }

    @ExceptionHandler(value = UserLogOutException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiErrorResponse handleUserLogOutException(UserLogOutException ex, WebRequest request) {
        return new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                           @NotNull HttpHeaders headers,
                                                                           @NotNull HttpStatus status,
                                                                           @NotNull WebRequest request) {
        var errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        var response = new ApiErrorResponse(errorMessage, 417, ex.getClass().getName(), pathFromRequest(request));
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }
}
