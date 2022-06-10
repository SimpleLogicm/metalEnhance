package cpm.simplelogic.helper;

public class Check_Null_Value {

	public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJava(final String string)
	{
	   if(string != null && !string.isEmpty() && !string.trim().isEmpty() &&  string != "null" && !string.equalsIgnoreCase("") && !string.equalsIgnoreCase(" ") && !string.equalsIgnoreCase("null"))
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
	}


	public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(final String string) {
		return string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0");
	}

	public static Boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck_b(final String string) {
		return string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0");
	}

	public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(final String string) {
		return string != null && !string.isEmpty() && !string.trim().isEmpty() && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("") && !string.equalsIgnoreCase(" ") && !string.equalsIgnoreCase("0") && !string.equalsIgnoreCase("0.0");
	}

}
