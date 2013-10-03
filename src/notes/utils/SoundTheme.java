package notes.utils;

import lombok.Getter;

/**
 * The enumeration of available sound themes.
 *
 * @author Rui Du
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

    /**
     * Gets the corresponding instance of {@code SoundTheme} that has the provided description
     * message.
     *
     * @param description The description message of the sound theme.
     * @return {@code SoundTheme} The instance of {@code SoundTheme}.
     */
    public static SoundTheme getSoundThemeFromDescription(String description) {
        for (SoundTheme theme : SoundTheme.values()) {
            if (theme.getDescription().equals(description)) {
                return theme;
            }
        }
        return null;
    }
}
