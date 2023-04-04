package plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;
import plugfest.tooling.translator.TranslatorCDX;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;
import java.util.*;


public class SBOMConflictTest {

    /**
     * Test SBOM 1 Constant Set
     */
    public static final String SB_SPEC_VER_ONE = "1";
    public static final String SB_SBOM_VER_ONE = "CYCLONEDX-1.4";
    public static final String SB_AUTHOR_ONE = "Anchore";
    public static final String SB_SERIAL_ONE = "f057a217-e332-4981-94dc-799d6a776f58";
    public static final String SB_TIMESTAMP_ONE = "2023-01-01T00:00:00-05:00";
    public static final Signature SB_SIGNATURE_ONE = null;

    /**
     * Test SBOM 2 Constants
     */
    public static final String SB_SPEC_VER_TWO = "2";
    public static final String SB_SBOM_VER_TWO = "CYCLONEDX-1.3";
    public static final String SB_AUTHOR_TWO = "Anchor";
    public static final String SB_SERIAL_TWO = "e057a217-e332-4981-94dc-799d6a776f09";
    public static final String SB_TIMESTAMP_TWO = "2023-01-01T00:00:00-04:00";
    public static final Signature SB_SIGNATURE_TWO = null;

    /**
     * Result Constants
     */
    public static final int EXPECTED_CONFLICTS_SBOM_ONE_SBOM_TWO = 5;



    /**
     * Test SBOM 3 Constants
     */

    @Test
    public void two_SBOMs_with_conflicts_test() {

        SBOM a = new SBOM(
                SBOMType.CYCLONE_DX,
                SB_SPEC_VER_ONE,
                SB_SBOM_VER_ONE,
                SB_AUTHOR_ONE,
                SB_SERIAL_ONE,
                SB_TIMESTAMP_ONE,
                Collections.singleton(SB_SIGNATURE_ONE),
                new DependencyTree());

        SBOM b = new SBOM(
                SBOMType.CYCLONE_DX,
                SB_SPEC_VER_TWO,
                SB_SBOM_VER_TWO,
                SB_AUTHOR_TWO,
                SB_SERIAL_TWO,
                SB_TIMESTAMP_TWO,
                Collections.singleton(SB_SIGNATURE_TWO),
                new DependencyTree());

        SBOMConflict test_conflict = new SBOMConflict(a, b);

        assertNotNull(test_conflict);

        assertEquals(EXPECTED_CONFLICTS_SBOM_ONE_SBOM_TWO, test_conflict.getConflicts().size());

    }

}
