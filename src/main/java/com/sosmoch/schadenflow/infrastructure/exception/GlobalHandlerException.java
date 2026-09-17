package com.sosmoch.schadenflow.infrastructure.exception;


import com.sosmoch.schadenflow.auth.IdentityVerificationException;
import com.sosmoch.schadenflow.insuredclient.exceptions.ContractNotFoundException;
import com.sosmoch.schadenflow.insuredclient.exceptions.ContractOwnershipMismatchException;
import com.sosmoch.schadenflow.insuredclient.exceptions.InactiveContractException;
import com.sosmoch.schadenflow.insuredclient.exceptions.InsuredClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    // AUTH MODULE
    @ExceptionHandler(IdentityVerificationException.class)
    public ResponseEntity<ErrorResponse> handleIdentityVerification(IdentityVerificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    // CLIENT / CONTRACT MODULE
    @ExceptionHandler(InsuredClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInsuredClientNotFound(InsuredClientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleContractNotFound(ContractNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(InactiveContractException.class)
    public ResponseEntity<ErrorResponse> handleInactiveContract(InactiveContractException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ContractOwnershipMismatchException.class)
    public ResponseEntity<ErrorResponse> handleContractOwnershipMismatch(ContractOwnershipMismatchException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

}
