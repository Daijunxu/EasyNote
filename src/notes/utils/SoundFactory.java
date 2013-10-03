package notes.utils;

import notes.data.cache.Property;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class responsible for playing sound effects.
 *
 * @author Rui Du
 */
public class SoundFactory {

    private static AudioPlayer player = AudioPlayer.player;
    private static Map<String, Map<String, String>> soundMap;

    static {
        soundMap = new HashMap<String, Map<String, String>>();

        Map<String, String> outlook = new HashMap<String, String>();
        outlook.put("delete", "./resources/sound/outlook/delete.wav");
        outlook.put("error", "./resources/sound/outlook/error.wav");
        outlook.put("export", "./resources/sound/outlook/export.wav");
        outlook.put("navigation", "./resources/sound/outlook/navigation.wav");
        outlook.put("notify", "./resources/sound/outlook/notify.wav");
        outlook.put("off", "./resources/sound/outlook/off.wav");
        outlook.put("on", "./resources/sound/outlook/on.wav");
        outlook.put("popup", "./resources/sound/outlook/popup.wav");
        outlook.put("update", "./resources/sound/outlook/update.wav");
        soundMap.put(SoundTheme.OUTLOOK.getDescription(), outlook);

        Map<String, String> windows = new HashMap<String, String>();
        windows.put("delete", "./resources/sound/windows/delete.wav");
        windows.put("error", "./resources/sound/windows/error.wav");
        windows.put("export", "./resources/sound/windows/export.wav");
        windows.put("navigation", "./resources/sound/windows/navigation.wav");
        windows.put("notify", "./resources/sound/windows/notify.wav");
        windows.put("off", "./resources/sound/windows/off.wav");
        windows.put("on", "./resources/sound/windows/on.wav");
        windows.put("popup", "./resources/sound/windows/popup.wav");
        windows.put("update", "./resources/sound/windows/update.wav");
        soundMap.put(SoundTheme.WINDOWS.getDescription(), windows);
    }

    /**
     * Plays the sound when deleting something.
     */
    public static void playDelete() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("delete")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when showing an error message.
     */
    public static void playError() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("error")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when exporting something.
     */
    public static void playExport() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("export")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when navigating to somewhere else.
     */
    public static void playNavigation() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("navigation")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when showing an notification message.
     */
    public static void playNotify() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("notify")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when exiting the program.
     */
    public static void playOff() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("off")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when starting the program.
     */
    public static void playOn() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("on")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when showing a new dialog.
     */
    public static void playPopup() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("popup")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound when updating something.
     */
    public static void playUpdate() {
        try {
            AudioStream stream = new AudioStream(new FileInputStream(soundMap.get(
                    Property.get().getSoundTheme()).get("update")));
            player.start(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
