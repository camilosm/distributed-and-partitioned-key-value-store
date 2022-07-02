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
		String op = args[1];
		if(op.equals("put") || op.equals("get") || op.equals("delete")){
			if(args.length != 3){
				System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
				System.exit(1);
			}
			String opnd = args[2];
		}
	}
}

