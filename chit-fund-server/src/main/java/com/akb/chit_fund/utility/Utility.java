package com.akb.chit_fund.utility;

import org.apache.commons.lang3.StringUtils;

public class Utility {

    private static final String MOBILE_NUMBER_PATTERN = "^\\d{10}$";

    public static boolean isValidMobileNumber(String mobileNumber) {
        return StringUtils.isBlank(mobileNumber) && mobileNumber.matches(MOBILE_NUMBER_PATTERN);
    }
}
