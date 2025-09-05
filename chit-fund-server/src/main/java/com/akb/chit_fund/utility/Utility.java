package com.akb.chit_fund.utility;

public class Utility {

    public static boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber != null && mobileNumber.matches("^\\d{10}$");
    }
}
