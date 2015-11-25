package project.service.campaign;

import project.service.account.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by bjornbjarnst on 10/28/15.
 */
public class Campaign {
	private String name;
	private Vector<User> players;
	private Vector<User> DMs;
	private Map<String, String> imagePaths; // Map<[Image title], [Image path]>
	private String notes;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<User> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<User> players) {
		this.players = players;
	}

	public Vector<User> getDMs() {
		return DMs;
	}

	public void setDMs(Vector<User> DMs) {
		this.DMs = DMs;
	}

	public Map<String, String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(Map<String, String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
