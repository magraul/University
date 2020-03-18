import me.services.config.ApplicationContext;
import me.structures.AnUniversitar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnUniversitarTest {

    AnUniversitar an = new AnUniversitar(ApplicationContext.getPROPERTIES().getProperty("data.structure.anUniversitar"));
    @Test
    void getCurentWeek() {
        assertEquals(an.getCurentWeek(), 9);
    }
}
