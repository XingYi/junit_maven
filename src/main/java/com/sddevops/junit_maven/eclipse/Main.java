package com.sddevops.junit_maven.eclipse;

public class Main {
	public static void main(String[] args) {
		SongCollection sc = new SongCollection(3);
		sc.fetchSongOfTheDay();
		System.out.println("Calling the API");
		System.out.println(sc.getSongs());
	}
}
