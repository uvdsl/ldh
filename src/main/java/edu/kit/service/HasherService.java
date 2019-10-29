package edu.kit.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.hash.HashCode;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;
import org.springframework.stereotype.Service;

import cl.uchile.dcc.blabel.cli.LabelRDFGraph;
import cl.uchile.dcc.blabel.label.GraphColouring.HashCollisionException;
import cl.uchile.dcc.blabel.label.GraphLabelling.GraphLabellingArgs;
import cl.uchile.dcc.blabel.label.GraphLabelling.GraphLabellingResult;

/**
 * This class is a service to hash an rdf graph.
 */
@Service
public class HasherService {

	/**
	 * Hash the <b>{@literal Iterable<Node[]>}</b> graph, as shown in the paper.
	 * Returns a string containing each byte of {@link #asBytes}, in order, as a
	 * two-digit unsigned hexadecimal number in lower case."
	 * 
	 * @param graph
	 * @return Hash as string: two-digit hexadecimal representation of bytes
	 * @throws HashCollisionException
	 * @throws InterruptedException
	 */
	public String hashGraph(Iterable<Node[]> graph) throws InterruptedException, HashCollisionException {
		HashCode hash = this.hashRDFGraph(graph);
		return "0x" + hash.toString();
	}

	/**
	 * Hash an RDF graph
	 * 
	 * @param stmts some rdf graph
	 * @return HashCode of that graph
	 */
	private HashCode hashRDFGraph(Iterable<Node[]> stmts) throws InterruptedException, HashCollisionException {
		Collection<Node[]> graph = new LinkedList<>();
		stmts.forEach(triple -> graph.add(triple));
		final List<String> actual = new ArrayList<String>();

		GraphLabellingResult glr = LabelRDFGraph.labelGraph(graph, new Callback() {
			@Override
			public void startDocument() {
			}

			@Override
			public void endDocument() {
			}

			@Override
			public void processStatement(Node[] nx) {
				String triple = toN3(nx);
				// System.out.println(triple);
				actual.add(triple);
			}

			@Override
			protected void endDocumentInternal() {
			}

			@Override
			protected void processStatementInternal(Node[] nx) {
				String triple = toN3(nx);
				// System.out.println(triple);
				actual.add(triple);
			}

			@Override
			protected void startDocumentInternal() {
			}
		}, new GraphLabellingArgs(), "", true);

		HashCode hash = glr.getHashGraph().getGraphHash();
		// for(Node[] out:glr.getGraph()){ System.out.println(this.toN3(out)); }
		return hash;

	}

	/**
	 * @param ns node array to be converted to N3
	 * @return String value of N3 representation as one string.
	 */
	private String toN3(Node[] ns) {
		StringBuilder buf = new StringBuilder();
		for (Node n : ns) {
			buf.append(n.toString());
			buf.append(" ");
		}
		buf.append(".");
		return buf.toString();
	}

}
