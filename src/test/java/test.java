import httpServer.booter;

public class test {
	public static void main(String[] args) {
		booter booter = new booter();
		try {
			System.out.println("GrapeVote!");
			System.setProperty("AppName", "GrapeVote");
			booter.start(1008);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
