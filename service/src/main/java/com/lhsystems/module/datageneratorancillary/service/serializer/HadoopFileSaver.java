package com.lhsystems.module.datageneratorancillary.service.serializer;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.util.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HadoopFileSaver {

    /*** Logger. ***/
    private static final Logger log = LoggerFactory.getLogger(HadoopFileSaver.class);


    private static String hdfsuri = "";

    public static Configuration initializeHadoop() {
        final Configuration conf = new Configuration();

        initializeHdfsUrl();

        conf.set("fs.defaultFS", hdfsuri);
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//        System.setProperty("HADOOP_USER_NAME", "root");
//        System.setProperty("hadoop.home.dir", "/");

        return conf;
    }

    private static void initializeHdfsUrl() {
        final String hdfsEnvirommentParam = System.getenv("HDFS_URL");
        if(StringUtils.isNotBlank(hdfsEnvirommentParam)) {
            if(hdfsEnvirommentParam.startsWith("hdfs://")) {
                hdfsuri = hdfsEnvirommentParam;
            } else {
                hdfsuri = "hdfs://" + hdfsEnvirommentParam + ":8020";
            }
        } else {
            hdfsuri = "hdfs://hdfs:8020";
        }
    }


    @Nullable
    private static FileSystem getFileSystem(final Path fileToDelete) {
        try {
            final FileSystem fs = FileSystem.get(URI.create(hdfsuri), initializeHadoop());
            //if (fs.exists(fileToDelete)) { fs.delete(fileToDelete, true); }
            return fs;
        } catch (IOException e) {
            log.error("Cannot get hadoop file system", e);
        }
        return null;
    }


    static <T> void saveEntitiesList(final List<T> entities, final String fileName, final Class<T> serializedClass) {
        final Path file = new Path(hdfsuri + "/" + fileName + System.currentTimeMillis() );
        final FileSystem fs = getFileSystem(file);
        if (Objects.isNull(fs)) {
            return;
        }

        final CsvMapper mapper = new CsvMapper();
        final CsvSchema schema = mapper.schemaFor(serializedClass).withHeader().withColumnSeparator(';');


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(fs.create(file), StandardCharsets.UTF_8))) {
            mapper.writer(schema).writeValue(writer, entities);
        } catch (IOException e) {
            log.error("Cannot write to hadoop file", e);
        }
    }
}
