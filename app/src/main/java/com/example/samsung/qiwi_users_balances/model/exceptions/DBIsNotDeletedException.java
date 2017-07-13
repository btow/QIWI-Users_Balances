package com.example.samsung.qiwi_users_balances.model.exceptions;

public class DBIsNotDeletedException extends RuntimeException {

    public DBIsNotDeletedException(final String msg) {
        super(msg);
    }
}
