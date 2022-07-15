package src;

import java.io.*;
import java.net.*;

public class UDPHandler extends Thread {
	MulticastSocket multicast_socket;
	MembershipService membership_service;

	public UDPHandler(MulticastSocket multicast_socket, MembershipService membership_service){
		this.multicast_socket = multicast_socket;
		this.membership_service = membership_service;
	}

    public void run(){
		while(true){
			try{
				byte[] buffer = new byte[1024];
				DatagramPacket datagram_packet = new DatagramPacket(buffer, buffer.length);
				this.multicast_socket.receive(datagram_packet);
				String datagram_string = new String(datagram_packet.getData(), 0, datagram_packet.getLength());
				System.out.println(datagram_string);
			}
			catch (Exception e){
				System.err.println("UDP Handler failed.");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
