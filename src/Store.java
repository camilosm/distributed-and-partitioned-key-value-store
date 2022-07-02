package src;
import java.io.*;
import java.net.*;

public class Store {

    String ip_mcast_addr;
    int ip_mcast_port;
    String ip_addr;
    int store_port;
	ServerSocket server;

    public Store(String ip_mcast_addr, int ip_mcast_port, String ip_addr, int store_port) throws IOException {
        this.ip_mcast_addr = ip_mcast_addr;
		this.ip_mcast_port = ip_mcast_port;
		this.ip_addr = ip_addr;
        this.store_port = store_port;

		InetSocketAddress socketaddress = new InetSocketAddress(this.ip_addr, this.store_port);
		this.server = new ServerSocket();
		this.server.bind(socketaddress);
    }


    public static void main(String[] args) throws IOException {
        if(args.length != 4){
            System.err.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id> <store_port>");
            System.exit(1);
        }

		String ip_mcast_addr = args[0];
		int ip_mcast_port = Integer.parseInt(args[1]);
		String ip_addr = args[2];
		int store_port = Integer.parseInt(args[3]);

        Store store = new Store(ip_mcast_addr, ip_mcast_port, ip_addr, store_port);
		while(true){
			store.server.accept();
		}
    }
}
