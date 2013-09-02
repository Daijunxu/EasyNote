/**
 * 
 */
package notes.data.cache;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * Stores the system's configuration information.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class Property {
	/**
	 * The location of data file.
	 */
	private String dataLocation;

	/**
	 * The list of document types.
	 */
	private final List<String> documentTypes;

	/**
	 * The default sound theme.
	 */
	private String soundTheme;

	/**
	 * The single instance that is used in the system.
	 */
	private static final Property instance = new Property();

	/**
	 * Gets the instance of {@code Property}.
	 * 
	 * @return {@code Property} The instance of {@code Property}.
	 */
	public static Property get() {
		return instance;
	}

	/**
	 * Constructs an instance of {@code Property}.
	 */
	private Property() {
		Properties PROPERTIES = new Properties();
		try {
			InputStream input = new FileInputStream("./Config.xml");
			PROPERTIES.loadFromXML(input);
			input.close();
		} catch (InvalidPropertiesFormatException e) {
			System.err.println("Error occurred loading Config.xml file: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDataLocation(PROPERTIES.getProperty("dataLocation"));
		documentTypes = Arrays.asList(PROPERTIES.getProperty("documentType").split(","));
		setSoundTheme(PROPERTIES.getProperty("soundTheme"));
	}

	/**
	 * Gets the data file location.
	 * 
	 * @return {@code String} The data file location.
	 */
	public String getDataLocation() {
		return dataLocation;
	}

	/**
	 * Gets the list of document types.
	 * 
	 * @return {@code List<String>} The list of document types.
	 */
	public List<String> getDocumentTypes() {
		return documentTypes;
	}

	/**
	 * Gets the default sound theme.
	 * 
	 * @return {@code String} The default sound theme.
	 */
	public String getSoundTheme() {
		return soundTheme;
	}

	public void saveProperty() {
		Properties PROPERTIES = new Properties();
		PROPERTIES.setProperty("dataLocation", getDataLocation());

		StringBuilder sb = new StringBuilder();
		if (getDocumentTypes().isEmpty() == false) {
			for (String type : getDocumentTypes()) {
				sb.append(type);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		PROPERTIES.setProperty("documentType", sb.toString());

		PROPERTIES.setProperty("soundTheme", getSoundTheme());

		try {
			OutputStream output = new FileOutputStream("./Config.xml");
			PROPERTIES.storeToXML(output, "Updated at " + (new Date(System.currentTimeMillis())),
					"UTF-8");
			output.close();
		} catch (IOException e) {
			System.err.println("Error occurred writing Config.xml file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Sets the data file location.
	 * 
	 * @param dataLocation
	 *            The data file location to set.
	 */
	public void setDataLocation(String dataLocation) {
		this.dataLocation = dataLocation;
	}

	/**
	 * Sets the default sound theme.
	 * 
	 * @param soundTheme
	 *            The default sound theme to set.
	 */
	public void setSoundTheme(String soundTheme) {
		this.soundTheme = soundTheme;
	}
}
