package fr.setphysics.engine;

import fr.setphysics.common.geom.Bounds;
import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Vec3;
import fr.setphysics.common.geom.shape.Cuboid;
import fr.setphysics.common.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<PhysicObject> physicObjects;
    private boolean gravityEnabled;
    private Vec3 gravity;

	/**
	 * Crée un nouveau monde 3D avec une sol de dimensions 10x10
	 */
	public World() {
    	this(10);
    }

	/**
	 *  Crée un nouveau monde 3D avec une sol de dimensions personnalisées
	 * @param groundSize Dimension du sol
	 */
	public World(double groundSize) {
		this.physicObjects = new ArrayList<>();
		PhysicObject ground = new PhysicObject(new Cuboid(groundSize, groundSize, 2), new Position(0, -1, 0));
		ground.setDynamic(false);
		addPhysicObject(ground);
	}

	/**
	 * Ajoute un corps physique dans le monde
	 * @param po Corps physique à ajouter
	 */
    public void addPhysicObject(PhysicObject po) {
    	this.physicObjects.add(po);
    	if (gravityEnabled) {
    		po.addForce(this.gravity, 0);
    	}
    }

	/**
	 * Supprime un corps physique du monde
	 * @param po Corps physique à supprimer
	 */
	public void removePhysicObject(PhysicObject po) {
    	this.physicObjects.remove(po);
    }

	/**
	 * Définit la gravité du monde
	 * @param gravity Valeur de gravité en m/s
	 */
	public void setGravity(Vec3 gravity) {
    	if (this.gravityEnabled) {
			for(PhysicObject po: this.physicObjects) {
				po.getForces().get(0).setY(gravity.getY());
			}
			return;
    	}
    	for(PhysicObject po: this.physicObjects) {
    		po.addForce(gravity, 0);
    	}
    	this.gravity = gravity;
    	this.gravityEnabled = true;
    }

	/**
	 * Supprime la gravité du monde
	 */
	public void deleteGravity() {
    	if (this.gravityEnabled) {
    		this.gravityEnabled = false;
    		for(PhysicObject po: this.physicObjects) {
    			po.removeForce(0);
    		}
    		this.gravity = null;
    	}
    }

	/**
	 * Avance la simulation de time milliseconde
	 * @param time Avancement en milliseconde
	 */
	public void step(long time) {
    	double timeSeconde = time/1000.0;
		for(int i = 0; i < this.physicObjects.size(); i++) {
			PhysicObject po = this.physicObjects.get(i);
			po.update(timeSeconde);
			doCollisions(po, i);
		}
    }

	/**
	 * Réinitialise la simulation
	 */
	public void reset() {
    	Logger.debug("Reset du monde, tous les objets retournent à leur position d'origine.");
		for(PhysicObject po: this.physicObjects) {
			po.reset();
		}
	}

	/**
	 * Gère les collisions pour un objet précis
	 * @param po Objet dont calculer les collisions
	 * @param index Index de l'objet
	 */
    private void doCollisions(PhysicObject po, int index) {
		for(int i = 0; i < index; i++) {
			PhysicObject po2 = physicObjects.get(i);

			Vec3 newCoords1 = null, newCoords2 = null;
			if(po.isDynamic()) {
				newCoords1 = doCollision(po, po2);
			}
			if(po2.isDynamic()) {
				newCoords2 = doCollision(po2, po);
			}

			if(newCoords1 != null) {
				po.getPosition().setCoords(newCoords1);
			}
			if(newCoords2 != null) {
				po2.getPosition().setCoords(newCoords2);
			}
		}
	}

	/**
	 * Gère les collisions de A contre B (en mode AABB)
	 * @param a Corps physique A
	 * @param b Corps physique B
	 * @return Nouvelle position à attribuer à A si une collision a été détectée ou null
	 */
	private Vec3 doCollision(PhysicObject a, PhysicObject b) {
    	boolean collision = false;
    	Vec3 positionWithoutCollisions = a.getCoords();
    	Position lastPosition = a.getLastPosition();
		Vec3 nextPosY = new Vec3(lastPosition.getX(), a.getY(), lastPosition.getZ());
		Vec3 nextPosX = new Vec3(a.getX(), lastPosition.getY(), lastPosition.getZ());
		Vec3 nextPosZ = new Vec3(lastPosition.getX(), lastPosition.getY(), a.getZ());

		Bounds bounds = a.getShape().getBounds();
		Bounds otherBounds = b.getBounds();
		if(bounds.translate(nextPosZ).intersect(otherBounds)) {
			collision = true;
			positionWithoutCollisions.setZ(lastPosition.getZ());
			a.getSpeed().setZ(0d);
		}
		if(bounds.translate(nextPosX).intersect(otherBounds)) {
			collision = true;
			positionWithoutCollisions.setX(lastPosition.getX());
			a.getSpeed().setX(0d);
		}
		if(bounds.translate(nextPosY).intersect(otherBounds)) {
			collision = true;
			positionWithoutCollisions.setY(lastPosition.getY());
			a.getSpeed().setY(0d);
		}
		return collision ? positionWithoutCollisions : null;
	}

	/**
	 * Renvoie la liste des corps physiques contenus dans le monde
	 * @return liste des corps physiques contenus dans le monde.
	 */
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}