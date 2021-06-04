package fr.setphysics.engine;

import java.util.ArrayList;
import java.util.List;

import fr.setphysics.common.geom.Vec3;
import fr.setphysics.engine.PhysicObject;

public class World {
    private List<PhysicObject> physicObjects;
    private boolean gravity;
    
    public World() {
    	this.physicObjects = new ArrayList<PhysicObject>();
    }
    
    public void addPhysicObject(PhysicObject po) {
    	this.physicObjects.add(po);
    }
    
    public void removePhysicObject(PhysicObject po) {
    	this.physicObjects.remove(po);
    }
    
    public void addGravity(Vec3 gravity) {
    	if (this.gravity) {
    		return;
    	}
    	for(PhysicObject po: physicObjects) {
    		po.addForce(gravity, 0);
    	}
    	this.gravity = true;
    }
    
    public void deleteGravity() {
    	if (gravity) {
    		this.gravity = false;
    		for(PhysicObject po: this.physicObjects) {
    			po.removeForce(0);
    		}
    	}
    }
    
    public void step(long time) {
    	double timeSeconde = time/1000.0;
    	for(PhysicObject po : this.physicObjects) {
    		po.calculatePosition(timeSeconde);
    		po.calculateSpeed(timeSeconde);
    	}
    }
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}