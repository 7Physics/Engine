package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Shape;
import fr.setphysics.common.geom.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Objet dans l'environnement avec des caracteristiques physique
 */
public class PhysicObject {
	private Shape shape;
	private Position position;
	private Position positionInitial;
	private List<Vec3> forces;
	private Vec3 speed;
	private Vec3 speedInitial;

	public PhysicObject(Shape shape, Position position) {
		this.shape = shape;
		this.position = position;
		this.positionInitial = position;
		this.forces = new ArrayList<Vec3>();
		this.speed = new Vec3(0, 0, 0);
		this.speedInitial = new Vec3(0, 0, 0);
	}

	public void addForce(Vec3 f) {
		forces.add(f);
	}

	public Vec3 cumulatedForces() {
		Vec3 additionForces = new Vec3(0, 0, 0);
		for (Vec3 force : this.forces) {
			additionForces.addX(force.getX());
			additionForces.addY(force.getY());
			additionForces.addZ(force.getZ());
		}
		return additionForces;
	}

	public Position calculatePosition(double time) {
		Vec3 additionForces = cumulatedForces();
		Vec3 newCoords = new Vec3(
				(this.speedInitial.getX() * time) + ((1 / 2.0) * additionForces.getX() * Math.pow(time, 2)),
				(this.speedInitial.getY() * time) + ((1 / 2.0) * additionForces.getY() * Math.pow(time, 2)),
				(this.speedInitial.getZ() * time) + ((1 / 2.0) * additionForces.getZ() * Math.pow(time, 2)));
		this.position.setCoords(newCoords);
		return this.position;
	}

	/**
	 *
	 * @param time
	 * @return
	 */
	public Vec3 calculateSpeed(double time) {
		// formule vitesse : v2 = vInitiale + acceleration * temps
		Vec3 additionForces = cumulatedForces();
		Vec3 newSpeed = new Vec3(this.speedInitial.getX() + (additionForces.getX() * time),
				this.speedInitial.getY() + (additionForces.getY() * time),
				this.speedInitial.getZ() + (additionForces.getZ() * time));
		this.speed = newSpeed.clone();
		return this.speed;
	}

	public double getSpeed() {
		return this.speed.getX() + this.speed.getY() + this.speed.getZ();
	}
}