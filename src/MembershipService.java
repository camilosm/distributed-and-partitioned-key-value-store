package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class MembershipService {
	String ip_mcast_addr;
	Integer ip_mcast_port;
	String id;
	String hashed_id;
	String folder_path;
	Integer membership_counter;
	SortedMap<String,String> cluster_membership;
	HashMap<String,Integer> members_counters;
	MulticastSocket multicast_socket;

	public MembershipService(String ip_mcast_addr, Integer ip_mcast_port, String id, String hashed_id, String folder_path) throws IOException {
		this.ip_mcast_addr = ip_mcast_addr;
		this.ip_mcast_port = ip_mcast_port;
		this.id = id;
		this.hashed_id = hashed_id;
		this.folder_path = folder_path;
		this.membership_counter = -1;
		this.cluster_membership = new TreeMap<String,String>();
		this.members_counters = new HashMap<String,Integer>();
		this.multicast_socket = new MulticastSocket(this.ip_mcast_port);
		this.join();
	}

	public void counterToFile() throws IOException{
		File counter_file = new File(this.folder_path + "membership_counter.txt");
		counter_file.createNewFile();
		FileWriter fw = new FileWriter(counter_file);
		fw.write(this.membership_counter.toString());
		fw.close();
	}

	public void join() throws IOException {
		InetAddress mcast_addr = InetAddress.getByName(ip_mcast_addr);
		SocketAddress mcast_socket_addr = new InetSocketAddress(mcast_addr, this.ip_mcast_port);
		NetworkInterface net_int = NetworkInterface.getByInetAddress(mcast_addr);
		this.multicast_socket.joinGroup(mcast_socket_addr, net_int);
		this.membership_counter++;
		this.cluster_membership.put(this.hashed_id, this.id);
		this.members_counters.put(this.id, this.membership_counter);
		this.counterToFile();
        return;
    }

    public void leave() throws IOException {
		InetAddress mcast_addr = InetAddress.getByName(ip_mcast_addr);
		SocketAddress mcast_socket_addr = new InetSocketAddress(mcast_addr, this.ip_mcast_port);
		NetworkInterface net_int = NetworkInterface.getByInetAddress(mcast_addr);
		this.multicast_socket.leaveGroup(mcast_socket_addr, net_int);
		this.membership_counter++;
		this.cluster_membership.clear();
		this.members_counters.clear();
		this.counterToFile();
        return;
    }

	public void view(DataOutputStream dos) throws IOException {
		dos.writeInt(this.cluster_membership.size());
		for(Map.Entry<String,Integer> entry : this.members_counters.entrySet()){
			dos.writeUTF(entry.getKey());
			dos.writeInt(entry.getValue());
		}
		return;
	}

	public String hashed_id(String key_in){
		String node_hashed_id = this.cluster_membership.firstKey();
		for(String cluster_key : this.cluster_membership.keySet())
			if(key_in.compareTo(cluster_key)<=0)
				node_hashed_id = cluster_key;
		return node_hashed_id;
	}

	public String id(String hashed_id){
		return this.cluster_membership.get(hashed_id);
	}
}
