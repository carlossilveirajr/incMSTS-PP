package postprocessing;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * @brief Class are used to read and manipulate ontology.
 * 
 *        This class has an object Model, that keep the ontology and allow for
 *        manipulating that. This object is provided by Jena interface. So, to
 *        use this class is necessary having Jena installed. It can be
 *        downloaded at http://jena.sourceforge.net/ (last access: February 13,
 *        2012).
 * 
 *        So, with that class, the ontology can be read the information in that
 *        can be used. The hole information extrated by the ontology here is the
 *        size of classes and the superclasses to using at the generation.
 * 
 *        This class are based on NARFO* algorithm, the implementation of it in
 *        Java. However, same modification are implemented to keep it easy to
 *        use.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public class OntologyReader {

	/**
	 * Ontology name.
	 */
	private String ontName;

	/**
	 * Ontology name plus "#".
	 */
	private String ontNamespace;

	/**
	 * Path of ontology with prefix "file:".
	 */
	private String ontLocation;

	/**
	 * Object to represent ontology.
	 */
	private OntModel ontModel;

	/**
	 * Constructor to read the ontology.
	 * 
	 * @param ontName
	 *            Name of the ontology
	 * @param ontLocation
	 *            Path of the ontology without "file:"
	 */
	public OntologyReader(String ontName, String ontLocation) {

		try {
			this.ontName = ontName;
			this.ontLocation = "file:" + ontLocation;
			this.ontNamespace = ontName + "#";

			// reading the ontology model
			this.ontModel = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
			OntDocumentManager dm = this.ontModel.getDocumentManager();
			dm.addAltEntry(this.ontName, this.ontLocation);
			this.ontModel.read(this.ontName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the number of instances a class has counting just one time each
	 * element.
	 * 
	 * @param className
	 *            Name of the class
	 * @return Size of the class
	 */
	public int getClassSize(String className) {

		return this.ontModel.getOntClass(this.ontNamespace + className)
				.listInstances().toSet().size();
	}

	/*
	 * The box said 'Requires Windows Vista or better'. So I installed LINUX.
	 * 
	 * Computers are like air conditioners: they stop working when you open
	 * Windows.
	 */

	/**
	 * Getting the super classes of a class.
	 * 
	 * @param individualName
	 *            The individual name
	 * @return Vector of super classes
	 */
	public Vector<String> getPatterns(String individualName) {
		Vector<String> result = new Vector<String>();
		Iterator j;

		Individual individual = ontModel.getIndividual(this.ontNamespace
				+ individualName);

		if (individual != null) {

			if (individual.isIndividual())
				j = individual.listOntClasses(true);
			else {
				OntClass ontClass = ontModel.getOntClass(this.ontNamespace
						+ individualName);
				j = ontClass.listSuperClasses(true);
			}

			while (j.hasNext()) {

				OntClass domain = (OntClass) ((RDFNode) j.next())
						.as(OntClass.class);

				if (!(domain.getLocalName().equals("NamedIndividual")))
					result.add(domain.getLocalName());
			}
		}

		return result;
	}

	/**
	 * Just for tests.
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {

/*		OntologyReader ontReader = new OntologyReader(
				"http://www.dc.ufscar.br/stimuliequivalence",
				"filesToTest/ontology.xml");

		String classname = "CCBehavior";
		String instancename = "CCBehaviorTrain";
*/
		OntologyReader ontReader = new OntologyReader(
				"http://www.semanticweb.org/ontologies/2013/0/feijaoRiver.owl",
				"feijaoRiver.owl");

		String classname = "Rainfall";
		String instancename = "Flow_5";
		
		System.out.println("Size of the class " + classname + " = "
				+ ontReader.getClassSize(classname));
		Vector<String> superClasses = ontReader.getPatterns(instancename);
		Enumeration<String> e = superClasses.elements();
		while (e.hasMoreElements()) {
			System.out.println((String) e.nextElement());
		}

		/*
		 * Results:
		 * 
		 * Size of the class CCBehavior = 6
		 * 
		 * CCBehavior
		 */
	}

}
