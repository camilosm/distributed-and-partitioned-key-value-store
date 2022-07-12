package src;

import java.io.*;
import java.net.*;

public class TCPhandler extends Thread{
	ServerSocket server_socket;
	StorageService storage_service;

	public TCPhandler(ServerSocket server_socket, StorageService storage_service){
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
				String key = dis.readUTF();
				switch(op){
					case "put":
						this.storage_service.put(key, dis);
						break;
					case "get":
						OutputStream os = socket.getOutputStream();
						DataOutputStream dos = new DataOutputStream(os);
						this.storage_service.get(key, dos);
						dos.close();
						break;
					case "delete":
						this.storage_service.delete(key);
						break;
					case "view":
						// this.
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
