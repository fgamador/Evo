package fga.evo.model;

/**
 * A ring gradient around a cell that affects other cells.
 */
public abstract class ChemicalHalo {
    // intrusion = haloRadius + cellRadius - centerSeparation;
    // overlap = Math.min(cellRadius*2, intrusion);
    // averageConcentrationRadius = haloRadius - intrusion + overlap/2;
}
