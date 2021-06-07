package fr.setphysics.engine;

import fr.setphysics.common.geom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe représentant un objet 3D dans l'environnement avec des
 * caracteristiques physique.
 */
public class PhysicObject implements Positionable {
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

	private Set<Vec3> uniqueShapeVertices;

	private boolean dynamic = true;

	/**
	 * Met à jour la position et la vitesse selon le temps écoulé depuis la dernière
	 * mise à jour
	 *
	 * @param time: Temps écoulé depuis la dernière mise à jour
	 */
	public void update(double time) {
		this.calculatePosition(time);
		this.calculateSpeed(time);
	}

	/**
	 * Getter of all the forces applied to a Physic Object
	 *
	 * @return
	 */
	public List<Vec3> getForces() {
		return this.forces;
	}

	/**
	 * Constructeur
	 * 
	 * @param shape    : l'objet 3D
	 * @param position : position initiale de l'objet 3D
	 */
	public PhysicObject(Shape shape, Position position) {
		this(shape, position, new Vec3(0, 0, 0));
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
		this.positionInitial = new Position(position.getX(), position.getY(), position.getZ());
		this.forces = new ArrayList<Vec3>();
		this.speed = speedInit;
		this.speedInitial = new Vec3(speedInit.getX(), speedInit.getY(), speedInit.getZ());
		this.uniqueShapeVertices = new HashSet<>(shape.getVertices());
	}

	public Shape getShape() {
		return shape;
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

	public void addForce(Vec3 f, int index) {
		forces.add(index, f);
	}
	
	public void removeForce(int i) {
		forces.remove(i);
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
	
	/**
	 * Applique l'équation du calcul de la position selon les forces appliquées.
	 * @param position : position (en x, y ou z) initiale 
	 * @param speed : vitesse (en x, y ou z) initiale
	 * @param forces : cumul des forces (en x, y ou z)
	 * @param time : le temps écoulé depuis que la simulation a été lancée.
	 * @return résultat de l'équation (nouvelle coordonnée x, y ou z)
	 */
	public double positionEquation(double position, double speed, double forces, double time) {
		return (position + (speed * time) + ((1 / 2.0) * forces * Math.pow(time, 2)));
	}

	/**
	 * Calcul et mise à jour de la position de l'objet en prenant compte les
	 * différentes forces appliquées.
	 * 
	 * @param time : le temps écoulé depuis que la simulation a été lancée.
	 * @return la nouvelle position de l'objet
	 */
	public Position calculatePosition(double time) {
		if(!this.dynamic) {
			return this.position;
		}
		Vec3 additionForces = cumulatedForces();
		Vec3 newCoords = new Vec3(
				positionEquation(this.position.getX(), this.speed.getX(), additionForces.getX(), time),
				positionEquation(this.position.getY(), this.speed.getY(), additionForces.getY(), time),
				positionEquation(this.position.getZ(), this.speed.getZ(), additionForces.getZ(), time));

		this.position.setCoords(newCoords);
		return this.position;
	}
	
	/**
	 * Applique l'équation du calcul de la vitesse selon les forces appliquées.
	 * @param speed : vitesse (en x, y ou z) initiale
	 * @param forces : cumul des forces (en x, y ou z)
	 * @param time : le temps écoulé depuis que la simulation a été lancée.
	 * @return nouvelle vitesse
	 */
	public double speedEquation(double speed, double forces, double time) {
		return speed + (forces*time);
	}

	/**
	 * Calcul et mise à jour de la vitesse de l'objet.
	 * 
	 * @param time: le temps écoulé depuis que la simulation a été lancée.
	 * @return la nouvelle vitesse de l'objet
	 */
	public Vec3 calculateSpeed(double time) {
		if (!this.dynamic) {
			return this.speed;
		}
		// formule vitesse : v2 = vInitiale + acceleration * temps
		Vec3 additionForces = cumulatedForces();
		Vec3 newSpeed = new Vec3(speedEquation(this.speed.getX(), additionForces.getX(), time),
				speedEquation(this.speed.getY(), additionForces.getY(), time),
				speedEquation(this.speed.getZ(), additionForces.getZ(), time));
		this.speed = newSpeed.clone();
		return this.speed;
	}

	public Position getPosition() {
		return position;
	}

	public Vec3 getSpeed() {
		return speed;
	}

	/**
	 * Calcul de la vitesse globale.
	 *
	 * @return la vitesse de l'objet
	 */
	public double getSpeedValue() {
		return this.speed.getX() + this.speed.getY() + this.speed.getZ();
	}

	public Bounds getBounds() {
		return shape.getBounds().translate(this.position.getCoords());
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public boolean collideWith(PhysicObject other) {
		return this.getBounds().intersect(other.getBounds());
	}

	@Override
	public String toString() {
		return "PhysicObject{" +
				"shape=" + shape +
				", position=" + position +
				", dynamic=" + dynamic +
				'}';
	}
}