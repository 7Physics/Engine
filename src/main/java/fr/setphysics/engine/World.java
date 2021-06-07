package fr.setphysics.engine;

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
			for(int j = i-1; j >= 0; j--) {
				PhysicObject po2 = this.physicObjects.get(j);
				if (po.collideWith(po2)) {
					System.out.println(po + "collides with " + po2);
					po.bounceAgainst(po2);
					po.setDynamic(false);
					po2.setDynamic(false);
					// Régler le problème d'overlapping
				}
			}
		}
    }
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}