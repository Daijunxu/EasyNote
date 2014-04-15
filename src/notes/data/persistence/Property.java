package notes.data.persistence;

import lombok.Getter;
import lombok.Setter;

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
 * Author: Rui Du
 */
public class Property {
    /**
     * The single instance that is used in the system.
     */
    private static final Property instance = new Property();
    /**
     * The list of document types.
     */
    @Getter
    private final List<String> documentTypes;
    /**
     * The location of data file.
     */
    @Getter
    @Setter
    private String xmlDataLocation;
    /**
     * The name of current sound theme.
     */
    @Getter
    @Setter
    private String soundThemeName;
    /**
     * Whether to open the last document when program starts.
     */
    @Setter
    @Getter
    private boolean showLastDocumentOnOpening;
    /**
     * The id of last opened document.
     */
    @Getter
    @Setter
    private Long lastOpenedDocumentId;

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
        setXmlDataLocation(PROPERTIES.getProperty("xmlDataLocation"));
        documentTypes = Arrays.asList(PROPERTIES.getProperty("documentType").split(","));
        setSoundThemeName(PROPERTIES.getProperty("soundTheme"));

        if (PROPERTIES.getProperty("showLastDocumentOnOpening") != null) {
            setShowLastDocumentOnOpening(PROPERTIES.getProperty("showLastDocumentOnOpening").equals("true"));
        } else {
            setShowLastDocumentOnOpening(false);
        }

        if (PROPERTIES.getProperty("lastOpenedDocumentId") != null) {
            setLastOpenedDocumentId(Long.parseLong(PROPERTIES.getProperty("lastOpenedDocumentId")));
        }
    }

    /**
     * Gets the instance of {@code Property}.
     *
     * @return {@code Property} The instance of {@code Property}.
     */
    public static Property get() {
        return instance;
    }

    public void saveProperty() {
        Properties PROPERTIES = new Properties();
        PROPERTIES.setProperty("xmlDataLocation", getXmlDataLocation());

        StringBuilder sb = new StringBuilder();
        if (!getDocumentTypes().isEmpty()) {
            for (String type : getDocumentTypes()) {
                sb.append(type);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        PROPERTIES.setProperty("documentType", sb.toString());

        PROPERTIES.setProperty("soundTheme", getSoundThemeName());
        PROPERTIES.setProperty("showLastDocumentOnOpening", String.valueOf(isShowLastDocumentOnOpening()));

        if (getLastOpenedDocumentId() != null) {
            PROPERTIES.setProperty("lastOpenedDocumentId", getLastOpenedDocumentId().toString());
        }

        try {
            OutputStream output = new FileOutputStream("./Config.xml");
            PROPERTIES.storeToXML(output, "Updated on " + (new Date(System.currentTimeMillis())),
                    "UTF-8");
            output.close();
        } catch (IOException e) {
            System.err.println("Error occurred writing Config.xml file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
