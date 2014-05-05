/**
 * @package postprocessing
 * @brief Implements the post-processing model
 * 
 * This package use ontology to generalize the patterns found.
 */
package postprocessing;

import java.util.Vector;

import useful.SimplePattern;

/**
 * @brief This class implements the generalization of found patterns.
 * 
 *        This class works with same steps: first to inicialize the class we
 *        need to pass the ontology. After that, there is a method to set the
 *        patterns. With the ontology and the patterns it can be executed. The
 *        execution will generalized the found patterns, just that ones that can
 *        be generalized. This generalization is base on the knowledge in the
 *        ontology.
 * 
 *        To generalize this class combine pattern by pattern that are similar
 *        (closer). If the pattern could be generalized the patterns involved
 *        are marked and the generalized pattern are include with the patterns.
 *        After look at all pattern and marked that generalized that are deleted
 *        to rest just the generalized ones. If that the generalized pattern can
 *        be consulted.
 * 
 *        An exemple: s1 = < a b (c d)> and s2 = <a i (c d)> and s3 = <t j (c
 *        d)>. There is an ontology that sees a,t is son of x and b,i is son of
 *        y and i,j is son of w. Than: s4 = < a y (c d)> (s1, s2), s5 = < x w (c
 *        d)) > (s2, s3). It is impossible to make (s1,s3) because there is no
 *        comum father between b and j.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public class Generalizer {

	/**
	 * Patterns to generalize.
	 */
	private Vector<SimplePattern> patterns;

	/**
	 * Object to read and search things at the ontology.
	 */
	private OntologyReader ontReader;

	/**
	 * Find parent in comum between two item if there is at least one.
	 * 
	 * @param el1
	 *            First element
	 * @param el2
	 *            Second element
	 * @return Comum father between both
	 */
	protected String fatherInComum(String el1, String el2) {

		if (!el1.contains(" ") && !el2.contains(" ")) {

			Vector<String> father1 = this.ontReader.getPatterns(el1);

			Vector<String> father2 = this.ontReader.getPatterns(el2);

			for (int i = 0; i < father1.size(); ++i)
				if (father2.contains(father1.elementAt(i)))
					return father1.elementAt(i);
			
			/*sometimes the element can be the father*/
			if (father1.contains(el2))
				return el2;
			else if (father2.contains(el1))
				return el1;
		}

		return null;
	}

	/**
	 * This method try to generalized each sequences. To generalize two
	 * sequences it is necessary to try each couple of sequences. With there is
	 * a couple that can be generalized (has the same size) the algoritmo checks
	 * if for each diferente item in the sequences there is at least one comum
	 * father. If there is, all differents itens has, the sequences are
	 * generalized. At the end, the algorithm removes all redundant sequence.
	 */
	protected void generalizingPatterns() {

		int limit = this.patterns.size();
		for (int i = 0; i < limit; ++i) {
			for (int j = i + 1; j < limit; ++j) {
				
				//System.out.println("i = " + i + "; j = " + j );

				SimplePattern newPattern = new SimplePattern(
						this.patterns.elementAt(i));

				if (this.patterns.elementAt(i).getItemsets().size() == this.patterns
						.elementAt(j).getItemsets().size()) {

					/* trying generalization */
					for (int k = 0; k < newPattern.getItemsets().size(); ++k)
						if (this.patterns.elementAt(i).getItemset(k) !=
						    this.patterns.elementAt(j).getItemset(k)) {

							String father = this.fatherInComum(
									this.patterns.elementAt(i).getItemset(k), 
									this.patterns.elementAt(j).getItemset(k));

							if (father != null)
								newPattern.change(k, father);
							else {
								newPattern = null;
								break;
							}
						}

					if (newPattern != null) {
						newPattern.setSupport(
								this.patterns.elementAt(i).getSupport() +
								this.patterns.elementAt(j).getSupport()
						);
						
						this.patterns.elementAt(i).setCanDelete();
						this.patterns.elementAt(j).setCanDelete();

						if (!this.patterns.contains(newPattern))
							this.patterns.add(new SimplePattern(newPattern));
					}
				}
			}
		}

		/* Removing generalized patterns */
		for (int i = 0; i < this.patterns.size(); ++i)
			if (this.patterns.elementAt(i).isCanDelete()) {
				this.patterns.remove(i--);
				
				if (i < 0)
					i = 0;
			}
	}

	/**
	 * Constructor with three parameter. This method inicialize the ontology
	 * object, and set minGen.
	 * 
	 * @param ontName
	 *            Name of the ontology
	 * @param ontLocation
	 *            Path of the ontology
	 */
	public Generalizer(String ontName, String ontLocation) {

		this.patterns = new Vector<SimplePattern>();

		this.ontReader = new OntologyReader("http://www.semanticweb.org/ontologies/2013/0/" + ontName, ontLocation);
	}

/* 
 * This is hilarious, and also true: if a pizza has a radius 'z' and a 
 *  depth 'a' that pizza's volume can be defined Pi*z*z*a.
 */
	
	/**
	 * It receives the patterns.
	 * 
	 * @param patterns
	 *            String with all patterns
	 */
	public void setPatterns(String patterns) {

		String[] s = patterns.split("\n");

		for (String s1 : s) {

			String[] values = s1.split(" support: ");

			this.patterns.add(new SimplePattern(new String(values[0]),
					new Double(values[1])));
		}
	}

	/**
	 * Execute the post-processing after has all pattern, this method can
	 * process all patterns.
	 * 
	 * @return True if it was possible to generalize
	 */
	public boolean exec() {

		if (this.patterns.size() <= 1)
			return false;

		/*
		 * generalize the sequences.
		 */
		this.generalizingPatterns();

		return true;
	}

	/**
	 * Write the result on a string.
	 * 
	 * @return the patterns
	 */
	@Override
	public String toString() {
		String s = new String();

		for (int i = 0; i < this.patterns.size(); ++i)
			s += (this.patterns.elementAt(i).toString() + "\n");

		return s;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            unused.
	 */
	public static void main(String[] args) {

		String teste = "< rise coca water > support: 0.345 \n";

		teste += "< rise (coca water) > support: 0.345 \n";

		teste += "< (rise coca) water > support: 0.35 \n";

		teste += "< (rise coca water) > support: 0.345";

		Generalizer g = new Generalizer("feijaoRiver", 
				"/home/junior/workspace/stella/feijaoRiver.owl");

		g.setPatterns(teste);
		
		System.out.print(g.toString());

	}

}
