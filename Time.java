import java.io.File;


public class Time {
	
	private static int Secs, Mins, Hrs = 0;
	public static void currentTime() throws InterruptedException {
		Secs++;
		if(Secs == 60) {
			Secs = 0;
			Mins++;
		}
		if(Mins == 60) {
			Mins = 0;
			Hrs++;
		}
		Thread.sleep(1000);
	}
	public static void tellTime() {
		System.out.println(Hrs+":"+Mins+":"+Secs);
	}
	public static void checkPlay() throws InterruptedException {
		MusicPlayer MP = new MusicPlayer(new File("C:/Sound/Sound1.wav"));
		if(Secs == TargetTime.targetSecs && Mins == TargetTime.targetMins && Hrs == TargetTime.targetHrs) {
			MP.start();
			Main.metTime = true;
		}
	}
}
