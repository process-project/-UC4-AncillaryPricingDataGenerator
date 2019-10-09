package com.lhsystems.module.datageneratorancillary.service;

import com.lhsystems.module.datageneratorancillary.service.data.Airport;
import com.lhsystems.module.datageneratorancillary.service.data.BaggageClass;
import com.lhsystems.module.datageneratorancillary.service.data.BaggageLimits;
import com.lhsystems.module.datageneratorancillary.service.data.BaggagePricing;
import com.lhsystems.module.datageneratorancillary.service.data.BaggageSize;
import com.lhsystems.module.datageneratorancillary.service.data.Compartment;
import com.lhsystems.module.datageneratorancillary.service.data.Flight;
import com.lhsystems.module.datageneratorancillary.service.data.Market;
import com.lhsystems.module.datageneratorancillary.service.data.Product;
import com.lhsystems.module.datageneratorancillary.service.data.Route;
import com.lhsystems.module.datageneratorancillary.service.data.SeatGroup;
import com.lhsystems.module.datageneratorancillary.service.data.Service;
import com.lhsystems.module.datageneratorancillary.service.data.Tariff;
import com.lhsystems.module.datageneratorancillary.service.generator.configuration.FlightConfiguration;
import com.lhsystems.module.datageneratorancillary.service.generator.core.FlightGenerator;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

/**
 * Test <code>FlightGenerator</code>.
 *
 * @author REJ
 * @version $Revision: 1.10 $
 */
@RunWith(MockitoJUnitRunner.class)
public class FlightGeneratorTest {

    /** The minimal date for testing. */
    private static final LocalDate MIN_DATE = LocalDate.of(2017, 4, 6);

    /** The maximal date for testing. */
    private static final LocalDate MAX_DATE = LocalDate.of(2018, 7, 9);

    private static final int SAMPLE_SIZE = 200;
    /** some tariff. */
    private Tariff tariff;

    /**
     * Instantiates a new flight generator test.
     */
    public FlightGeneratorTest() {
    }

    /**
     * sets up the tests.
     */
    @Before
    public void setUp(){
        final BaggageSize baggageSize = new BaggageSize(3, 3, 3, 3);
        final BaggageLimits baggageLimits = new BaggageLimits(
                baggageSize,
                3,
                3);
        final BaggagePricing baggagePricing = new BaggagePricing(3, 3, 3);
        final BaggageClass baggageClass = new BaggageClass(
                "baggageClass",
                1,
                baggageLimits,
                baggagePricing);
        final Compartment compartment = new Compartment('N', "name");
        final List<BaggageClass> baggageClasses = new ArrayList<>();
        baggageClasses.add(baggageClass);
        final Map<BaggageClass, Integer> includedBags = new HashMap<>();
        includedBags.put(baggageClass, 1);
        final SeatGroup seatGroup = new SeatGroup("seatGroup", 1, 1);
        final List<Service> services = new ArrayList<>();
        services.add(seatGroup);
        services.addAll(baggageClasses);
        final Product product = new Product(
                "product",
                compartment,
                services,
                includedBags);
        tariff = new Tariff(3, product, Market.CONTINENTAL);
    }

    /**
     * Checks if for each flight in a list generated by a
     * <code>flightGenerator</code> the destination is different to the origin
     *
     * @param flights
     *            to be checked
     * @return true, if the origin is not the destination
     */
    private boolean checkAirports(final List<Flight> flights){
        boolean result = true;
        for (final Flight flight : flights) {
            if (flight.getRoute().getOriginAirport() == flight.getRoute().getDestinationAirport()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Checks if a list of flights generated by a <code>flightGenerator</code>
     * has dates that lie between the specified dates.
     *
     * @param flights
     *            to be checked
     * @return true, if departure is between <code>MIN_DATE</code> and
     *         <code>MAX_DATE</code>
     */
    private boolean checkDates(final List<Flight> flights) {
        boolean result = true;
        for (final Flight flight : flights) {
            if (flight.getDepartureDate().compareTo(MIN_DATE) < 0
                    || flight.getDepartureDate().compareTo(MAX_DATE) > 0) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Checks if a list of flights generated by a <code>flightGenerator</code>
     * has unique FlightNumbers.
     *
     * @param flights
     *            to be checked
     * @return true, if flight numbers are unique
     */
    private boolean checkFlightNumbers(final List<Flight> flights) {
        final Set<Long> usedFlightNumbers = new HashSet<>();
        boolean result = true;
        for (final Flight flight : flights) {
            if (!usedFlightNumbers.add(flight.getFlightNumber())) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Tests <code>FlightGenerator.generateFlights()</code> by generating a
     * number of flights and checking if they suffice the checks.
     */
    @Test
    public final void testGenerateFlights() {
        final List<Airport> airports = new ArrayList<>();
        final List<Tariff> tariffs = new ArrayList<>();
        tariffs.add(tariff);
        airports.add(new Airport("TAD","Test Airport Domestic", Market.DOMESTIC));
        airports.add(new Airport("TDO", "Test Airport Domestic2", Market.DOMESTIC));
        airports.add(new Airport("TAC","Test Airport Continental", Market.CONTINENTAL));
        airports.add(new Airport("TAI","Test Airport Intercontinental", Market.INTERCONTINENTAL));
        final List<Route> routes = new ArrayList<>();
        routes.add(new Route(airports.get(0), airports.get(1)));
        routes.add(new Route(airports.get(2), airports.get(1)));
        routes.add(new Route(airports.get(1), airports.get(2)));
        final FlightConfiguration flightConfiguration = new FlightConfiguration();
        flightConfiguration.setMaximumFlightDate(
                Date.from(MAX_DATE.atStartOfDay().toInstant(ZoneOffset.UTC)));
        flightConfiguration.setMinimumFlightDate(
                Date.from(
                        MIN_DATE.atStartOfDay().toInstant(ZoneOffset.UTC)));
        flightConfiguration.setMaximumNumberTariffs(4);
        flightConfiguration.setMinimumNumberTariffs(1);
        final FlightGenerator flightGenerator = new FlightGenerator(
                routes,
                tariffs,
                flightConfiguration);
        final List<Flight> flights = flightGenerator.generateList(
                SAMPLE_SIZE);
        assertTrue(checkAirports(flights));
        assertTrue(checkDates(flights));
        assertTrue(checkFlightNumbers(flights));
    }
}