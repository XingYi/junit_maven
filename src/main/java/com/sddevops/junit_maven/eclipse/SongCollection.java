package com.sddevops.junit_maven.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class SongCollection {

	private ArrayList<Song> songs = new ArrayList<>();
	private int capacity;

	public SongCollection() {
		/*
		 * songs.add(new Song("0001","good 4 u","Olivia Rodrigo",3.59)); songs.add(new
		 * Song("0002","Peaches","Justin Bieber",3.18)); songs.add(new
		 * Song("0003","MONTERO","Lil Nas X",2.3)); songs.add(new
		 * Song("0004","bad guy","Billie Eilish",3.14));
		 */

		this.capacity = 20;
	}

	public SongCollection(int capacity) {
		this.capacity = capacity;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void addSong(Song song) {
		if (songs.size() != capacity) {
			songs.add(song);
		}
	}

	public ArrayList<Song> sortSongsByTitle() {
		Collections.sort(songs, Song.titleComparator);
		return songs;
	}

	public ArrayList<Song> sortSongsBySongLength() {
		Collections.sort(songs, Song.songLengthComparator);
		return songs;
	}

	public Song findSongsById(String id) {
		for (Song s : songs) {
			if (s.getId().equals(id))
				return s;
		}
		return null;
	}

	public Song findSongByTitle(String title) {
		for (Song s : songs) {
			if (s.getTitle().equals(title))
				return s;
		}
		return null;
	}

	protected String fetchSongJson() {
		String urlString = "https://mocki.io/v1/36c94419-b141-4cfd-96fa-327f4872aca6";
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder response = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Song fetchSongOfTheDay() {
		try {
			String jsonStr = fetchSongJson();
			if (jsonStr == null)
				return null;
			
			JSONObject json = new JSONObject(jsonStr);
			Song song = new Song(json.getString("id"), json.getString("title"), json.getString("artiste"),
					json.getDouble("songLength"));
			
			if (song.getArtiste().equals("Taylor Swift")) {
				song.setArtiste("TS");
				this.addSong(song);
			} else if (song.getArtiste().equals("Bruno Mars")) {
				this.addSong(song);
			}	
			
			return song;
	    } catch (JSONException e) {
	    	System.out.println("Malformed JSON received: " + e.toString());
	        return null;
		} catch (Exception e) {
			//e.printStackTrace(); //Note: This will cause error for Jenkin pipeline to build success
			System.out.println("Caught general exception: " + e.toString());
			return null;
		}
	}
}
