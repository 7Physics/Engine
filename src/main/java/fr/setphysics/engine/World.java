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
			Position lastPosition = po.getPosition().clone();
			po.calculatePosition(timeSeconde);
			po.calculateSpeed(timeSeconde);
			if(po.isDynamic()) {
				handleCollisions(po, lastPosition);
			}
		}
    }

    private void handleCollisions(PhysicObject po, Position lastPosition) {
		// On conserve la position actuelle de l'objet
		// On le déplace
		// On regarde s'il y a collision : en y, en x puis en z
		// Si oui : on adapte la coordonnée en question
		// On modifie la vitesse pour éviter qu'elle s'incrémente encore plus
		Vec3 nextPosY = new Vec3(lastPosition.getX(), po.getY(), lastPosition.getZ());
		Vec3 nextPosX = new Vec3(po.getX(), lastPosition.getY(), lastPosition.getZ());
		Vec3 nextPosZ = new Vec3(lastPosition.getX(), lastPosition.getY(), po.getZ());
		for(PhysicObject po2 : physicObjects) {
			if(po == po2) {
				continue;
			}
			Bounds otherBounds = po2.getBounds();
			if(po.getShape().getBounds().translate(nextPosZ).intersect(otherBounds)) {
				if(po.getSpeed().getZ() < 0) {
					po.getPosition().setZ(otherBounds.getMaxZ()+po.getBounds().getLength()/2+0.001f);
				}else{
					po.getPosition().setZ(otherBounds.getMinZ()-po.getBounds().getLength()/2-0.001f);
				}
				po.getSpeed().setZ(0d);
			}
			else if(po.getShape().getBounds().translate(nextPosX).intersect(otherBounds)) {
				if(po.getSpeed().getX() < 0) {
					po.getPosition().setX(otherBounds.getMaxX()+po.getBounds().getWidth()/2+0.001f);
				}else{
					po.getPosition().setX(otherBounds.getMinX()-po.getBounds().getWidth()/2-0.001f);
				}
				po.getSpeed().setX(0d);
			}
			else if(po.getShape().getBounds().translate(nextPosY).intersect(otherBounds)) {
				if(po.getSpeed().getY() < 0) {
					po.getPosition().setY(otherBounds.getMaxY()+po.getBounds().getHeight()/2+0.001f);
				}else{
					po.getPosition().setY(otherBounds.getMinY()-po.getBounds().getHeight()/2-0.001f);
				}
				po.getSpeed().setY(0d);
			}
		}
	}
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}