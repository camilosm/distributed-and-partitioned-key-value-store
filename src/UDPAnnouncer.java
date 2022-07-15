package src;

public class UDPAnnouncer extends Thread {
	MembershipService membership_service;

	public UDPAnnouncer(MembershipService membership_service){
		this.membership_service = membership_service;
	}

	@Override
	public void run(){
		try {
			while(true){
				if(this.membership_service.membership_counter%2!=0)
					continue;
				this.membership_service.multicast("view");
				Thread.sleep(1000*this.membership_service.membership_counter);
			}
		} catch (Exception e) {
			System.err.println("UDP Announcer failed.");
			System.exit(1);
		}
	}
}
