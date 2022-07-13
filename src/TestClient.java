package src;

import java.io.*;
import java.net.*;

public class TestClient {
	private static void usage() {
		System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
		System.exit(1);
	}

	public static void main(String args[]) throws IOException {
		if(args.length < 2 || args.length > 3)
			usage();
		String node_ap[] = args[0].split(":");
		if(node_ap.length != 2)
			usage();
		String node_ip = node_ap[0];
		Integer node_port = Integer.valueOf(node_ap[1]);
		String op = args[1];
		if(op.equals("put") || op.equals("get") || op.equals("delete")){
			if(args.length != 3)
				usage();
			String opnd = args[2];
			Socket socket = new Socket(node_ip, node_port);
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(op);
			switch(op){
				case "put":{
					File file = new File(opnd);
					String key = Hashing.hash(file.getAbsolutePath());
					System.out.println("Key: " + key);
					byte[] byte_array = new byte[(int)file.length()];
					FileInputStream fis = new FileInputStream(file);
					DataInputStream dis = new DataInputStream(fis);
					dis.readFully(byte_array, 0, byte_array.length);
					dos.writeUTF(key);
					dos.writeLong(byte_array.length);
					dos.write(byte_array, 0, byte_array.length);
					dis.close();
					break;
				}
				case "get":{
					dos.writeUTF(opnd);
					InputStream is = socket.getInputStream();
					DataInputStream dis = new DataInputStream(is);
					long size = dis.readLong();
					File folder = new File("received");
					if(!folder.exists())
						folder.mkdir();
					OutputStream os_file = new FileOutputStream("received/" + opnd);
					byte[] buffer = new byte[1024];
					int bytes_read;
					while (size > 0 && (bytes_read = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1){
						os_file.write(buffer, 0, bytes_read);
						size -= bytes_read;
					}
					os_file.close();
					dis.close();
					break;
				}
				case "delete":
					dos.writeUTF(opnd);
					break;
				default:
					break;
			}
			dos.close();
			socket.close();
		}
	}
}

