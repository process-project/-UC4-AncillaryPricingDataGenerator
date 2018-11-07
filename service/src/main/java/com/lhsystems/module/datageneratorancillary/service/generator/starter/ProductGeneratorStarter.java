package com.lhsystems.module.datageneratorancillary.service.generator.starter;

import com.lhsystems.module.datageneratorancillary.service.data.BaggageClass;
import com.lhsystems.module.datageneratorancillary.service.data.Compartment;
import com.lhsystems.module.datageneratorancillary.service.data.Product;
import com.lhsystems.module.datageneratorancillary.service.generator.configuration.ProductConfiguration;
import com.lhsystems.module.datageneratorancillary.service.generator.core.ProductGenerator;
import com.lhsystems.module.datageneratorancillary.service.repository.CompartmentRepository;
import com.lhsystems.module.datageneratorancillary.service.repository.ProductRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Starts generating product entities and save them into database.
 *
 *
 * @author REJ
 * @version $Revision: 1.10 $
 */
@Service
public final class ProductGeneratorStarter {
    /** The repository used for saving products. */
    private final ProductRepository productRepository;

    /** The repository used for saving compartments. */
    private final CompartmentRepository compartmentRepository;

    /**
     * Instantiates a new product generator starer with injected repositories.
     *
     * @param productRepositoryParam
     *        repository responsible for crud operations on products entities
     * @param compartmentRepositoryParam
     *        repository responsible for crud operations on compartment entities
     */
    @Autowired
    public ProductGeneratorStarter(final ProductRepository productRepositoryParam,
            final CompartmentRepository compartmentRepositoryParam) {
        productRepository = productRepositoryParam;
        compartmentRepository = compartmentRepositoryParam;
    }

    /**
     * Generate products entities save them.
     *
     * @param baggageClasses
     *            the tariffs to be used for flight generation
     * @param compartments
     *            the compartments to be used for product generation
     * @param productConfiguration
     *            configures generation of products
     * @return the list of generated products
     */
    public List<Product> generateProductEntities(
            final List<Compartment> compartments,
            final List<BaggageClass> baggageClasses,
            final ProductConfiguration productConfiguration) {
        compartmentRepository.save(compartments);
        return generateProducts(
                compartments,
                baggageClasses,
                productConfiguration);
    }

    /**
     * Generate products entities and save them into database.
     *
     * @param compartments
     *        the compartments to be used for product generation
     * @param baggageClasses
     *        the baggage classes to be used for product generation
     * @param productConfiguration
     *            the product configuration
     * @return the list of generated products
     */
    private List<Product> generateProducts(
            final List<Compartment> compartments,
            final List<BaggageClass> baggageClasses,
            final ProductConfiguration productConfiguration) {
        final ProductGenerator productGenerator = new ProductGenerator(
                compartments,
                baggageClasses,
                productConfiguration);
        final List<Product> products = productGenerator.generateList(
                productConfiguration.getNumberProduct());
        compartmentRepository.save(compartments);
        productRepository.save(products);
        return products;
    }
}