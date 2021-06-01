package fr.setphysics.engine;

import fr.setphysics.common.geom.Position;
import fr.setphysics.common.geom.Shape;
import main.java.fr.setphysics.common.geom.Vec3;

import java.util.List;

/**
 * Objet dans l'environnement avec des caracteristiques physique
 */
public class PhysicObject {
    private Shape shape;
    private Position position;
    private List<Vec3> forces;
    private Vec3 speed;

    public PhysicObject(Shape shape, Position position) {
        this.shape = shape;
        this.position = position;
        this.speed = new Vec3(0,0,0);
    }

    public void addForce(Vec3 f) {
        forces.add(f);
    }

    public Vec3 cumulatedForces() {
        Vec3 additionForces = new Vec3(0,0,0);
        for (Vec3 force : this.forces) {
            additionForces.addX(force.getX());
            additionForces.addY(force.getY());
            additionForces.addZ(force.getZ());
        }
        return additionForces;
    }

    public Position calculatePosition(double time) {
        this.position.getCoords().addX((this.speed.getX() * time) + (1/2 * cumulatedForces().getX() * Math.pow(time, 2)));
        this.position.getCoords().addY((this.speed.getY() * time) + (1/2 * cumulatedForces().getY() * Math.pow(time, 2)));
        this.position.getCoords().addZ((this.speed.getZ() * time) + (1/2 * cumulatedForces().getZ() * Math.pow(time, 2)));
        return this.position;
    }

    /**
     *
     * @param time
     * @return
     */
    public double calculateSpeed(double time) {
        // formule vitesse : v2 = v1 + acceleration * temps
        this.speed.addX(cumulatedForces().getX() * time);
        this.speed.addY(cumulatedForces().getY() * time);
        this.speed.addZ(cumulatedForces().getZ() * time);
        return this.speed.getX() + this.speed.getY() + this.speed.getZ();
    }
}