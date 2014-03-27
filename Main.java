
public class Main {
	
	public static boolean metTime = false;
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("K");
		TargetTime.targetTime();
		while(metTime == false) {
			Time.currentTime();
			Time.tellTime();
			Time.checkPlay();
		}
	}
}
