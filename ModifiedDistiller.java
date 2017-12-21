package edu.montana.gsoc.msusel.moddistill;

import com.google.common.collect.Lists;
import edu.montana.gsoc.msusel.quamoco.distiller.DistilledGraphCreator;
import edu.montana.gsoc.msusel.quamoco.distiller.ModelDistiller;
import edu.montana.gsoc.msusel.quamoco.graph.edge.Edge;
import edu.montana.gsoc.msusel.quamoco.graph.node.Node;
import edu.montana.gsoc.msusel.quamoco.io.QMReader;
import edu.montana.gsoc.msusel.quamoco.model.qm.QualityModel;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class uesed to control the processing graph distillation process for Quamoco Based Model plugins
 *
 * @author Isaac Griffith
 */
public class ModifiedDistiller {

    private static final Logger LOG = LoggerFactory.getLogger(ModifiedDistiller.class);
    private List<QualityModel> models;
    private DirectedSparseGraph<Node, Edge> graph;

    /**
     * method to construct the processing graphs for the named models.
     *
     * @param filenames an array of strings which are the names of the model files to be used to construct the processing graph. This list should be organized such that the most abstract model (i.e., root) is the first item in the array.
     */
    public void buildGraph(String... filenames) {
        models = readInQualityModels(filenames);
        DistilledGraphCreator creator = new DistilledGraphCreator();
        graph = creator.buildGraph(models);
    }

    /**
     * Controls the reading of the graphs into the system to extract the processing graph from.
     * 
     * @param args an array of strings which are the names of the model files to be used to construct the processing graph. This list should be organized such that the most abstract model (i.e., root) is the first item in the array.
     */
    private List<QualityModel> readInQualityModels(String... args) {
        final QMReader qmread = new ModifiedQMReader();
        final List<QualityModel> models = Lists.newArrayList();
        if (args != null)
        {
            try
            {
                for (final String arg : args)
                {
                    qmread.read(arg);
                    models.add(qmread.getModel());
                }
            }
            catch (FileNotFoundException | XMLStreamException e)
            {
                ModifiedDistiller.LOG.warn(e.getMessage(), e);
            }
        }
        return models;
    }

    /**
     * @return List of all loaded quality models
     */
    public List<QualityModel> getModelList()
    {
        return models;
    }

    /**
     * @return The processing graph.
     */
    public DirectedSparseGraph<Node, Edge> getGraph()
    {
        return graph;
    }

    /**
     * Executable main method which provides a basic smoke test for this program.
     */
    public static void main(String args[]) {
        ModifiedDistiller md = new ModifiedDistiller();

        md.buildGraph("root", "object", "csharp");

        DirectedSparseGraph<Node, Edge> graph = md.getGraph();
        System.out.println("Graph: ");
        System.out.println("\tNodes: " + graph.getVertexCount());
        System.out.println("\tEdges: " + graph.getEdgeCount());

        System.out.println("Models");
        for (QualityModel m : md.getModelList()) {
            System.out.println("\t" + m.getName());
        }
    }
}
