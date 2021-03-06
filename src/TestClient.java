package src;

import java.io.*;
import java.net.*;

public class TestClient {
	private static void usage() {
		System.err.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
		System.exit(1);
	}

	private static boolean error(String response) {
		if(!response.equals("sucess")){
			switch(response){
				case "key":
					System.err.println("Wrong node.");
					return true;
				case "failed":
					System.err.println("File not found.");
					return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		if(args.length < 2 || args.length > 3)
			usage();
		String node_ap[] = args[0].split(":");
		if(node_ap.length != 2)
			usage();
		String node_ip = node_ap[0];
		Integer node_port = Integer.valueOf(node_ap[1]);
		String op = args[1];
		try {
			Socket socket = new Socket(node_ip, node_port);
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(op);
			InputStream is = socket.getInputStream();
			DataInputStream dis_socket = new DataInputStream(is);
			if(op.equals("put") || op.equals("get") || op.equals("delete")){
				if(args.length != 3)
					usage();
				String opnd = args[2];
				switch(op){
					case "put":{
						File file = new File(opnd);
						String key = Hashing.hash(file.getAbsolutePath());
						System.out.println("Key: " + key);
						dos.writeUTF(key);
						byte[] byte_array = new byte[(int)file.length()];
						FileInputStream fis = new FileInputStream(file);
						DataInputStream dis_file = new DataInputStream(fis);
						dis_file.readFully(byte_array, 0, byte_array.length);
						dos.writeLong(byte_array.length);
						dos.write(byte_array, 0, byte_array.length);
						dis_file.close();
						String response = dis_socket.readUTF();
						error(response);
						break;
					}
					case "get":{
						dos.writeUTF(opnd);
						String response = dis_socket.readUTF();
						if(error(response))
							break;
						long size = dis_socket.readLong();
						File folder = new File("received");
						if(!folder.exists())
							folder.mkdir();
						OutputStream os_file = new FileOutputStream("received/" + opnd);
						byte[] buffer = new byte[1024];
						int bytes_read;
						while (size > 0 && (bytes_read = dis_socket.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1){
							os_file.write(buffer, 0, bytes_read);
							size -= bytes_read;
						}
						os_file.close();
						break;
					}
					case "delete":
						dos.writeUTF(opnd);
						String response = dis_socket.readUTF();
						error(response);
						break;
					default:
						break;
				}
			}
			else if(op.equals("view")){
				Integer n = dis_socket.readInt();
				for(int i=0; i<n; i++)
					System.out.println("IP: " + dis_socket.readUTF() + " -> membership counter: " + dis_socket.readInt());
			}
			else{
				switch(op){
					case "join":
					case "leave":
						break;
					default:
						System.err.println("Unknow operation.");
						System.exit(1);
				}
			}
			socket.close();
		} catch (Exception e){
			System.err.println("Transaction failed.");
			System.exit(1);
		}
	}
}

