package src;

import java.io.*;
import java.net.*;

public class TCPHandler extends Thread{
	ServerSocket server_socket;
	StorageService storage_service;
	MembershipService membership_service;

	public TCPHandler(ServerSocket server_socket, StorageService storage_service, MembershipService membership_service){
		this.server_socket = server_socket;
		this.storage_service = storage_service;
		this.membership_service = membership_service;
	}

	@Override
	public void run(){
		while(true){
			try{
				Socket socket = this.server_socket.accept();
				InputStream is = socket.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				String op = dis.readUTF();
				switch(op){
					case "put":{
						String opnd = dis.readUTF();
						this.storage_service.put(opnd, dis);
						break;
						}
					case "get":{
						String opnd = dis.readUTF();
						OutputStream os = socket.getOutputStream();
						DataOutputStream dos = new DataOutputStream(os);
						this.storage_service.get(opnd, dos);
						break;
					}
					case "delete":{
						String opnd = dis.readUTF();
						this.storage_service.delete(opnd);
						break;
					}
					case "view":{
						OutputStream os = socket.getOutputStream();
						DataOutputStream dos = new DataOutputStream(os);
						this.membership_service.view(dos);
						break;
					}
					default:
						break;
				}
				socket.close();
			} catch (Exception e) {
				System.out.println("TCP Handler failed.");
				e.printStackTrace();
			}
		}
	}
}
