package fr.setphysics.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Shape;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.common.geom.shape.Sphere;

public class TestPhysicBody {
	private static final double epsilon = 0.001;
	Position pos;
	Vec3 forceX, forceY, forceZ, forceXYZ;
	PhysicObject po;
	Shape s = new Sphere(1,1);
	
	@BeforeEach
	public void setUp() {
		pos = new Position(new Vec3(0,0,0));
		po = new PhysicObject(s, pos);
		forceX = new Vec3(2,0,0);
		forceY = new Vec3(0,3,0);
		forceZ = new Vec3(0,0,5);
		forceXYZ = new Vec3(2,6,3);
	}
	
	@Test
	public void positionTestX() {
		po.addForce(forceX);
		po.update(0);
		assertEquals(0,po.getPosition().getCoords().getX(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(0.1,po.getPosition().getCoords().getX(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(0.4,po.getPosition().getCoords().getX(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(0.9,po.getPosition().getCoords().getX(), epsilon);
	}
	
	@Test
	public void positionTestY() {
		po.addForce(forceY);
		po.update(0);
		//Cas de collision avec le sol
		assertEquals(1,po.getPosition().getCoords().getY(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(1.15,po.getPosition().getCoords().getY(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(1.6,po.getPosition().getCoords().getY(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(2.35,po.getPosition().getCoords().getY(), epsilon);
	}
	
	@Test
	public void positionTestZ() {
		po.addForce(forceZ);
		po.update(0);
		assertEquals(0,po.calculatePosition(0).getCoords().getZ(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(0.25,po.getPosition().getCoords().getZ(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(1.0,po.getPosition().getCoords().getZ(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(2.25,po.getPosition().getCoords().getZ(), epsilon);
	}
	
	@Test
	public void positionTestXYZ() {
		po.addForce(forceXYZ);
		po.update(0);
		assertEquals(0,po.getPosition().getCoords().getX(),epsilon);
		//Cas de collision -> le y est modifié
		assertEquals(1,po.getPosition().getCoords().getY(),epsilon);
		assertEquals(0,po.getPosition().getCoords().getZ(),epsilon);
		//time = 1
		po.update(1);
		assertEquals(0.1,po.getPosition().getCoords().getX(),epsilon);
		assertEquals(1.3,po.getPosition().getCoords().getY(),epsilon);
		assertEquals(0.15,po.getPosition().getCoords().getZ(),epsilon);
		//time = 2
		po.update(1);
		assertEquals(0.4,po.getPosition().getCoords().getX(),epsilon);
		assertEquals(2.2,po.getPosition().getCoords().getY(),epsilon);
		assertEquals(0.6,po.getPosition().getCoords().getZ(),epsilon);
		//time = 3
		po.update(1);
		assertEquals(0.9,po.getPosition().getCoords().getX(),epsilon);
		assertEquals(3.7,po.getPosition().getCoords().getY(),epsilon);
		assertEquals(1.35,po.getPosition().getCoords().getZ(),epsilon);
	}
	
	@Test
	public void speedTestX() {
		po.addForce(forceX);
		po.update(0);
		assertEquals(0,po.getSpeed().getX(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(0.2,po.getSpeed().getX(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(0.4,po.getSpeed().getX(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(0.6,po.getSpeed().getX(), epsilon);
	}
	
	@Test
	public void speedTestY() {
		po.addForce(forceY);
		po.update(0);
		assertEquals(0,po.getSpeed().getY(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(.3,po.getSpeed().getY(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(.6,po.getSpeed().getY(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(.9,po.getSpeed().getY(), epsilon);
	}
	
	@Test
	public void speedTestZ() {
		po.addForce(forceZ);
		po.update(0);
		assertEquals(0,po.getSpeed().getZ(),epsilon);
		// time = 1
		po.update(1);
		assertEquals(.5,po.getSpeed().getZ(), epsilon);
		// time = 2
		po.update(1);
		assertEquals(1.0,po.getSpeed().getZ(), epsilon);
		// time = 3
		po.update(1);
		assertEquals(1.5,po.getSpeed().getZ(), epsilon);
	}
	
	@Test
	public void speedTestXYZ() {
		po.addForce(forceXYZ);
		po.update(0);
		assertEquals(0,po.getSpeed().getX(),epsilon);
		assertEquals(0,po.getSpeed().getY(),epsilon);
		assertEquals(0,po.getSpeed().getZ(),epsilon);
		//time = 1
		po.update(1);
		assertEquals(.2,po.getSpeed().getX(),epsilon);
		assertEquals(.6,po.getSpeed().getY(),epsilon);
		assertEquals(.3,po.getSpeed().getZ(),epsilon);
		//time = 2
		po.update(1);
		assertEquals(.4,po.getSpeed().getX(),epsilon);
		assertEquals(1.2,po.getSpeed().getY(),epsilon);
		assertEquals(.6,po.getSpeed().getZ(),epsilon);
		//time = 3
		po.update(1);
		assertEquals(.6,po.getSpeed().getX(),epsilon);
		assertEquals(1.8,po.getSpeed().getY(),epsilon);
		assertEquals(.9,po.getSpeed().getZ(),epsilon);
	}

}
