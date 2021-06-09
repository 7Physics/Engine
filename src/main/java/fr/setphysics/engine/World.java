package fr.setphysics.engine;

import fr.setphysics.common.geom.Bounds;
import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Vec3;

import java.util.ArrayList;
import java.util.List;

public class World {
    private List<PhysicObject> physicObjects;
    private boolean gravityEnabled;
    private Vec3 gravity;
    
    public World() {
    	this.physicObjects = new ArrayList<PhysicObject>();
    }
    
    public void addPhysicObject(PhysicObject po) {
    	this.physicObjects.add(po);
    	if (gravityEnabled) {
    		po.addForce(this.gravity, 0);
    	}
    }
    
    public void removePhysicObject(PhysicObject po) {
    	this.physicObjects.remove(po);
    }
    
    public void addGravity(Vec3 gravity) {
    	if (this.gravityEnabled) {
    		return;
    	}
    	for(PhysicObject po: physicObjects) {
    		po.addForce(gravity, 0);
    	}
    	this.gravity = gravity;
    	this.gravityEnabled = true;
    }
    
    public void deleteGravity() {
    	if (gravityEnabled) {
    		this.gravityEnabled = false;
    		for(PhysicObject po: this.physicObjects) {
    			po.removeForce(0);
    		}
    		this.gravity = null;
    	}
    }
    
    public void step(long time) {
    	double timeSeconde = time/1000.0;
    	for(int i = 0; i < this.physicObjects.size(); i++) {
    		PhysicObject po = this.physicObjects.get(i);
			po.calculatePosition(timeSeconde);
			po.calculateSpeed(timeSeconde);
			handleCollisions(po, i);
		}
    }

    private void handleCollisions(PhysicObject po, int index) {
		// On conserve la position actuelle de l'objet
		// On le déplace
		// On regarde s'il y a collision : en y, en x puis en z
		// Si oui : on adapte la coordonnée en question
		// On modifie la vitesse pour éviter qu'elle s'incrémente encore plus

		for(int i = 0; i < index; i++) {
			PhysicObject po2 = physicObjects.get(i);

			if(po2.isDynamic()) {
				handleCollision(po2, po);
			}
			if(po.isDynamic()) {
				handleCollision(po, po2);
			}
		}
	}

	private void handleCollision(PhysicObject a, PhysicObject b) {
    	Position lastPosition = a.getLastPosition();
		Vec3 nextPosY = new Vec3(lastPosition.getX(), a.getY(), lastPosition.getZ());
		Vec3 nextPosX = new Vec3(a.getX(), lastPosition.getY(), lastPosition.getZ());
		Vec3 nextPosZ = new Vec3(lastPosition.getX(), lastPosition.getY(), a.getZ());

		Bounds otherBounds = b.getBounds();
		if(a.getShape().getBounds().translate(nextPosZ).intersect(otherBounds)) {
			double f = Math.abs(a.getSpeed().getZ()/b.getSpeed().getZ());
			if(f == Double.POSITIVE_INFINITY || f == Double.NEGATIVE_INFINITY) {
				f = 1;
			}
			if(a.getSpeed().getZ() < 0) {
				a.getPosition().setZ(otherBounds.getMaxZ()*f+a.getBounds().getLength()/2+0.001f);
			}else{
				a.getPosition().setZ(otherBounds.getMinZ()*f-a.getBounds().getLength()/2-0.001f);
			}
			a.getSpeed().setZ(0d);
		}
		else if(a.getShape().getBounds().translate(nextPosX).intersect(otherBounds)) {
			double f = Math.abs(a.getSpeed().getX()/b.getSpeed().getX());
			if(f == Double.POSITIVE_INFINITY || f == Double.NEGATIVE_INFINITY) {
				f = 1;
			}
			if(a.getSpeed().getX() < 0) {
				a.getPosition().setX(otherBounds.getMaxX()*f+a.getBounds().getWidth()/2+0.001f);
			}else{
				a.getPosition().setX(otherBounds.getMinX()*f-a.getBounds().getWidth()/2-0.001f);
			}
			a.getSpeed().setX(0d);
		}
		else if(a.getShape().getBounds().translate(nextPosY).intersect(otherBounds)) {
			double f = Math.abs(a.getSpeed().getY()/b.getSpeed().getY());
			if(f == Double.POSITIVE_INFINITY || f == Double.NEGATIVE_INFINITY) {
				f = 1;
			}
			if(a.getSpeed().getY() < 0) {
				a.getPosition().setY(otherBounds.getMaxY()*f+a.getBounds().getHeight()/2+0.001f);
			}else{
				a.getPosition().setY(otherBounds.getMinY()*f-a.getBounds().getHeight()/2-0.001f);
			}
			a.getSpeed().setY(0d);
		}
	}
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}