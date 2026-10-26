package com.sosmoch.schadenflow.infrastructure.exception;


import com.sosmoch.schadenflow.agent.exceptions.AgentHasOpenClaimsException;
import com.sosmoch.schadenflow.agent.exceptions.AgentNotFoundException;
import com.sosmoch.schadenflow.agent.exceptions.IdentityCheckException;
import com.sosmoch.schadenflow.auth.AccountDisabledException;
import com.sosmoch.schadenflow.auth.IdentityVerificationException;
import com.sosmoch.schadenflow.insuredclient.exceptions.*;
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

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ErrorResponse> handleAccountDisabled(AccountDisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
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

    @ExceptionHandler(InvalidClientTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidClientTransition(InvalidClientTransitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(CsvImportFailedException.class)
    public ResponseEntity<ErrorResponse> handleCsvImportFailed(CsvImportFailedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(CsvRowException.class)
    public ResponseEntity<ErrorResponse> handleCsvRowException(CsvRowException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(CsvImportMissingHeader.class)
    public ResponseEntity<ErrorResponse> handleCsvImportMissingHeader(CsvImportMissingHeader ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }


    // AGENT MODULE
    @ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAgentNotFound(AgentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));

    }

    @ExceptionHandler(IdentityCheckException.class)
    public ResponseEntity<ErrorResponse> handleIdentityCheck(IdentityCheckException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(AgentHasOpenClaimsException.class)
    public ResponseEntity<ErrorResponse> handleAgentHasOpenClaims(AgentHasOpenClaimsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    // GENERAL EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
