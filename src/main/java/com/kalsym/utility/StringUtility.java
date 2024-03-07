package com.kalsym.utility;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author taufik
 */
public class StringUtility {

    /**
     * Formats passed string in 923xxxxxxxxx format e.g if passed parameter is
     * 03434153312 or 3435163312 it will return 923435163312
     *
     * @param phoneNumber
     * @return msisdn in 923xxxxxxxxx format
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10) {
            phoneNumber = phoneNumber;
        } else if (phoneNumber.startsWith("0") == true) {
            //03
            phoneNumber = "92" + phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("92") == true) {
            //923
            phoneNumber = phoneNumber;
        } else {
            //33
            phoneNumber = "92" + phoneNumber;
        }
        return phoneNumber;
    }

    /**
     * Checks if a number is valid for a specific 2 character country code. 
     * @example PK, CH
     *
     * @param number
     * @param countryCode
     * @return
     * @throws java.lang.Exception
     */
    public static boolean isPhoneNumberValid(String number, String countryCode) throws Exception {
        boolean isValid = false;
        if (null != number && !number.matches(".*[a-zA-Z].*")) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
          
            try {
                PhoneNumber numberProto = phoneUtil.parse(number, countryCode);
                isValid = phoneUtil.isValidNumber(numberProto);
            } catch (NumberParseException e) {
                //LogProperties.writeLog("NumberParseException was thrown: " + e);
                throw new Exception(e);
            }
        } else {
            //LogProperties.writeLog("number:" + number + " contains alphabets");
            isValid = false;
        }
        return isValid;
    }

    public static String parseNumberInInternationFormat(String number,
            String countryCode) throws Exception {
        String internationFormat = "";
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            PhoneNumber numberProto = phoneUtil.parse(number, countryCode);
            internationFormat = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).replaceAll("\\s", "");
        } catch (NumberParseException e) {
            // LogProperties.writeLog("exception:" + e);
            throw new Exception(e);
        }
        return internationFormat;
    }

    /**
     * Adds 92 prefix to an msisdn if it is not present for e.g. 03004455678 and
     * 3004455678 returns 923004455678, 923004455678 returns 923004455678
     * without any change
     *
     * @param msisdn
     * @return 92 prepended msisdn
     */
    public static String prepend92(String msisdn) {
        msisdn = msisdn.trim();
        if (msisdn.startsWith("3")) {
            msisdn = "92" + msisdn;
        } else if (msisdn.startsWith("0")) {
            msisdn = msisdn.substring(1);
            msisdn = "92" + msisdn;
        }
        return msisdn;
    }

    /**
     * checks if passed parameter is correct phone number e.g if passed number
     * is 776323dfss or 92343516331298 or 97578 it will return false If number
     * is 923435163312 it will return true
     *
     * @param phoneNumber
     * @return TRUE if number format is valid, FALSE if phone number contains
     * alphabets or more than 12 characters or less than 10 characters
     */
    public static boolean checkPhoneFormat(String phoneNumber) {
        if (phoneNumber.length() < 10) {
            return false;
        } else if (phoneNumber.length() > 12) {
            return false;
        } else if (phoneNumber.contains("[a-zA-Z]+")) {
            return false;
        } else if (phoneNumber.contains(" ")) {
            return false;
        } else if (phoneNumber.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Replaces &, <, >, ', and \ with their asci code in String (xml) e.g
     * replaces & with $amp; and < with $lt; etc @param
     *
     * str @return Normalized String (xml)
     *
     * @param str
     * @return
     */
    public static String normalizeXML(String str) {
        String normalized = str;
        normalized = normalized.replace("&", "&amp;");//ampersand - mandatory
        normalized = normalized.replace("<", "&lt;");//less than - mandatory
        normalized = normalized.replace(">", "&gt;");//greater than - optional
        normalized = normalized.replace("'", "&apos;");//apostrophe - optional
        normalized = normalized.replace("\"", "&quot;");//quotation mark - optional
        return normalized;
    }

    /**
     * insert '\\' in string after index of '\' and '\\' e.g if is string is
     * "hello\" it will return "hello\\" and if string is "another\\string" it
     * will return "another\\\string"
     *
     * @param str
     * @return
     */
    public static String addSlashes(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder s = new StringBuilder(str);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') {
                s.insert(i++, '\\');
            }
            if (s.charAt(i) == '\\') {
                s.insert(i++, '\\');
            }
        }
        return s.toString();
    }

    /**
     * Generates a unique hex terminated and prefix appended Random ID
     *
     * @param appInitials prefix to be appended
     * @return A Unique identifier with datetime and <code>appInitials</code>
     * prepended
     */
    public static String createRefId(String appInitials) {
        int min = 4097; // hex equivalant 1001
        int max = 65534; // hex equivalant fffe

        // Not generating UUID string since it's big in length
        // Creating a random UUID (Universally unique identifier).
        //        UUID uuid = UUID.randomUUID();
        //        String randomUUIDString = uuid.toString();
        Random r = new Random();
        int decRand1 = r.nextInt(max - min + 1) + min;
        int decRand2 = r.nextInt(max - min + 1) + min;
        int decRand = decRand1 + decRand2;
        String hexRand = Integer.toHexString(decRand);
        String Prefix = appInitials;

        DateFormat dateFormat = new SimpleDateFormat("ddMMyyHHmmss");

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();

        String dateStr = dateFormat.format(cal.getTime());
        String refId = Prefix + dateStr + hexRand;
        return refId;
    }

    /**
     * Gets the First Regular Expression Match, as String Returns empty string
     * if no match found
     *
     * @param InputString String to be scanned to find the pattern.
     * @param RegExpString
     * @return
     */
    public static String getRegularExpressionMatch(String InputString, String RegExpString) {
        String result = "";

        Pattern ptrn = Pattern.compile(RegExpString);

        // Now create matcher object.
        Matcher m = ptrn.matcher(InputString);
        if (m.find()) {
            result = m.group(0);
        } else {
            //System.out.println("NO MATCH");
        }
        return result;
    }

    /**
     * Checks if the inputString has a match from the regular expression
     * provided
     *
     * @param InputString String to be scanned to find the pattern.
     * @param RegExpString
     * @return true: if match found, false: otherwise
     */
    public static boolean regexIsMatch(String InputString, String RegExpString) {
        try {
            Pattern ptrn = Pattern.compile(RegExpString);
            // Now create matcher object.
            Matcher m = ptrn.matcher(InputString);
            if (m.find()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exp) {
            return false;
        }
    }

    /**
     * Returns date in yyyy-mm-dd format. e.g 20140523 to 2014-05-23
     *
     * @param input
     * @return
     */
    public static String convertDate(String input) {
        String formattedDate = "";
        String yr = input.substring(0, 4);
        String mth = input.substring(4, 6);
        String dt = input.substring(6, 8);
        formattedDate = yr + "-" + mth + "-" + dt;
        return formattedDate;
    }
}
