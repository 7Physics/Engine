package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Shape;
import fr.setphysics.engine.Force;
import java.util.List;

/**
 * Objet dans l'environnement avec des caracteristiques physique
 */
public class PhysicObject {
    private Shape shape;
    private Position position;
    private List<Force> forces;

    public PhysicObject(Shape shape, Position position) {
        this.shape = shape;
        this.position = position;
    }

    public void addForce(Force f) {
        forces.add(f);
    }

    public Position getPosition() {
        return this.position;
    }

    public void applyForce() {
        for (Force force : forces) {
            position.getCoords().setX(position.getCoords().getX() + force.getDirection().getX() / force.getIntensity());
            position.getCoords().setY(position.getCoords().getY() + force.getDirection().getY() / force.getIntensity());
            position.getCoords().setZ(position.getCoords().getZ() + force.getDirection().getZ() / force.getIntensity());

        }
    }
}