package src;

import java.io.*;
import java.net.*;

public class Store {
    String ip_mcast_addr;
    Integer ip_mcast_port;
    String ip_addr;
    Integer store_port;
	String hashed_id;
	String id;
	ServerSocket server_socket;
	StorageService storage_service;
	MembershipService membership_service;

    public Store(String ip_mcast_addr, int ip_mcast_port, String ip_addr, int store_port) throws IOException {
        this.ip_mcast_addr = ip_mcast_addr;
		this.ip_mcast_port = ip_mcast_port;
		this.ip_addr = ip_addr;
        this.store_port = store_port;
		this.id = this.ip_addr;
		this.hashed_id = Hashing.hash(this.id);

		this.storage_service = new StorageService(hashed_id);

		InetSocketAddress socket_address = new InetSocketAddress(this.ip_addr, this.store_port);
		this.server_socket = new ServerSocket();
		this.server_socket.bind(socket_address);

		this.membership_service = new MembershipService(this.id);
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 4){
            System.err.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id> <store_port>");
            System.exit(1);
        }

		String ip_mcast_addr = args[0];
		Integer ip_mcast_port = Integer.valueOf(args[1]);
		String ip_addr = args[2];
		Integer store_port = Integer.valueOf(args[3]);

        Store store = new Store(ip_mcast_addr, ip_mcast_port, ip_addr, store_port);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("Storage node at " + store.id + " terminated.");
			}
		}));

		TCPHandler tcp_handler = new TCPHandler(store.server_socket, store.storage_service, store.membership_service);
		tcp_handler.start();
    }
}
