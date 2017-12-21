package edu.montana.gsoc.msusel.sonar.quamoco;

import com.google.common.annotations.VisibleForTesting;
import edu.montana.gsoc.msusel.quamoco.io.QMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * An extension to the QMReader class in the Quamoco Framework's io package. The goal of this is to setup the base directory where the models are stored.
 *
 * @author Isaac Griffith
 * @author David Rice
 */
public class ModifiedQMReader extends QMReader {

    private static final Logger LOG = LoggerFactory.getLogger(ModifiedQMReader.class);

    /**
     * Retrieves an input stream for the file with the given name. If the file
     * cannot be read, then an attempt to read the file from the JAR is made.
     *
     * @param qm
     *            File name to construct a stream for.
     * @return An input stream for reading the file with the given name.
     */
    @VisibleForTesting
    InputStream getInputStream(final String qm)
    {
        final Path file = Paths.get(qm);
        if (Files.exists(file))
        {
            try
            {
                return Files.newInputStream(file, StandardOpenOption.READ);
            }
            catch (final IOException e)
            {
                ModifiedQMReader.LOG.warn(e.getMessage(), e);
                return ModifiedQMReader.class.getResourceAsStream("/models/" + qm + ".qm"); // Note that you will need to update the "/models/" section to reflect the location in the resources section of src where the models are actually stored"
            }
        }
        else
        {
            return ModifiedQMReader.class.getResourceAsStream("/models/" + qm + ".qm"); // Note that you will need to update the "/models/" section to reflect the location in the resources section of src where the models are actually stored"
        }
    }
}
