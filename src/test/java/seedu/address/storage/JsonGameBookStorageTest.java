package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGameEntries.getTypicalGameBook;
import static seedu.address.testutil.TypicalGameEntries.POKER1;
import static seedu.address.testutil.TypicalGameEntries.BLACKJACK1;
import static seedu.address.testutil.TypicalGameEntries.DARTS1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.GameBook;
import seedu.address.model.ReadOnlyGameBook;

public class JsonGameBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonGameBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readGameBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readGameBook(null));
    }

    private java.util.Optional<ReadOnlyGameBook> readGameBook(String filePath) throws Exception {
        return new JsonGameBookStorage(Paths.get(filePath)).readGameBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readGameBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readGameBook("notJsonFormatGameBook.json"));
    }

    @Test
    public void readGameBook_invalidGameEntryGameBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readGameBook("invalidGameEntryGameBook.json"));
    }

    @Test
    public void readGameBook_invalidAndValidGameEntryGameBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readGameBook("invalidAndValidGameEntryGameBook.json"));
    }

    @Test
    public void readAndSaveGameBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempGameBook.json");
        GameBook original = getTypicalGameBook();
        JsonGameBookStorage jsonGameBookStorage = new JsonGameBookStorage(filePath);

        // Save in new file and read back
        jsonGameBookStorage.saveGameBook(original, filePath);
        ReadOnlyGameBook readBack = jsonGameBookStorage.readGameBook(filePath).get();
        assertEquals(original, new GameBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addGameEntry(BLACKJACK1);
        original.removeGameEntry(POKER1);
        jsonGameBookStorage.saveGameBook(original, filePath);
        readBack = jsonGameBookStorage.readGameBook(filePath).get();
        assertEquals(original, new GameBook(readBack));

        // Save and read without specifying file path
        original.addGameEntry(DARTS1);
        jsonGameBookStorage.saveGameBook(original); // file path not specified
        readBack = jsonGameBookStorage.readGameBook().get(); // file path not specified
        assertEquals(original, new GameBook(readBack));

    }

    @Test
    public void saveGameBook_nullGameBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveGameBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code gameBook} at the specified {@code filePath}.
     */
    private void saveGameBook(ReadOnlyGameBook gameBook, String filePath) {
        try {
            new JsonGameBookStorage(Paths.get(filePath))
                    .saveGameBook(gameBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveGameBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveGameBook(new GameBook(), null));
    }
}