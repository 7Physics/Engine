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

//			if(po2.isDynamic()) {
			handleCollision(po, po2);
//			}
//			if(po.isDynamic()) {
//				handleCollision(po, po2);
//			}
		}
	}

	private void handleCollision(PhysicObject a, PhysicObject b) {
    	Position lastPosition = a.getLastPosition();
		Vec3 nextPosY = new Vec3(lastPosition.getX(), a.getY(), lastPosition.getZ());
		Vec3 nextPosX = new Vec3(a.getX(), lastPosition.getY(), lastPosition.getZ());
		Vec3 nextPosZ = new Vec3(lastPosition.getX(), lastPosition.getY(), a.getZ());

		Bounds bounds = a.getShape().getBounds();
		Bounds otherBounds = b.getBounds();
		if(bounds.translate(nextPosZ).intersect(otherBounds)) {
			if(b.getSpeed().getZ() >= 0) {
				if(b.isDynamic())
				b.getPosition().setZ(b.getPosition().getZ()+b.getSpeed().getZ()/30);
				if(a.isDynamic())
				a.getPosition().setZ(a.getPosition().getZ()-a.getSpeed().getZ()/30);
			}else{
				if(b.isDynamic())
				b.getPosition().setZ(b.getPosition().getZ()+b.getSpeed().getZ()/30);
				if(a.isDynamic())
				a.getPosition().setZ(a.getPosition().getZ()-a.getSpeed().getZ()/30);
			}
			a.getSpeed().setZ(0d);
			b.getSpeed().setZ(0d);
		}
		else if(bounds.translate(nextPosX).intersect(otherBounds)) {
			if(b.getSpeed().getX() >= 0) {
				if(b.isDynamic())
				b.getPosition().setX(b.getPosition().getX()+b.getSpeed().getX()/30);
				if(a.isDynamic())
				a.getPosition().setX(a.getPosition().getX()-a.getSpeed().getX()/30);
			}else{
				if(b.isDynamic())
				b.getPosition().setX(b.getPosition().getX()+b.getSpeed().getX()/30);
				if(a.isDynamic())
				a.getPosition().setX(a.getPosition().getX()-a.getSpeed().getX()/30);
			}
			a.getSpeed().setX(0d);
			b.getSpeed().setX(0d);
		}
		else if(bounds.translate(nextPosY).intersect(otherBounds)) {
			if(b.getSpeed().getY() >= 0) {
				if(b.isDynamic())
				b.getPosition().setY(b.getPosition().getY()+b.getSpeed().getY()/30);
				if(a.isDynamic())
				a.getPosition().setY(a.getPosition().getY()-a.getSpeed().getY()/30);
			}else{
				if(b.isDynamic())
				b.getPosition().setY(b.getPosition().getY()-b.getSpeed().getY()/30);
				if(a.isDynamic())
				a.getPosition().setY(a.getPosition().getY()+a.getSpeed().getY()/30);
			}
			a.getSpeed().setY(0d);
			b.getSpeed().setY(0d);
		}
	}
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}