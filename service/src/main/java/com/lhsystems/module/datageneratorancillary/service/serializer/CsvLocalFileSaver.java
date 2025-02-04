package com.lhsystems.module.datageneratorancillary.service.serializer;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for writing to csv file.
 * @author REJ
 * @version $Revision: 1.10 $
 */
final class CsvLocalFileSaver {

    /*** Logger. ***/
    private static final Logger log = LoggerFactory.getLogger(CsvLocalFileSaver.class);

    static final CsvMapper mapper = new CsvMapper();

    /**
     * Private constructor.
     */
    private CsvLocalFileSaver(){

    }

    /**
     * Save entities to csv file.
     *
     * @param <T>             the type parameter
     * @param entities        the entities to save in csv file
     * @param fileName        the csv file name
     * @param serializedClass the serialized class to save in file
     */
    static <T> void saveEntitiesList(final List<T> entities, final String fileName, final Class<T> serializedClass) {
        long startTime = System.currentTimeMillis();
        log.info("Start saving entities locally. Filename: " + fileName);
        final CsvSchema schema = mapper.schemaFor(serializedClass).withHeader().withColumnSeparator(';');
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            mapper.writer(schema).writeValue(writer, entities);
        } catch (final IOException e) {
            log.error("Cannot write to csv file", e);
        }
        log.info("Saving file " + fileName + " locally completed successfully. It took " + (System.currentTimeMillis() - startTime)  + " ms");

    }
}
