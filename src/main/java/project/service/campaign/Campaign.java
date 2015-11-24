package project.service.campaign;

import project.service.account.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by whiskeyjack on 10/28/15.
 */
public class Campaign {
	public String name;
	public Vector<User> players;
	public Vector<User> DMs;
	public Map<String, String> imagePaths; // Map<[Image title], [Image path]>
	public String notes;

	public Campaign(String name) {
		this.name = name;
		this.players = new Vector<>();
		this.DMs = new Vector<>();
		this.imagePaths = new HashMap<>();
		this.notes = "";
	}

	public Campaign() {
		this("");
	}

	public void addPlayer(User user) {
		this.players.add(user);
	}

	public void addDM(User user) {
		this.players.add(user);
	}

	public void addImage(String name, String path) {
		this.imagePaths.put(name, path);
	}
}
