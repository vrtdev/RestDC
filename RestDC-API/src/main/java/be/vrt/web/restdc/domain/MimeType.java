package be.vrt.web.restdc.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * MimeType holding class. You should first check the constants of this class to check if your required mimetype isn't
 * already available. If it is not available, then call the {@link be.vrt.web.restdc.domain.MimeType#getMimeType(String,
 * String...)} method.
 * <p/>
 * This class keeps all requested mime types in memory, so requesting the same mime type will always return the same
 * instance.
 *
 * @author Mike Seghers
 */
public final class MimeType {

    /**
     * A map, serving as memory registry to store all mime-types ever requested.
     */
    private static final Map<String, MimeType> KNOWN_MIME_TYPES = new HashMap<>();

    public static final MimeType APPLICATION_JSON = getMimeType("application/json", "json");
    public static final MimeType APPLICATION_XML = getMimeType("application/xml", "xml");
    public static final MimeType APPLICATION_ZIP = getMimeType("application/zip", "zip");
    public static final MimeType APPLICATION_PDF = getMimeType("application/pdf", "pdf");
    public static final MimeType APPLICATION_JS = getMimeType("application/javascript", "js");

    public static final MimeType IMAGE_PNG = getMimeType("image/png", "png");
    public static final MimeType IMAGE_JPEG = getMimeType("image/jpeg", "jpg", "jpeg");
    public static final MimeType IMAGE_GIF = getMimeType("image/gif", "gif");
    public static final MimeType IMAGE_TIFF = getMimeType("image/tiff", "tiff");
    public static final MimeType IMAGE_SVG = getMimeType("image/svg+xml", "svg");

    public static final MimeType TEXT_HTML = getMimeType("text/html", "html", "htm");
    public static final MimeType TEXT_CSS = getMimeType("text/css", "css");

    /**
     * The internet media type string for this MimeType
     */
    private String internetMediaType;

    /**
     * The file extension(s) for this MimeType
     */
    private String[] fileExtensions;

    /**
     * Private constructor (construction should happen via the {@link #getMimeType(String, String...)} method.
     *
     * @param internetMediaType the internet media type string for this MimeType
     * @param fileExtensions    the file extension(s) for this MimeType
     */
    private MimeType(final String internetMediaType, final String... fileExtensions) {
        if (internetMediaType == null || internetMediaType.trim().length() == 0) {
            throw new IllegalArgumentException("You should provide a valid internet media type for a mime type!");
        }

        if (fileExtensions == null) {
            this.fileExtensions = new String[0];
        } else {
            this.fileExtensions = fileExtensions;
        }

        this.internetMediaType = internetMediaType;

    }

    /**
     * Return an already known MimeType for the given internet media type. If the given type is unknown, this method
     * returns null.
     *
     * @param internetMediaType the internet media type
     * @return an already known MimeType
     */
    public static MimeType getMimeType(final String internetMediaType, final String... fileExtensions) {
        MimeType result = KNOWN_MIME_TYPES.get(internetMediaType);
        if (result == null) {
            result = new MimeType(internetMediaType, fileExtensions);
            KNOWN_MIME_TYPES.put(internetMediaType, result);
        }
        return result;
    }

    /**
     * Get the internet media type string for this MimeType
     *
     * @return the internet media type
     */
    public String getInternetMediaType() {
        return internetMediaType;
    }

    /**
     * Get the preferred file extension for this MimeType
     *
     * @return the preferred file extension
     */
    public String getPreferredFileExtension() {
        return fileExtensions[0];
    }

    @Override
    public String toString() {
        return "MimeType{" +
                "internetMediaType='" + internetMediaType + '\'' +
                ", fileExtensions=" + Arrays.toString(fileExtensions) +
                '}';
    }
}
