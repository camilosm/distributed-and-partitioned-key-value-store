package src;

import java.io.*;
import java.util.*;

public class MembershipService {
	String id;
	Integer membership_counter;
	HashMap<String,Integer> cluster_membership;

	public MembershipService(String id) {
		this.id = id;
		this.membership_counter = 0;
		this.cluster_membership = new HashMap<String,Integer>();
		// for testing
		this.cluster_membership.put(this.id, this.membership_counter);
		this.cluster_membership.put("127.0.0.2", this.membership_counter);
		this.cluster_membership.put("127.0.0.3", this.membership_counter);
	}

	public void join() {
        return;
    }

    public void leave() {
        return;
    }

	public void view(DataOutputStream dos) throws IOException {
		dos.writeInt(this.cluster_membership.size());
		for(Map.Entry<String,Integer> entry : this.cluster_membership.entrySet()){
			dos.writeUTF(entry.getKey());
			dos.writeInt(entry.getValue());
		}
		return;
	}

}
