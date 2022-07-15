package src;

import java.net.*;

public class UDPHandler extends Thread {
	MulticastSocket multicast_socket;
	MembershipService membership_service;

	public UDPHandler(MulticastSocket multicast_socket, MembershipService membership_service){
		this.multicast_socket = multicast_socket;
		this.membership_service = membership_service;
	}

	@Override
    public void run(){
		while(true){
			try{
				byte[] buffer = new byte[1024*10];
				DatagramPacket datagram_packet = new DatagramPacket(buffer, buffer.length);
				this.multicast_socket.receive(datagram_packet);
				String datagram_string = new String(datagram_packet.getData(), 0, datagram_packet.getLength());
				String datagram_data[] = datagram_string.split(" ");
				switch(datagram_data[0]){
					case "JOIN":
						this.membership_service.add(datagram_data[1], Integer.valueOf(datagram_data[2]));
						break;
					case "LEAVE":
						this.membership_service.del(datagram_data[1]);
						break;
					case "VIEW":
						for(int i = 1; i<datagram_data.length; i+=2)
							this.membership_service.add(datagram_data[i], Integer.valueOf(datagram_data[i+1]));
						break;
				}
			}
			catch (Exception e){
				System.err.println("UDP Handler failed.");
				System.err.println(e);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
