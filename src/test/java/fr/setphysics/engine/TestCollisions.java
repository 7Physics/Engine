package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.common.geom.shape.Cuboid;
import fr.setphysics.common.geom.shape.Sphere;
import fr.setphysics.renderer.Camera;
import fr.setphysics.renderer.Object3D;
import fr.setphysics.renderer.Scene3D;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Random;
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
    public void visualCollisionTest() {
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
        world.addGravity(new Vec3(0, -1, 0));

        PhysicObject ground = new PhysicObject(new Cuboid(10, 10, 2), new Position(0, -1, 0));
        ground.setDynamic(false);
        world.addPhysicObject(ground);

        PhysicObject mur = addCube(new Vec3(0, .15, -1), new Vec3(0, 0, 0), new Vec3(3, 2, .1), Color.GRAY);
        mur.setDynamic(false);

//        addCube(new Vec3(0, 1, 1), new Vec3(0, 0, 0), Color.RED);

//        addCube(new Vec3(0, .1, 1), new Vec3(0, 0, -1), Color.RED);
//        addCube(new Vec3(0, .1, -1), new Vec3(0, 0, 1));
//
//        addCube(new Vec3(1, .1, 0), new Vec3(-1, 0, 0), Color.RED);
//        addCube(new Vec3(-1, .1, 0), new Vec3(1, 0, 0));

//        addCube(new Vec3(0, 1, 0), new Vec3(0, 0, 0));

//        addCube(new Vec3(1, 1, 1), new Vec3(-1, 0, -1));
//        addCube(new Vec3(-1, 1, -1), new Vec3(1, 0, 1));
//        addCube(new Vec3(1, 1, -1), new Vec3(-1, 0, 1));
//        addCube(new Vec3(-1, 1, 1), new Vec3(1, 0, -1));

//        addCube(new Vec3(1, 1, 1), new Vec3(0, 0, -8));
//        addCube(new Vec3(0, 1, 1), new Vec3(0, 0, -8));
//        addCube(new Vec3(-1, 1, 1), new Vec3(0, 0, -8));

        for(int i = 0; i < 5; i++) {
            addSphere(new Vec3(0, i+2, 0), new Vec3(0, 0, 0));
            addSphere(new Vec3(0, i+7, .05), new Vec3(0, 0, 0));
        }

        Random r = new Random();

        for(int i = 0; i < 100; i++) {
            addSphere(new Vec3(r.nextFloat()*6-3, i/3f, r.nextFloat()*6-3), new Vec3(r.nextFloat()*4-2, 0, r.nextFloat()*4-2));
        }

        final long[] start = {System.currentTimeMillis()};

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                world.step(System.currentTimeMillis() - start[0]);
                start[0] = System.currentTimeMillis();
//                System.out.println("Collides : "+cube.collideWith(ground));
            }
        }, 0L, 1000/60L);

        new CompletableFuture<>().join();
    }

    private PhysicObject addSphere(Vec3 position, Vec3 force) {
        Position pos = new Position(position);
        Object3D object3D = new Object3D(new Sphere(.1, 2), pos, Color.magenta);
        scene3D.addObject(object3D);

        PhysicObject sphere1 = new PhysicObject(new Sphere(.1, 1), pos);
        sphere1.addForce(force);

        world.addPhysicObject(sphere1);
        return sphere1;
    }

    private PhysicObject addCube(Vec3 position, Vec3 force) {
        return addCube(position, force, new Vec3(.1, .1, .1), Color.GRAY);
    }

    private PhysicObject addCube(Vec3 position, Vec3 force, Color color) {
        return addCube(position, force, new Vec3(.1, .1, .1), color);
    }

    private PhysicObject addCube(Vec3 position, Vec3 force, Vec3 dim, Color color) {
        Position pos = new Position(position);
        Object3D object3D = new Object3D(new Cuboid(dim.getX(), dim.getZ(), dim.getY()), pos, color, Color.WHITE);
        scene3D.addObject(object3D);

        PhysicObject cube1 = new PhysicObject(new Cuboid(dim.getX(), dim.getZ(), dim.getY()), pos);
        cube1.addForce(force);

        world.addPhysicObject(cube1);
        return cube1;
    }
}
