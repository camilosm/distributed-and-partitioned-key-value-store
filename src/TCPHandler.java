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
						dos.writeUTF("key");
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
							if(this.storage_service.contains(opnd)){
								dos.writeUTF("sucess");
								this.storage_service.get(opnd, dos);
							}
							else
								dos.writeUTF("failed");
							break;
						}
						case "delete":{
							if(this.storage_service.contains(opnd)){
								dos.writeUTF("sucess");
								this.storage_service.delete(opnd);
							}
							else
								dos.writeUTF("failed");
							break;
						}
						default:
							break;
					}
				}
				else{
					switch(op){
						case "view":
							this.membership_service.view(dos);
							break;
						case "join":
							this.membership_service.join();
							break;
						case "leave":
							this.membership_service.leave();
							break;
						default:
							break;
					}

				}
				socket.close();
			} catch (Exception e) {
				System.err.println("TCP Handler failed.");
				// e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
