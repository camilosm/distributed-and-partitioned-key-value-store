package src;

import java.io.*;
import java.util.*;

public class StorageService {
	String id;
	String folder_path;
	ArrayList<String> key_store;

	public StorageService(String id, String folder_path){
		this.key_store = new ArrayList<String>();
		this.id = id;
		this.folder_path = folder_path;
	}

	public void put(String key, DataInputStream dis) throws IOException {
		OutputStream os = new FileOutputStream(this.folder_path + key);
		long size = dis.readLong();
        byte[] buffer = new byte[1024];
		int bytes_read;
        while (size > 0 && (bytes_read = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1){
            os.write(buffer, 0, bytes_read);
            size -= bytes_read;
        }
		os.close();
		if(!this.key_store.contains(key))
			this.key_store.add(key);
		return;
    }

	public boolean contains(String key){
		return this.key_store.contains(key);
	}

	public void get(String key, DataOutputStream dos) throws IOException {
		File file = new File(this.folder_path + key);
		byte[] byte_array = new byte[(int)file.length()];
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(byte_array, 0, byte_array.length);
		dos.writeLong(byte_array.length);
		dos.write(byte_array, 0, byte_array.length);
		dis.close();
    }

	public void delete(String key){
		File file = new File(this.folder_path + key);
		if(file.exists())
			file.delete();
		this.key_store.remove(key);
        return;
    }

	public void run() {

	}
}
