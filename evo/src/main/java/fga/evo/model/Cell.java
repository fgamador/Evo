package fga.evo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cell {
    static final double PULL_SPRING_CONSTANT = 1;
    // costs and yields are all energy per area
    static final double FAT_GROWTH_COST = 1;
    static final double FAT_MAINTENANCE_COST = 0.005;
    static final double FAT_BURN_YIELD = 0.9;
    static final double PHOTOSYNTHETIC_RING_GROWTH_COST = 1.1;
    static final double PHOTOSYNTHETIC_RING_MAINTENANCE_COST = 0.005;
    static final double MAX_PHOTOSYNTHETIC_RING_EFFICIENCY = 0.9;
    static final double MIN_REPRODUCTION_RADIUS = 50;
    static final double MIN_REPRODUCTION_ENERGY = 10;
    static final double MAX_CHILD_RADIUS = 20;
    static final double MAX_TOTAL_OVERLAP = 0.1;
    static final double MASS_PER_AREA = 0.01;

    private final World world;
    private Thruster thruster;
    private double radius;
    private double photosyntheticRingOuterRadius;
    private double photosyntheticRingArea;
    private double fatRadius;
    private double fatArea;
    private double mass;
    private double centerX, centerY;
    private double velocityX = 0, velocityY = 0;
    private double forceX = 0, forceY = 0;
    private double pullPointX = 0, pullPointY = 0;
    private List<CellCellInteraction> interactingPairs = new ArrayList<>();
    private boolean alive = true;
    private boolean pulled = false;
    private Cell parent = null;
    private Cell child = null;
    private double donatedEnergy;
    private double totalOverlap = 0;
    private boolean reifyForces = false;
    private List<Force> forces = new ArrayList<>();

    /** Creates an original, spontaneously generated cell. */
    Cell(World world, Thruster thruster, double radius, double centerX, double centerY) {
        this.world = world;
        this.thruster = thruster;
        setRadius(radius);
        this.centerX = centerX;
        this.centerY = centerY;
        fatRadius = 0.9 * radius; // TODO
        photosyntheticRingOuterRadius = radius;
        radiiToAreas();
    }

    /** Creates a child cell. */
    private Cell(Cell parent, double angle) {
        this.parent = parent;
        world = parent.world;
        thruster = ZeroThruster.INSTANCE;
        centerX = parent.centerX + parent.radius * Math.cos(angle);
        centerY = parent.centerY + parent.radius * Math.sin(angle);
        fatArea = 0;
        photosyntheticRingArea = 0;
    }

    /** Housekeeping before all the cell ticks. */
    void pretick() {
        interactingPairs.clear();
    }

    /** Records that this cell is interacting with another cell. */
    void addInteraction(CellCellInteraction pair) {
        interactingPairs.add(pair);
    }

    // -- energy, growth, reproduction --

    void balanceEnergy(Set<Cell> newCells) {
        if (!alive) {
            return;
        }

        double availableEnergy = donatedEnergy() + photosynthesis() - maintenance();
        double growthEnergy = availableEnergy - thruster.getEnergy();
        if (growthEnergy > 0) {
            energySurplus(growthEnergy, newCells);
        } else if (growthEnergy < 0) {
            energyDeficit(growthEnergy);
        }
    }

    private double donatedEnergy() {
        double energy = donatedEnergy;
        donatedEnergy = 0;
        return energy;
    }

    private double photosynthesis() {
        return world.getLightIntensity(centerY) * radius * getPhotoRingEfficiency();
    }

    private double maintenance() {
        double phRingCost = PHOTOSYNTHETIC_RING_MAINTENANCE_COST * photosyntheticRingArea;
        double fatCost = FAT_MAINTENANCE_COST * fatArea;
        return phRingCost + fatCost;
    }

    private void energySurplus(double growthEnergy, Set<Cell> newCells) {
        if (child != null) {
            supportChild(growthEnergy);
        } else if (radius >= MIN_REPRODUCTION_RADIUS && growthEnergy >= MIN_REPRODUCTION_ENERGY) {
            reproduce(growthEnergy, newCells);
        } else if (totalOverlap < MAX_TOTAL_OVERLAP) { // don't grow if too crowded
            grow(growthEnergy);
        }
    }

    private void supportChild(double growthEnergy) {
        if (child.radius < MAX_CHILD_RADIUS) {
            child.donatedEnergy += growthEnergy;
        } else {
            releaseChild();
        }
    }

    private void releaseChild() {
        child.parent = null;
        child = null;
    }

    private void reproduce(double growthEnergy, Set<Cell> newCells) {
        child = new Cell(this, world.randomAngle());
        newCells.add(child);
        //child.pretick();
        child.donatedEnergy = growthEnergy;
        child.balanceEnergy(newCells);
    }

    private void grow(double growthEnergy) {
        growthEnergy = growPhotoRing(growthEnergy);
        growFatCircle(growthEnergy);
        areasToRadii();
    }

    private double growPhotoRing(double growthEnergy) {
        double thickness = photosyntheticRingOuterRadius - fatRadius;
        if (thickness >= getTargetPhotoRingThickness()) {
            return growthEnergy;
        } else {
            photosyntheticRingArea += growthEnergy / PHOTOSYNTHETIC_RING_GROWTH_COST;
            return 0;
        }
    }

    private void growFatCircle(double growthEnergy) {
        fatArea += growthEnergy / FAT_GROWTH_COST;
    }

    private double getPhotoRingEfficiency() {
        // efficiency ranges from 0 to 1, asymptotic
        double thickness = photosyntheticRingOuterRadius - fatRadius;
        return 1 - (1 / (thickness + 1));
    }

    private double getTargetPhotoRingThickness() {
        // derived from efficiency equation
        return (1 / (1 - MAX_PHOTOSYNTHETIC_RING_EFFICIENCY)) - 1;
    }

    private void energyDeficit(double growthEnergy) {
        growthEnergy += burnFat(-growthEnergy);
        if (growthEnergy < 0) {
            //                if (-growthEnergy >= thruster.getEnergy())
            //                    ; // TODO reduce thrust by growthEnergy
            //                else
            die();
        }
    }

    private double burnFat(double energyRequired) {
        double energyYield;
        double areaRequired = energyRequired / FAT_BURN_YIELD;
        if (areaRequired <= fatArea) {
            energyYield = energyRequired;
            fatArea -= areaRequired;
        } else {
            energyYield = fatArea * FAT_BURN_YIELD;
            fatArea = 0;
        }
        areasToRadii();
        return energyYield;
    }

    private void die() {
        if (child != null)
            releaseChild();
        if (parent != null)
            parent.releaseChild();
        thruster = ZeroThruster.INSTANCE;
        alive = false;
    }

    // -- forces and motion --

    void calculateForces() {
        forceX = forceY = 0;
        if (!forces.isEmpty()) {
            forces.clear();
        }
        totalOverlap = 0;

        // if (alive) {
        //        forceX += thruster.getForceX();
        //        forceY += thruster.getForceY();
        // }

        if (pulled) {
            pullForce();
        }
        fluidResistanceForce();
        wallCollisionForces();
        pairInteractionForces();
    }

    private void pullForce() {
        double pullX = PULL_SPRING_CONSTANT * (pullPointX - centerX);
        double pullY = PULL_SPRING_CONSTANT * (pullPointY - centerY);
        forceX += pullX;
        forceY += pullY;
        if (reifyForces) {
            forces.add(new Force(0, 0, pullX, pullY));
        }
    }

    private void fluidResistanceForce() {
        double relativeVelocityX = velocityX - world.getCurrentX(centerX, centerY);
        double relativeVelocityY = velocityY - world.getCurrentY(centerX, centerY);
        double dragX = -Math.signum(relativeVelocityX) * World.FLUID_RESISTANCE * radius * relativeVelocityX
            * relativeVelocityX;
        double dragY = -Math.signum(relativeVelocityY) * World.FLUID_RESISTANCE * radius * relativeVelocityY
            * relativeVelocityY;
        forceX += dragX;
        forceY += dragY;
        if (reifyForces) {
            forces.add(new Force(0, 0, dragX, dragY));
        }
    }

    private void wallCollisionForces() {
        double leftOverlap = radius - centerX;
        if (leftOverlap > 0) {
            double wallForce = CellCellCollision.SPRING_CONSTANT * leftOverlap;
            forceX += wallForce;
            if (reifyForces) {
                forces.add(new Force(0 /*-radius*/, 0, wallForce, 0));
            }
            totalOverlap += leftOverlap;
        }

        double rightOverlap = centerX + radius - world.getWidth();
        if (rightOverlap > 0) {
            double wallForce = -CellCellCollision.SPRING_CONSTANT * rightOverlap;
            forceX += wallForce;
            if (reifyForces) {
                forces.add(new Force(0 /* radius */, 0, wallForce, 0));
            }
            totalOverlap += rightOverlap;
        }

        double topOverlap = radius - centerY;
        if (topOverlap > 0) {
            double wallForce = CellCellCollision.SPRING_CONSTANT * topOverlap;
            forceY += wallForce;
            if (reifyForces) {
                forces.add(new Force(0, 0 /*-radius*/, 0, wallForce));
            }
            totalOverlap += topOverlap;
        }

        double bottomOverlap = centerY + radius - world.getHeight();
        if (bottomOverlap > 0) {
            double wallForce = -CellCellCollision.SPRING_CONSTANT * bottomOverlap;
            forceY += wallForce;
            if (reifyForces) {
                forces.add(new Force(0, 0 /* radius */, 0, wallForce));
            }
            totalOverlap += bottomOverlap;
        }
    }

    private void pairInteractionForces() {
        for (CellCellInteraction pair : interactingPairs) {
            if (this == pair.getCell1()) {
                forceX -= pair.getForceX();
                forceY -= pair.getForceY();
                if (reifyForces) {
                    forces.add(new Force(0, 0, -pair.getForceX(), -pair.getForceY()));
                }
            } else {
                forceX += pair.getForceX();
                forceY += pair.getForceY();
                if (reifyForces) {
                    forces.add(new Force(0, 0, pair.getForceX(), pair.getForceY()));
                }
            }
            totalOverlap += pair.getOverlap();
        }
    }

    void move() {
        velocityX += forceX / mass;
        velocityY += forceY / mass;
        centerX += velocityX;
        centerY += velocityY;
    }

    // -- util --

    private void radiiToAreas() {
        fatArea = Math.PI * fatRadius * fatRadius;
        photosyntheticRingArea = Math.PI * photosyntheticRingOuterRadius * photosyntheticRingOuterRadius
            - fatArea;
    }

    private void areasToRadii() {
        fatRadius = Math.sqrt(fatArea / Math.PI);
        photosyntheticRingOuterRadius = Math.sqrt(fatRadius * fatRadius + photosyntheticRingArea / Math.PI);
        setRadius(photosyntheticRingOuterRadius);
    }

    private void setRadius(double val) {
        radius = val;
        mass = MASS_PER_AREA * (Math.PI * radius * radius);
    }

    // -- accessors --

    public final double getRadius() {
        return radius;
    }

    public final double getCenterX() {
        return centerX;
    }

    public final double getCenterY() {
        return centerY;
    }

    public final double getFatRadius() {
        return fatRadius;
    }

    public final double getPhotosyntheticRingOuterRadius() {
        return photosyntheticRingOuterRadius;
    }

    public final boolean isAlive() {
        return alive;
    }

    public final Cell getChild() {
        return child;
    }

    public final void setReifyForces(boolean val) {
        reifyForces = val;
    }

    public final List<Force> getForces() {
        return forces;
    }

    public final void setPulled(boolean val) {
        pulled = val;
    }

    public final void setPullPoint(double x, double y) {
        pullPointX = x;
        pullPointY = y;
    }
}
