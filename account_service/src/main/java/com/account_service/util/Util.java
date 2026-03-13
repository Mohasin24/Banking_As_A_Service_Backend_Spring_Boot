package com.account_service.util;

import com.account_service.constant.AccountType;
import com.account_service.dto.AccountRequest;

public class Util {
    private static final String BANK_CODE = "313";
    private static final String BRANCH_CODE = "01";
    private static final int USER_ID_PADDING_LENGTH=8;

    public static String generateAccountNo(AccountRequest accountRequest){

        String accountTypeCode = Integer.toString(AccountType.valueOf(accountRequest.accountType().toUpperCase()).ordinal());

        System.err.println("Account Code: " + accountTypeCode);

        String paddedUserId = String.format("%0"+USER_ID_PADDING_LENGTH+"d", accountRequest.userId());

        String accountNumber = BANK_CODE+BRANCH_CODE+accountTypeCode+paddedUserId;

        System.err.println("Account no. length: " + accountTypeCode.length());

       return accountNumber;

    }

}
