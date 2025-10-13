package com.cybene.cyposdashboard.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateInput {
    //Will contain the boolean value of the validation response
    boolean response;
    private Pattern pattern;
    private Matcher matcher;
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
    private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String PHONE_PATTERN = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

    //Method should return true if the text is not empty and false if otherwise
    public boolean validateText(String text){
        if (text.equals("") || text.length()<4){
            response = false;
        }else{
            response = true;
        }
        return  response;
    }
    //Method should return true if email is valid and false if not
    public boolean validateEmail(String email){
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        if(matcher.matches()){
            response = true;
        }else {
            response = false;
        }
        return response;
    }
    //Method should return true if password is valid and false if not
    public boolean validatePassword(String password){
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if(matcher.matches()){
            response = true;
        }else {
            response = false;
        }
        return response;
    }
    //Method should return true if phone is valid and false if not
    public boolean validatePhone(String phone){
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        if(matcher.matches()){
            response = true;
        }else {
            response = false;
        }
        return response;
    }

}
