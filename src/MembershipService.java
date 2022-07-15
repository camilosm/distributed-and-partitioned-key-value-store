package src;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class MembershipService {
	String ip_mcast_addr;
	Integer ip_mcast_port;
	String id;
	String hashed_id;
	String folder_path;
	Integer membership_counter;
	SortedMap<String,String> cluster_membership;
	ConcurrentSkipListMap<String,Integer> members_counters;
	InetSocketAddress group;
	NetworkInterface net_int;
	MulticastSocket multicast_socket;

	public MembershipService(String ip_mcast_addr, Integer ip_mcast_port, String id, String hashed_id, String folder_path) throws IOException {
		this.ip_mcast_addr = ip_mcast_addr;
		this.ip_mcast_port = ip_mcast_port;
		this.id = id;
		this.hashed_id = hashed_id;
		this.folder_path = folder_path;
		this.membership_counter = -1;
		this.cluster_membership = new TreeMap<String,String>();
		this.members_counters = new ConcurrentSkipListMap<String,Integer>();
		this.multicast_socket = new MulticastSocket(this.ip_mcast_port);
		InetAddress mcast_addr = InetAddress.getByName(this.ip_mcast_addr);
		InetSocketAddress mcast_socket_addr = new InetSocketAddress(mcast_addr, this.ip_mcast_port);
		NetworkInterface net_int = NetworkInterface.getByInetAddress(mcast_addr);
		this.group = mcast_socket_addr;
		this.net_int = net_int;
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
		if(this.membership_counter%2==0)
			return;
		this.multicast_socket.joinGroup(this.group, this.net_int);
		this.membership_counter++;
		this.cluster_membership.put(this.hashed_id, this.id);
		this.members_counters.put(this.id, this.membership_counter);
		this.counterToFile();
		this.multicast("join");
        return;
    }

    public void leave() throws IOException {
		if(this.membership_counter%2!=0)
			return;
		this.multicast_socket.leaveGroup(this.group, this.net_int);
		this.membership_counter++;
		this.cluster_membership.clear();
		this.members_counters.clear();
		this.counterToFile();
		this.multicast("leave");
        return;
    }

	public void multicast(String op) throws IOException{
		String message = "";
		switch(op){
			case "join":
				message = "JOIN " + this.id + " " + this.membership_counter;
				break;
			case "leave":
				message = "LEAVE " + this.id;
				break;
			case "view":
				message = "VIEW";
				for(Map.Entry<String,Integer> entry : this.members_counters.entrySet())
					message += " " + entry.getKey() + " " + entry.getValue();
				break;
		}
		byte[] buffer = message.getBytes();
		Integer lenght = buffer.length;
		DatagramPacket dp = new DatagramPacket(buffer, lenght, InetAddress.getByName(this.ip_mcast_addr), this.ip_mcast_port);
		this.multicast_socket.send(dp);
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

	public void add(String id, Integer counter){
		this.cluster_membership.put(Hashing.hash(id), id);
		this.members_counters.put(id, counter);
	}

	public void del(String id){
		this.cluster_membership.remove(Hashing.hash(id));
		this.members_counters.remove(id);
	}
}
