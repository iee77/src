import java.util.Scanner;


public class TargetTime {
	
	public static int targetSecs, targetMins, targetHrs;
	static Scanner Scan = new Scanner(System.in);
	
	public static void targetTime() {
		System.out.println("Secs?");
		targetSecs = Scan.nextInt();
		System.out.println("Mins?");
		targetMins = Scan.nextInt();
		System.out.println("Hours?");
		targetHrs = Scan.nextInt();
	}
	
}
