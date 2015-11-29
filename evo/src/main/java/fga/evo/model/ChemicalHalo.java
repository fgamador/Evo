package fga.evo.model;

/**
 * Created by Franz on 8/17/2015.
 */
public abstract class ChemicalHalo {
    // intrusion = haloRadius + cellRadius - centerSeparation;
    // overlap = Math.min(cellRadius*2, intrusion);
    // averageConcentrationRadius = haloRadius - intrusion + overlap/2;
}
