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
				OutputStream os = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				String op = dis.readUTF();
				String opnd = "";
				if(op.equals("put") || op.equals("get") || op.equals("delete")){
					opnd = dis.readUTF();
					String correct_node = this.membership_service.hashed_id(opnd);
					if(!correct_node.equals(this.storage_service.id)){
						dos.writeUTF("failed");
						socket.close();
						continue;
					}
					switch(op){
						case "put":{
							this.storage_service.put(opnd, dis);
							dos.writeUTF("sucess");
							break;
							}
						case "get":{
							this.storage_service.get(opnd, dos);
							dos.writeUTF("sucess");
							break;
						}
						case "delete":{
							this.storage_service.delete(opnd);
							dos.writeUTF("sucess");
							break;
						}
						default:
							break;
					}
				}
				else
					this.membership_service.view(dos);
				socket.close();
			} catch (Exception e) {
				System.out.println("TCP Handler failed.");
				// e.printStackTrace();
			}
		}
	}
}
