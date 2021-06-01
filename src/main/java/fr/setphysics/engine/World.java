package fr.setphysics.engine;

import java.util.ArrayList;
import java.util.List;
import fr.setphysics.engine.PhysicObject;

public class World {
    private List<PhysicObject> physicObjects;
    
    public World() {
    	this.physicObjects = new ArrayList<PhysicObject>();
    }
    
    public void addPhysicObject(PhysicObject po) {
    	this.physicObjects.add(po);
    }
    
    public void step(long time) {
    	for(PhysicObject po : this.physicObjects) {
    		po.calculatePosition(time);
    		po.calculateSpeed(time);
    	}
    }
}