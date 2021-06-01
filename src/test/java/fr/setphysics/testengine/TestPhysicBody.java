package fr.setphysics.testengine;

import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.engine.PhysicObject;

public class TestPhysicBody {
	private static final double epsilon = 0.001;
	Position pos;
	Vec3 forceX, forceY, forceZ, forceXYZ;
	PhysicObject po;
	
	@BeforeEach
	public void setUp() {
		pos = new Position(new Vec3(0,0,0));
		po = new PhysicObject(null, pos);
		forceX = new Vec3(2,0,0);
		forceY = new Vec3(0,3,0);
		forceZ = new Vec3(0,0,5);
		forceXYZ = new Vec3(2,6,3);
	}
	
	@Test
	public void positionTestX() {
		po.addForce(forceX);
		assertEquals(0,po.calculatePosition(0).getCoords().getX(),epsilon);
		// time = 1
		assertEquals(1,po.calculatePosition(1).getCoords().getX(), epsilon);
		// time = 2
		assertEquals(4,po.calculatePosition(2).getCoords().getX(), epsilon);
		// time = 3
		assertEquals(9,po.calculatePosition(3).getCoords().getX(), epsilon);
	}
	
	@Test
	public void positionTestY() {
		po.addForce(forceY);
		assertEquals(0,po.calculatePosition(0).getCoords().getY(),epsilon);
		// time = 1
		assertEquals(1.5,po.calculatePosition(1).getCoords().getY(), epsilon);
		// time = 2
		assertEquals(6,po.calculatePosition(2).getCoords().getY(), epsilon);
		// time = 3
		assertEquals(13.5,po.calculatePosition(3).getCoords().getY(), epsilon);
	}
	
	@Test
	public void positionTestZ() {
		po.addForce(forceZ);
		assertEquals(0,po.calculatePosition(0).getCoords().getZ(),epsilon);
		// time = 1
		assertEquals(2.5,po.calculatePosition(1).getCoords().getZ(), epsilon);
		// time = 2
		assertEquals(10,po.calculatePosition(2).getCoords().getZ(), epsilon);
		// time = 3
		assertEquals(22.5,po.calculatePosition(3).getCoords().getZ(), epsilon);
	}
	
	@Test
	public void positionTestXYZ() {
		po.addForce(forceXYZ);
		assertEquals(0,po.calculatePosition(0).getCoords().getX(),epsilon);
		assertEquals(0,po.calculatePosition(0).getCoords().getY(),epsilon);
		assertEquals(0,po.calculatePosition(0).getCoords().getZ(),epsilon);
		//time = 1
		assertEquals(1,po.calculatePosition(1).getCoords().getX(),epsilon);
		assertEquals(3,po.calculatePosition(1).getCoords().getY(),epsilon);
		assertEquals(1.5,po.calculatePosition(1).getCoords().getZ(),epsilon);
		//time = 2
		assertEquals(4,po.calculatePosition(2).getCoords().getX(),epsilon);
		assertEquals(12,po.calculatePosition(2).getCoords().getY(),epsilon);
		assertEquals(6,po.calculatePosition(2).getCoords().getZ(),epsilon);
	}
	
	@Test
	public void speedTestX() {
		po.addForce(forceX);
		assertEquals(0,po.calculateSpeed(0).getX(),epsilon);
		// time = 1
		assertEquals(2,po.calculateSpeed(1).getX(), epsilon);
		// time = 2
		assertEquals(4,po.calculateSpeed(2).getX(), epsilon);
		// time = 3
		assertEquals(6,po.calculateSpeed(3).getX(), epsilon);
	}
	
	@Test
	public void speedTestY() {
		po.addForce(forceY);
		assertEquals(0,po.calculateSpeed(0).getY(),epsilon);
		// time = 1
		assertEquals(3,po.calculateSpeed(1).getY(), epsilon);
		// time = 2
		assertEquals(6,po.calculateSpeed(2).getY(), epsilon);
		// time = 3
		assertEquals(9,po.calculateSpeed(3).getY(), epsilon);
	}
	
	@Test
	public void speedTestZ() {
		po.addForce(forceZ);
		assertEquals(0,po.calculateSpeed(0).getZ(),epsilon);
		// time = 1
		assertEquals(5,po.calculateSpeed(1).getZ(), epsilon);
		// time = 2
		assertEquals(10,po.calculateSpeed(2).getZ(), epsilon);
		// time = 3
		assertEquals(15,po.calculateSpeed(3).getZ(), epsilon);
	}
	
	@Test
	public void speedTestXYZ() {
		po.addForce(forceXYZ);
		assertEquals(0,po.calculateSpeed(0).getX(),epsilon);
		assertEquals(0,po.calculateSpeed(0).getY(),epsilon);
		assertEquals(0,po.calculateSpeed(0).getZ(),epsilon);
		//time = 1
		assertEquals(2,po.calculateSpeed(1).getX(),epsilon);
		assertEquals(6,po.calculateSpeed(1).getY(),epsilon);
		assertEquals(3,po.calculateSpeed(1).getZ(),epsilon);
		//time = 2
		assertEquals(4,po.calculateSpeed(2).getX(),epsilon);
		assertEquals(12,po.calculateSpeed(2).getY(),epsilon);
		assertEquals(6,po.calculateSpeed(2).getZ(),epsilon);
		//time = 3
		assertEquals(6,po.calculateSpeed(3).getX(),epsilon);
		assertEquals(18,po.calculateSpeed(3).getY(),epsilon);
		assertEquals(9,po.calculateSpeed(3).getZ(),epsilon);
	}

}
