package com.cas.exception;

import javax.security.auth.login.AccountException;

public class CaptchaException extends AccountException {

    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super();
    }

    public CaptchaException(String arg0) {
        super(arg0);
    }
}
