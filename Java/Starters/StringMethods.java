public class StringMethods {
    public static void main(String[] args) {
      String rawFirstName = "   mArLeY   ";
      String lastName = "smith";
      String secretCode = "DEFCON-9-SECURE";
      String correctPassword = "JavaIsFun123";
      String userPasswordInput = "javaisfun123"; 

      rawFirstName = rawFirstName.trim();

      System.out.println(secretCode.length());
      System.out.println(rawFirstName.concat(" " + lastName));
      System.out.println(secretCode.indexOf("9"));
      System.out.println(lastName.charAt(0));
      System.out.println(correctPassword.equals(userPasswordInput));
      System.out.println(correctPassword.equalsIgnoreCase(userPasswordInput));
      System.out.println(secretCode.substring(9, 15));
      System.out.println(lastName.toUpperCase());
      System.out.println(rawFirstName.toLowerCase());
    }
}
