package com.example.trolley;

public class URLs {
    private static final String ROOT_URL = "http://192.168.0.106/e-trolley/Api.php?apicall=";
//    private static final String ROOT_URL = "https://e-trolley-ycce.000webhostapp.com/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_CREATEINVOICE = ROOT_URL + "createInvoice";
    public static final String URL_SEARCH = ROOT_URL + "search";
    public static final String URL_ADD = ROOT_URL + "add";
    public static final String URL_DELETE = ROOT_URL + "delete";
    public static final String URL_MINUS = ROOT_URL + "minus";
    public static final String URL_CART = ROOT_URL + "cart";
    public static final String URL_PAY = ROOT_URL + "pay";
    public static final String URL_HISTORY = ROOT_URL + "history";

}