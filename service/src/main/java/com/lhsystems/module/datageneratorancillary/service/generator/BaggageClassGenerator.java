package com.lhsystems.module.datageneratorancillary.service.generator;

import com.lhsystems.module.datageneratorancillary.service.ExtendedRandom;
import com.lhsystems.module.datageneratorancillary.service.data.BaggageClass;
import com.lhsystems.module.datageneratorancillary.service.data.BaggageLimits;
import com.lhsystems.module.datageneratorancillary.service.data.BaggagePricing;

import java.util.List;

/**
 * Generates baggage classes randomly.
 *
 * @author REJ
 * @version $Revision: 1.10 $
 */
public final class BaggageClassGenerator extends DataGenerator {

    /** The baggage limits from which one is chosen per baggage class. */
    private final List<BaggageLimits> baggageLimits;

    /**
     * The baggage pricing models from which one is chosen per baggage class.
     */
    private final List<BaggagePricing> baggagePricingModels;

    /**
     * Instantiates a new baggage class generator.
     *
     * @param startId
     *            the start id
     * @param paramRandom
     *            the param random
     * @param paramBaggageLimits
     *            the param baggage limits
     * @param paramBaggagePricingModels
     *            the param baggage pricing models
     */
    public BaggageClassGenerator(final Long startId,
            final ExtendedRandom paramRandom,
            final List<BaggageLimits> paramBaggageLimits,
            final List<BaggagePricing> paramBaggagePricingModels) {
        super(startId, paramRandom);
        baggageLimits = paramBaggageLimits;
        baggagePricingModels = paramBaggagePricingModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaggageClass generate(final long id) {
        final BaggageLimits limits = getRandom().getOneRandomElement(baggageLimits);
        final BaggagePricing pricing = getRandom().getOneRandomElement(
                baggagePricingModels);
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(
                Double.toString(limits.getWeightMax())
                + "kg_");
        nameBuilder.append("f" + Double.toString(pricing.getFirstPrice()));
        nameBuilder.append("s" + Double.toString(pricing.getSecondPrice()));
        nameBuilder.append(
                "a" + Double.toString(pricing.getAdditionalPrice()));

        return new BaggageClass(id, nameBuilder.toString(), limits, pricing);
    }

}
