import java.util.Map;
import java.util.Scanner;

public class timezones {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        Map<String, Integer> timeZones = Map.ofEntries(
            Map.entry("UTC", 0),
            Map.entry("GMT", 0),
            Map.entry("BST", 60),
            Map.entry("IST", 60),
            Map.entry("WET", 0),
            Map.entry("WEST", 60),
            Map.entry("CET", 60),
            Map.entry("CEST", 120),
            Map.entry("EET", 120),
            Map.entry("EEST", 180),
            Map.entry("MSK", 180),
            Map.entry("MSD", 240),
            Map.entry("AST", -240),
            Map.entry("ADT", -180),
            Map.entry("NST", -210),
            Map.entry("NDT", -150),
            Map.entry("EST", -300),
            Map.entry("EDT", -240),
            Map.entry("CST", -360),
            Map.entry("CDT", -300),
            Map.entry("MST", -420),
            Map.entry("MDT", -360),
            Map.entry("PST", -480),
            Map.entry("PDT", -420),
            Map.entry("HST", -600),
            Map.entry("AKST", -540),
            Map.entry("AKDT", -480),
            Map.entry("AEST", 600),
            Map.entry("AEDT", 660),
            Map.entry("ACST", 570),
            Map.entry("ACDT", 630),
            Map.entry("AWST", 480)
        );
        
        for (int i = 0; i < n; i++) {
            String time = sc.next();
            int minutes = 0;

            if (time.equals("noon")) {
                minutes = 720;
            } else if (time.equals("midnight")) {
                minutes = 1440;
            } else {
                String b = sc.next();
                String[] arr = time.split(":");

                int hours = Integer.parseInt(arr[0]);
                int mins = Integer.parseInt(arr[1]);

                if (b.equals("a.m.")) {
                    if (hours == 12) hours = 0;
                } else {
                    if (hours != 12) hours += 12;
                }
                minutes = hours * 60 + mins;
            }

            int cTZ = timeZones.get(sc.next());
            minutes -= cTZ;

            int aTZ = timeZones.get(sc.next());
            minutes += aTZ;

            minutes = ((minutes % 1440) + 1440) % 1440;
            
            if (minutes == 0) {
                System.out.println("midnight");
            } else if (minutes == 720) {
                System.out.println("noon");
            } else {
                int hours24 = minutes / 60;
                int mins = minutes % 60;
                String ampm;

                if (hours24 < 12) {
                    ampm = "a.m.";
                } else { 
                    ampm = "p.m.";
                }

                if (hours24 == 0) {
                    int displayHour = 12;
                    System.out.println(String.format("%d:%02d %s", displayHour, mins, ampm));
                } else if (hours24 > 12) {
                    int displayHour = hours24 - 12;
                    System.out.println(String.format("%d:%02d %s", displayHour, mins, ampm));
                } else {
                    int displayHour = hours24;
                    System.out.println(String.format("%d:%02d %s", displayHour, mins, ampm));
                }
            }
        }
    }
}