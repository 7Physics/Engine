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
    
    public void deleteGravity() {
    	if (this.gravityEnabled) {
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
			po.update(timeSeconde);
			handleCollisions(po, i);
		}
    }

    public void reset() {
		for(PhysicObject po: this.physicObjects) {
			po.reset();
		}
	}

    private void handleCollisions(PhysicObject po, int index) {
		for(int i = 0; i < index; i++) {
			PhysicObject po2 = physicObjects.get(i);

			Vec3 newCoords1 = null, newCoords2 = null;
			if(po.isDynamic()) {
				newCoords1 = handleCollision(po, po2);
			}
			if(po2.isDynamic()) {
				newCoords2 = handleCollision(po2, po);
			}

			if(newCoords1 != null) {
				po.getPosition().setCoords(newCoords1);
			}
			if(newCoords2 != null) {
				po2.getPosition().setCoords(newCoords2);
			}
		}
	}

	private Vec3 handleCollision(PhysicObject a, PhysicObject b) {
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

    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}