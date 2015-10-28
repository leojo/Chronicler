package project.service.campaign;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by whiskeyjack on 10/28/15.
 */
public class Campaign {
	public String name;
	public Vector<String> players;
	public Vector<String> DMs;
	public Map<String, String> imagePaths; // Map<[Image title], [Image path]>

	public Campaign(String name) {
		this.name = name;
		this.players = new Vector<>();
		this.DMs = new Vector<>();
		this.imagePaths = new HashMap<>();
	}

	public Campaign() {
		this("");
	}

	public void addPlayer(String username) {
		this.players.add(username);
	}

	public void addDM(String username) {
		this.players.add(username);
	}

	public void addImage(String name, String path) {
		this.imagePaths.put(name, path);
	}
}
