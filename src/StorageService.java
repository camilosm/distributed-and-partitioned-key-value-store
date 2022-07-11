package src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class StorageService {
	private final String FOLDER_PREFIX = "store_";
	private ArrayList<String> key_store;
	private String id;
	private String folder;

	public StorageService(String id){
		this.key_store = new ArrayList<>();
		this.id = id;
		this.folder = FOLDER_PREFIX + this.id + "/";
		File storage = new File(this.folder);
		if(!storage.exists())
			storage.mkdirs();
	}

	public void put(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
		String key = dis.readUTF();
		OutputStream os = new FileOutputStream(this.folder + key);
		long size = dis.readLong();
        byte[] buffer = new byte[1024];
		int bytes_read;

        while (size > 0 && (bytes_read = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1){
            os.write(buffer, 0, bytes_read);
            size -= bytes_read;
        }

        dis.close();
		os.close();
		this.key_store.add(key);
		return;
    }

	public String get(Integer key) {
        return "";
    }

	public void delete(Integer key) {
        return;
    }

	public void run() {

	}
}
