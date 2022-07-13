package src;

import java.io.*;
import java.net.*;

public class TCPHandler extends Thread{
	ServerSocket server_socket;
	StorageService storage_service;

	public TCPHandler(ServerSocket server_socket, StorageService storage_service){
		this.server_socket = server_socket;
		this.storage_service = storage_service;
	}

	@Override
	public void run(){
		while(true){
			try{
				Socket socket = this.server_socket.accept();
				InputStream is = socket.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				String op = dis.readUTF();
				String opnd = dis.readUTF();
				switch(op){
					case "put":
						this.storage_service.put(opnd, dis);
						break;
					case "get":
						OutputStream os = socket.getOutputStream();
						DataOutputStream dos = new DataOutputStream(os);
						this.storage_service.get(opnd, dos);
						dos.close();
						break;
					case "delete":
						this.storage_service.delete(opnd);
						break;
					case "view":
						// dependant on membership service
					default:
						break;
				}
				dis.close();
				socket.close();
			} catch (Exception e) {
				System.out.println("Deu merda: "+ e);
			}
		}
	}
}
