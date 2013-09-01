package name.etapic.codejam;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

final class MainXml {

	public static void main(final String[] args) throws Exception {
		new MainXml("config.xml").execute();
	}

	private final String configFilename;

	private MainXml(final String configFilename) {
		this.configFilename = configFilename;
	}

	private void execute() throws Exception {
		final List<ProblemConfig> problemConfigs = parseXmlFile(configFilename);
		new CodeJam(problemConfigs).execute();
	}

	private List<ProblemConfig> parseXmlFile(final String configFilename) throws Exception {
		final InputStream stream = MainXml.class.getResourceAsStream(configFilename);
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(stream);
		doc.getDocumentElement().normalize();
		final NodeList problemNodes = doc.getElementsByTagName("problem");
		final List<ProblemConfig> problemConfigs = new ArrayList<ProblemConfig>();
		for (int i = 0; i < problemNodes.getLength(); i++) {
			final Node problemNode = problemNodes.item(i);
			if (problemNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element problemElem = (Element) problemNode;
				final String problemName = problemElem.getAttribute("name");
				final String packageName = problemElem.getAttribute("package");
				final String url = problemElem.getAttribute("url");
				final List<SolverConfig> solverConfigs = parseSolverConfigs(problemElem);
				problemConfigs.add(new ProblemConfig(problemName, packageName, url, solverConfigs));
			}
		}
		return problemConfigs;
	}

	private List<SolverConfig> parseSolverConfigs(final Element problemElem) {
		final NodeList solverNodes = problemElem.getElementsByTagName("solver");
		final List<SolverConfig> solverConfigs = new ArrayList<SolverConfig>();
		for (int i = 0; i < solverNodes.getLength(); i++) {
			final Node solverNode = solverNodes.item(i);
			if (solverNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element solverElem = (Element) solverNode;
				final String className = solverElem.getAttribute("className");
				final NodeList datasetNodes = solverElem.getElementsByTagName("dataset");
				final List<String> datasetNames = new ArrayList<String>();
				for (int j = 0; j < datasetNodes.getLength(); j++) {
					final Node datasetNode = datasetNodes.item(j);
					if (datasetNode.getNodeType() == Node.ELEMENT_NODE) {
						final Element datasetElem = (Element) datasetNode;
						datasetNames.add(datasetElem.getAttribute("name"));
					}
				}
				solverConfigs.add(new SolverConfig(className, datasetNames));
			}
		}
		return solverConfigs;
	}
}
