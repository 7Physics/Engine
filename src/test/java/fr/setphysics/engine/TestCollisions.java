package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.common.geom.shape.Cuboid;
import fr.setphysics.renderer.Camera;
import fr.setphysics.renderer.Object3D;
import fr.setphysics.renderer.Scene3D;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

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

    private Scene3D scene3D;
    private World world;

    @Test
    public void visualCollisionTest() throws InterruptedException {
        JFrame frame = new JFrame("Test");
        Camera camera = new Camera(new Position(-3, 1, 0));
        camera.rotateVertical(-Math.PI/8);
        scene3D = new Scene3D(camera);
        scene3D.setPreferredSize(new Dimension(1000, 600));
        frame.add(scene3D);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        world = new World();

        addCube(new Vec3(1, .1, 1), new Vec3(-1, 0, -1));
        addCube(new Vec3(-1, .1, -1), new Vec3(1, 0, 1));

        long start = System.currentTimeMillis();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                world.step(System.currentTimeMillis()-start);
            }
        }, 0L, 1000/60L);

        new CompletableFuture<>().join();
    }

    private void addCube(Vec3 position, Vec3 force) {
        Position pos = new Position(position);
        Object3D object3D = new Object3D(new Cuboid(.1, .1, .1), pos);
        scene3D.addObject(object3D);

        PhysicObject cube1 = new PhysicObject(new Cuboid(.1, .1, .1), pos);
        cube1.addForce(force);

        world.addPhysicObject(cube1);
    }
}
