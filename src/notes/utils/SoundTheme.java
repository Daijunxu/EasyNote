package notes.utils;

import lombok.Getter;

/**
 * The enumeration of available sound themes.
 *
 * Author: Rui Du
 */
public enum SoundTheme {

    NONE("No Sound"), OUTLOOK("Windows Outlook"), WINDOWS("Windows Default");
    @Getter
    private String description;

    /**
     * Creates an instance of {@code SoundTheme}.
     *
     * @param description The description message of the sound theme.
     */
    private SoundTheme(String description) {
        this.description = description;
    }
}
