package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Shape;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.common.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un objet 3D dans l'environnement avec des
 * caracteristiques physique.
 */
public class PhysicObject {
	/* objet 3D */
	private Shape shape;
	/* position courante de l'objet 3D */
	private Position position;
	/* position initiale de l'objet 3D */
	private Position positionInitial;
	/* liste des différentes forces à appliquer */
	private List<Vec3> forces;
	/* vitesse courante de l'objet */
	private Vec3 speed;
	/* vitesse initiale de l'objet */
	private Vec3 speedInitial;

	/**
	 * Constructeur
	 * 
	 * @param shape    : l'objet 3D
	 * @param position : position initiale de l'objet 3D
	 */
	public PhysicObject(Shape shape, Position position) {
		this.shape = shape;
		this.position = position;
		this.positionInitial = position;
		this.forces = new ArrayList<Vec3>();
		this.speed = new Vec3(0, 0, 0);
		this.speedInitial = new Vec3(0, 0, 0);
	}

	/**
	 * Constructeur
	 * 
	 * @param shape     : l'objet 3D
	 * @param position  : position initiale de l'objet
	 * @param speedInit : vitesse initiale de l'objet
	 */
	public PhysicObject(Shape shape, Position position, Vec3 speedInit) {
		this.shape = shape;
		this.position = position;
		this.positionInitial = position;
		this.forces = new ArrayList<Vec3>();
		this.speed = speedInit;
		this.speedInitial = speedInit;
	}

	/**
	 * Ajout d'une force f (représentée par un Vec3) dans la liste des forces à
	 * appliquer.
	 * 
	 * @param f
	 */
	public void addForce(Vec3 f) {
		forces.add(new Vec3(f.getX() / 10.0, f.getY() / 10.0, f.getZ() / 10.0));
	}

	/**
	 * Calcul du total des forces à appliquer sur l'objet.
	 * 
	 * @return Vec3 représentant le total des forces
	 */
	public Vec3 cumulatedForces() {
		Vec3 additionForces = new Vec3(0, 0, 0);
		for (Vec3 force : this.forces) {
			additionForces.addX(force.getX());
			additionForces.addY(force.getY());
			additionForces.addZ(force.getZ());
		}
		return additionForces;
	}

	public double positionEquation(double position, double speed, double forces, double time) {
		return position + (speed * time) + ((1 / 2.0) * forces * Math.pow(time, 2));
	}

	/**
	 * Calcul et mise à jour de la position de l'objet en prenant compte les
	 * différentes forces appliquées.
	 * 
	 * @param time : le temps écoulé depuis que la simulation a été lancée.
	 * @return la nouvelle position de l'objet
	 */
	public Position calculatePosition(double time) {
		Vec3 additionForces = cumulatedForces();
		Vec3 newCoords = new Vec3(
				positionEquation(this.positionInitial.getX(), this.speed.getX(), additionForces.getX(), time),
				positionEquation(this.positionInitial.getY(), this.speed.getY(), additionForces.getY(), time),
				positionEquation(this.positionInitial.getZ(), this.speed.getZ(), additionForces.getZ(), time));

		Logger.info("Changement de position : x :" + newCoords.getX() + " y :" + newCoords.getY() + " z :"
				+ newCoords.getZ());

		if (newCoords.getY() < this.shape.getMinY()) {
			newCoords.addY(this.shape.getMinY() - newCoords.getY());
		}

		this.position.setCoords(newCoords);
		return this.position;
	}

	/**
	 * Calcul et mise à jour de la vitesse de l'objet.
	 * 
	 * @param time: le temps écoulé depuis que la simulation a été lancée.
	 * @return la nouvelle vitesse de l'objet
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

	/**
	 * Calcul de la vitesse globale.
	 * 
	 * @return la vitesse de l'objet
	 */
	public double getSpeed() {
		return this.speed.getX() + this.speed.getY() + this.speed.getZ();
	}
}