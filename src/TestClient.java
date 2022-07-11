package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class TestClient {
	private static void usage() {
		System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
		System.exit(1);
	}

	public static void main(String args[]) throws IOException {
		if(args.length < 2 || args.length > 3)
			usage();

		String node_ap[] = args[0].split(":", 0);
		if(node_ap.length != 2)
			usage();
		String node_ip = node_ap[0];
		int node_port = Integer.parseInt(node_ap[1]);
		// System.out.println("IP: " + node_ip + " port: " + node_port);
		// String op = args[1];
		// if(op.equals("put") || op.equals("get") || op.equals("delete")){
		// 	if(args.length != 3){
		// 		System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
		// 		System.exit(1);
		// 	}
		// 	String opnd = args[2];
		// }

		Socket socket = new Socket("127.0.0.1", 2013);

		File file = new File("README.md");
		byte[] byte_array = new byte[(int)file.length()];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
        dis.readFully(byte_array, 0, byte_array.length);

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(file.getName());
        dos.writeLong(byte_array.length);
        dos.write(byte_array, 0, byte_array.length);
        dos.flush();

        //Sending file data to the server
        os.write(byte_array, 0, byte_array.length);
        os.flush();

        //Closing socket
        os.close();
        dos.close();
		dis.close();
        socket.close();
	}
}

