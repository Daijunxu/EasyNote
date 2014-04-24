package notes.businessobjects.workset;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration type to indicate the status of a worksheet.
 * <p/>
 * Author: Rui Du
 * Date: 4/23/14
 * Time: 2:20 AM
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WorksheetStatus {
    ACTIVE("Active"),
    COMPLETED("Completed");

    @Getter
    private final String description;

}
