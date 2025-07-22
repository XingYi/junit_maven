package com.sddevops.junit_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SongCollectionTest {

	private SongCollection sc;
	private Song s1;
	private Song s2;
	private Song s3;
	private Song s4;
	private final int SONG_COLLECTION_SIZE = 4;
	private SongCollection sc_with_size;
	private SongCollection sc_with_size_1;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		sc = new SongCollection();
		s1 = new Song("001", "good 4 u", "Olivia Rodrigo", 3.59);
		s2 = new Song("002", "Peaches", "Justin Bieber", 3.18);
		s3 = new Song("003", "MONTERO", "Lil Nas", 2.3);
		s4 = new Song("004", "bad guy", "billie eilish", 3.14);
		sc.addSong(s1);
		sc.addSong(s2);
		sc.addSong(s3);
		sc.addSong(s4);
		sc_with_size = new SongCollection(5);
		sc_with_size_1 = new SongCollection(1);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		sc = null;
		sc_with_size = null;
		sc_with_size_1 = null;

	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#getSongs()}.
	 */
	@Test
	void testGetSongs() {
		List<Song> testSc = sc.getSongs();
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#addSong(com.sddevops.junit_maven.eclipse.Song)}.
	 */
	@Test
	void testAddSong() {
		List<Song> testSc = sc.getSongs();
		// Assert that Song Collection is equals to Song Collection Size : 4
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
		// Act
		sc.addSong(s1);
		// Assert that Song Collection is equals to Song Collection Size + 1 : 5
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE + 1);

		sc_with_size_1.addSong(s1);
		sc_with_size_1.addSong(s2);
		sc_with_size_1.addSong(s3);
		assertEquals(sc_with_size_1.getSongs().size(), 1);

	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#sortSongsByTitle()}.
	 */
	@Test
	void testSortSongsByTitle() {
		List<Song> sortedSongList = sc.sortSongsByTitle();
		assertEquals(sortedSongList.get(0).getTitle(), "MONTERO");
		assertEquals(sortedSongList.get(1).getTitle(), "Peaches");
		assertEquals(sortedSongList.get(2).getTitle(), "bad guy");
		assertEquals(sortedSongList.get(3).getTitle(), "good 4 u");

	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#sortSongsBySongLength()}.
	 */
	@Test
	void testSortSongsBySongLength() {
		List<Song> sortedSongByLengthList = sc.sortSongsBySongLength();
		assertEquals(sortedSongByLengthList.get(0).getSongLength(), 3.59);
		assertEquals(sortedSongByLengthList.get(1).getSongLength(), 3.18);
		assertEquals(sortedSongByLengthList.get(2).getSongLength(), 3.14);
		assertEquals(sortedSongByLengthList.get(3).getSongLength(), 2.3);

	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#findSongsById(java.lang.String)}.
	 */
	@Test
	void testFindSongsById() {
		Song song = sc.findSongsById("004");
		assertEquals(song.getArtiste(), "billie eilish");
		assertNull(sc.findSongsById("doesnt exist"));

	}

	/**
	 * Test method for
	 * {@link com.sddevops.junit_maven.eclipse.SongCollection#findSongByTitle(java.lang.String)}.
	 */
	@Test
	void testFindSongByTitle() {
		Song song = sc.findSongByTitle("MONTERO");
		assertEquals(song.getArtiste(), "Lil Nas");
		assertNull(sc.findSongByTitle("doesnt exist"));

	}

	@Test
	public void testFetchSongOfTheDay() {
		String mockJson = """
						{
							"id": "001",
							"title": "Mock Song",
							"artiste": "Mock Artist",
							"songLength": 4.25
						}
				""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);

		assertEquals("001", result.getId());
		assertEquals("Mock Song", result.getTitle());
		assertEquals("Mock Artist", result.getArtiste());
		assertEquals(4.25, result.getSongLength());
	}

	@Test
	public void testInvalidFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doReturn(null).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNull(result);
	}

	@Test
	public void testExceptionHandlingInFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doThrow(new RuntimeException("API failed")).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNull(result);
		assertEquals(collection.getSongs().size(), 0);
	}

	@Test
	public void testFetchSongOfTheDayTaylorSwift() {
		// This test ensures that when the artiste is "Taylor Swift," the artiste name
		// is changed to "TS" and that the song is added to the collection.
		// Mock the response for Taylor Swift's song
		String mockJson = """
				        {
				            "id": "005",
				            "title": "Love Story",
				            "artiste": "Taylor Swift",
				            "songLength": 3.55
				        }
				""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		// Assert the song details
		assertNotNull(result);
		assertEquals("005", result.getId());
		assertEquals("Love Story", result.getTitle());
		assertEquals("TS", result.getArtiste()); // Artiste should be changed to "TS"
		assertEquals(3.55, result.getSongLength());

		// Assert that the song was added to the collection
		assertEquals(1, collection.getSongs().size()); // Only 1 song in the collection
	}

	@Test
	public void testFetchSongOfTheDayBrunoMars() {
		// This test ensures that when the artiste is "Bruno Mars," the artiste name is
		// not modified and the song is added to the collection.
		// Mock the response for Bruno Mars' song
		String mockJson = """
				        {
				            "id": "006",
				            "title": "Just the Way You Are",
				            "artiste": "Bruno Mars",
				            "songLength": 3.45
				        }
				""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		// Assert the song details
		assertNotNull(result);
		assertEquals("006", result.getId());
		assertEquals("Just the Way You Are", result.getTitle());
		assertEquals("Bruno Mars", result.getArtiste()); // Artiste should remain "Bruno Mars"
		assertEquals(3.45, result.getSongLength());

		// Assert that the song was added to the collection
		assertEquals(1, collection.getSongs().size()); // Only 1 song in the collection
	}

	@Test
	public void testFetchSongOfTheDayNonMatchingArtist() {
		// This test ensures that songs from other artists (neither "Taylor Swift" nor
		// "Bruno Mars") are not added to the collection.
		// Mock the response for a song by a non-specific artiste
		String mockJson = """
				        {
				            "id": "007",
				            "title": "Blinding Lights",
				            "artiste": "The Weeknd",
				            "songLength": 3.20
				        }
				""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		// Assert the song details
		assertNotNull(result);
		assertEquals("007", result.getId());
		assertEquals("Blinding Lights", result.getTitle());
		assertEquals("The Weeknd", result.getArtiste()); // Artiste remains unchanged
		assertEquals(3.20, result.getSongLength());

		// Assert that the song was NOT added to the collection
		assertEquals(0, collection.getSongs().size()); // Collection should still be empty
	}

	@Test
	public void testFetchSongOfTheDayMalformedJson() {
		// This test ensures that if the API returns invalid or malformed JSON, it does
		// not crash and gracefully handles the error.
		// Mock malformed JSON
		String mockJson = """
				        {
				            "id": "008",
				            "title": "Unknown Song",
				            "artiste": "Unknown Artist"
				"""; // Missing "songLength"

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		// Assert that no song is created due to invalid JSON
		assertNull(result);
		assertEquals(0, collection.getSongs().size()); // Collection should still be empty
	}

	@Test
	public void testFetchSongOfTheDayCollectionCapacity() {
		// This test ensures that the collection does not exceed its specified capacity
		// when a new song is added.
		// Mock the response for a valid song
		String mockJson = """
				        {
				            "id": "009",
				            "title": "New Song",
				            "artiste": "New Artist",
				            "songLength": 3.50
				        }
				""";

		SongCollection collection = spy(new SongCollection(3)); // Capacity set to 3
		doReturn(mockJson).when(collection).fetchSongJson();

		// Add songs to reach the collection's capacity
		collection.addSong(new Song("001", "Song 1", "Artist 1", 3.0));
		collection.addSong(new Song("002", "Song 2", "Artist 2", 3.5));
		collection.addSong(new Song("003", "Song 3", "Artist 3", 4.0));

		// Attempt to add a 4th song, which should be prevented
		Song result = collection.fetchSongOfTheDay();

		// Assert the result and that no more songs were added
		assertNotNull(result); // Ensure a song was created
		assertEquals(3, collection.getSongs().size()); // Collection size should still be 3
	}

}
