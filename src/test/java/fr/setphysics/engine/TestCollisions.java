package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.shape.Cuboid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestCollisions {
    @Test
    public void cubeCollideTest() {
        PhysicObject a = new PhysicObject(new Cuboid(2, 2, 2), new Position(0, 0, 0));
        PhysicObject b = new PhysicObject(new Cuboid(2, 2, 2), new Position(2, 0, 0));
        PhysicObject c = new PhysicObject(new Cuboid(2, 2, 2), new Position(3, 0, 0));

        assertTrue(a.collideWith(b));
        assertFalse(a.collideWith(c));

        assertTrue(b.collideWith(c));
    }
}
