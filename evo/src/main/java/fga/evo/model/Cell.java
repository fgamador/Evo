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
    static final double CHILD_START_RADIUS = 1;
    static final double MAX_CHILD_RADIUS = 20;
    static final double MAX_TOTAL_OVERLAP = 0.1;
    static final double MASS_PER_AREA = 0.01;
    static double USABLE_GROWTH_ENERGY_PER_MASS = 10;
    static double MAX_SPEED = 4;

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
    private List<Force> forces = null;

    /** Creates an original, spontaneously generated cell. */
    Cell(World world, Thruster thruster, double radius, double centerX, double centerY) {
        this.world = world;
        this.thruster = thruster;
        setRadius(radius);
        this.centerX = centerX;
        this.centerY = centerY;
        fatRadius = 0.9 * radius; // TODO param
        photosyntheticRingOuterRadius = radius;
        radiiToAreas();
    }

    /** Creates a child cell. */
    private Cell(Cell parent, double angle) {
        this(parent.world, ZeroThruster.INSTANCE, CHILD_START_RADIUS, parent.getSpawningX(angle,
            CHILD_START_RADIUS), parent.getSpawningY(angle, CHILD_START_RADIUS));
        this.parent = parent;
    }

    private double getSpawningX(double angle, double childRadius) {
        return centerX + (radius + childRadius) * Math.cos(angle);
    }

    private double getSpawningY(double angle, double childRadius) {
        return centerY + (radius + childRadius) * Math.sin(angle);
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
            growthEnergy = supportChild(growthEnergy);
        } else if (radius >= MIN_REPRODUCTION_RADIUS && growthEnergy >= MIN_REPRODUCTION_ENERGY) {
            growthEnergy = reproduce(growthEnergy, newCells);
        }
        if (totalOverlap < MAX_TOTAL_OVERLAP) { // don't grow if too crowded
            grow(growthEnergy);
        }
    }

    private double supportChild(double growthEnergy) {
        if (child.radius < MAX_CHILD_RADIUS) {
            return donateEnery(child, growthEnergy);
        } else {
            releaseChild();
            return growthEnergy;
        }
    }

    private void releaseChild() {
        child.parent = null;
        child = null;
    }

    private double reproduce(double growthEnergy, Set<Cell> newCells) {
        child = new Cell(this, world.randomAngle());
        newCells.add(child);
        //child.pretick();
        growthEnergy = donateEnery(child, growthEnergy);
        child.balanceEnergy(newCells);
        return growthEnergy;
    }

    double donateEnery(Cell child, double growthEnergy) {
        double donatedEnergy = Math.min(growthEnergy, child.getMaxUsableGrowthEnergy());
        child.donatedEnergy += donatedEnergy;
        growthEnergy -= donatedEnergy;
        return growthEnergy;
    }

    private double getMaxUsableGrowthEnergy() {
        return USABLE_GROWTH_ENERGY_PER_MASS * mass;
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
        if (forces != null) {
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
        if (forces != null) {
            forces.add(new Force(0, 0, pullX, pullY));
        }
    }

    private void fluidResistanceForce() {
        double relativeVelocityX = velocityX - world.getCurrentX(centerX, centerY);
        double relativeVelocityY = velocityY - world.getCurrentY(centerX, centerY);
        double dragX = -Math.signum(relativeVelocityX) * World.FLUID_RESISTANCE * radius
            * sqr(relativeVelocityX);
        double dragY = -Math.signum(relativeVelocityY) * World.FLUID_RESISTANCE * radius
            * sqr(relativeVelocityY);
        forceX += dragX;
        forceY += dragY;
        if (forces != null) {
            forces.add(new Force(0, 0, dragX, dragY));
        }
    }

    private void wallCollisionForces() {
        double leftOverlap = radius - centerX;
        if (leftOverlap > 0) {
            double wallForce = CellCellCollision.SPRING_CONSTANT * leftOverlap;
            forceX += wallForce;
            if (forces != null) {
                forces.add(new Force(0 /*-radius*/, 0, wallForce, 0));
            }
            totalOverlap += leftOverlap;
        }

        double rightOverlap = centerX + radius - world.getWidth();
        if (rightOverlap > 0) {
            double wallForce = -CellCellCollision.SPRING_CONSTANT * rightOverlap;
            forceX += wallForce;
            if (forces != null) {
                forces.add(new Force(0 /* radius */, 0, wallForce, 0));
            }
            totalOverlap += rightOverlap;
        }

        double topOverlap = radius - centerY;
        if (topOverlap > 0) {
            double wallForce = CellCellCollision.SPRING_CONSTANT * topOverlap;
            forceY += wallForce;
            if (forces != null) {
                forces.add(new Force(0, 0 /*-radius*/, 0, wallForce));
            }
            totalOverlap += topOverlap;
        }

        double bottomOverlap = centerY + radius - world.getHeight();
        if (bottomOverlap > 0) {
            double wallForce = -CellCellCollision.SPRING_CONSTANT * bottomOverlap;
            forceY += wallForce;
            if (forces != null) {
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
                if (forces != null) {
                    forces.add(new Force(0, 0, -pair.getForceX(), -pair.getForceY()));
                }
            } else {
                forceX += pair.getForceX();
                forceY += pair.getForceY();
                if (forces != null) {
                    forces.add(new Force(0, 0, pair.getForceX(), pair.getForceY()));
                }
            }
            totalOverlap += pair.getOverlap();
        }
    }

    void move() {
        velocityX += forceX / mass;
        velocityY += forceY / mass;

        // TODO simpler check before doing this one? e.g. abs(vx) + abs(vy) > max/2?
        double speedSquared = sqr(velocityX) + sqr(velocityY);
        if (speedSquared > sqr(MAX_SPEED)) {
            double speed = Math.sqrt(speedSquared);
            velocityX *= MAX_SPEED / speed;
            velocityY *= MAX_SPEED / speed;
        }

        centerX += velocityX;
        centerY += velocityY;
    }

    // -- util --

    private void radiiToAreas() {
        fatArea = Math.PI * sqr(fatRadius);
        photosyntheticRingArea = Math.PI * sqr(photosyntheticRingOuterRadius) - fatArea;
    }

    private void areasToRadii() {
        fatRadius = Math.sqrt(fatArea / Math.PI);
        photosyntheticRingOuterRadius = Math.sqrt(sqr(fatRadius) + photosyntheticRingArea / Math.PI);
        setRadius(photosyntheticRingOuterRadius);
    }

    private void setRadius(double value) {
        radius = value;
        mass = MASS_PER_AREA * Math.PI * sqr(radius);
    }

    private static double sqr(double value) {
        return value * value;
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

    public final void setRecordForces(boolean value) {
        forces = value ? new ArrayList<>() : null;
    }

    public final List<Force> getForces() {
        return forces;
    }

    public final void setPulled(boolean value) {
        pulled = value;
    }

    public final void setPullPoint(double x, double y) {
        pullPointX = x;
        pullPointY = y;
    }
}
