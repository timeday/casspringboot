package com.cas.config;

import org.apereo.cas.authentication.Credential;

import java.io.Serializable;
import java.util.Objects;

public class UsernamePasswordCaptchaCredential implements Credential, Serializable {

    /**
     * Authentication attribute name for password.
     **/
    public static final String AUTHENTICATION_ATTRIBUTE_PASSWORD = "credential";

    private static final long serialVersionUID = -700605081472810939L;

    private String username;

    private String password;


    public UsernamePasswordCaptchaCredential() {
    }

    public UsernamePasswordCaptchaCredential(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String userName) {
        this.username = userName;
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public String toString() {
        return this.username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsernamePasswordCaptchaCredential)) return false;
        UsernamePasswordCaptchaCredential that = (UsernamePasswordCaptchaCredential) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
