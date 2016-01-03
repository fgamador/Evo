package fga.evo.model;

import java.util.HashMap;
import java.util.Map;

public class RegisteredParameters {
    private static Map<String, DoubleParameter> parameters = new HashMap<>();

    static {
        register("Ball.speedLimit", Ball.speedLimit);
        register("Ball.overlapForceFactor", Ball.overlapForceFactor);
        register("Ball.dampingForceFactor", BallPairForces.dampingForceFactor);
        register("Ball.overlapAccumulatorRetentionRate", Ball.overlapAccumulatorRetentionRate);
        register("Drag.dragFactor", Drag.dragFactor);
        register("Illumination.maxIntensity", Illumination.maxIntensity);
        register("Puller.forceFactor", Puller.forceFactor);
        register("Weight.gravity", Weight.gravity);
        register("Weight.fluidDensity", Weight.fluidDensity);

        register("FloatRing", FloatRing.parameters);
        register("PhotoRing", PhotoRing.parameters);
    }

    private static void register(String prefix, TissueRingParameters parameters) {
        register(prefix + ".tissueDensity", parameters.tissueDensity);
        register(prefix + ".growthCost", parameters.growthCost);
        register(prefix + ".maintenanceCost", parameters.maintenanceCost);
        register(prefix + ".shrinkageYield", parameters.shrinkageYield);
        register(prefix + ".maxGrowthRate", parameters.maxGrowthRate);
    }

    public static void register(String name, DoubleParameter parameter) {
        parameters.put(name, parameter);
    }

    public static DoubleParameter get(String name) {
        return parameters.get(name);
    }

    public static void revertToDefaultValues() {
        parameters.values().forEach(DoubleParameter::revertToDefaultValue);
    }
}
