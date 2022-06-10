package com.msimplelogic.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Check_Null_Value {

    public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(final String string) {
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("") && !string.equalsIgnoreCase(" ") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0")) {
            return true;
        } else {
            return false;
        }
    }

    static SimpleDateFormat dfDate  = new SimpleDateFormat("dd-MM-yyyy");
    public static boolean checkDates(final String fromdate, final String todate)    {
        try {
            if(dfDate.parse(fromdate).before(dfDate.parse(todate)))
            {
                return true;//If start date is before end date
            }
            else if(dfDate.parse(fromdate).equals(dfDate.parse(fromdate)))
            {
                return true;//If two dates are equal
            }
            else
            {
                return false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

	public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJava(final String string)  
	{
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("") && !string.equalsIgnoreCase(" ")) {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
    }

    public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzero(final String string) {
        return string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("") && !string.equalsIgnoreCase(" ") && !string.equalsIgnoreCase("0");
    }

    public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(final String string) {
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0")) {
            return true;
        } else {
            return false;
        }
    }

    public static String isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(final String string) {
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0")) {
            return string;
        } else {
            return " ";
        }
    }



    public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(final String string) {
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0")) {
            return true;
        } else {
            return false;
        }
    }

    public static String isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(final String string) {
        if (string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0")) {
            return string;
        } else {
            return "";
        }
    }

    public static Boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck_b(final String string) {
        return string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0");
    }
}
