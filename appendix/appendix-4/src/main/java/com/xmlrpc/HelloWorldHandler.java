package com.xmlrpc;

public class HelloWorldHandler implements HelloWorld {

    public String getMessage(String msg) {
        String message = "request param: " + msg + "\n";
        return message;
    }
}

