import org.hexanome.model.Intersection;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntersectionTest {
    static Intersection i1, i2, i3, i4, i5, i6, i7;

    @BeforeClass
    public static void setUp() {
        i1 = new Intersection(1.0, 1.0, 1);
        i2 = new Intersection(2.0, 2.0, 1);
        i3 = new Intersection(1.0, 1.0, 2);
        i4 = new Intersection(1.0, 2.0, 1);
        i5 = new Intersection(0.999, 1.0, 1);
        i6 = new Intersection(1.0, 1.0, 1);
        i7 = new Intersection(0.9999999, 1.0, 1);
    }

    @Test
    public void testIntersections() {
        assertNotEquals("i1 vs i2", i1, i2);
        assertNotEquals("i1 vs i3", i1, i3);
        assertNotEquals("i1 vs i4", i1, i4);
        assertNotEquals("i1 vs i5", i1, i5);
        assertEquals("i1 vs i6", i1, i6);
        assertNotEquals("i1 vs i7", i1, i7);
        assertNotEquals("i1 vs int", i1, 42);
    }
}
