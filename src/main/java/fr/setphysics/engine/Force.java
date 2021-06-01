package fr.setphysics.engine;

import main.java.fr.setphysics.common.geom.Vec3;

public class Force {
//    private String name;
    private double intensity;
    private Vec3 direction;

    public Force(double intensity, Vec3 direction) {
        this.intensity = intensity;
        this.direction = direction;
    }

    public double getIntensity() {
        return intensity;
    }

    public Vec3 getDirection() {
        return direction;
    }
}