package fr.setphysics.engine;

import java.util.ArrayList;
import java.util.List;

import fr.setphysics.common.geom.Vec3;
import fr.setphysics.engine.PhysicObject;

public class World {
    private List<PhysicObject> physicObjects;
    private Vec3 gravity;
    
    public World() {
    	this.physicObjects = new ArrayList<PhysicObject>();
    }
    
    public void addPhysicObject(PhysicObject po) {
    	this.physicObjects.add(po);
    }
    
    public void removePhysicObject(PhysicObject po) {
    	this.physicObjects.remove(po);
    }
    
    public void step(long time) {
    	double timeSeconde = time/1000.0;
    	for(PhysicObject po : this.physicObjects) {
    		po.calculatePosition(timeSeconde);
    		po.calculateSpeed(timeSeconde);
    	}
    }
    
    public boolean isGravityEnabled() {
    	return gravity != null;
    }
    
    public List<PhysicObject> getPhysicObjects(){
    	return this.physicObjects;
    }
}