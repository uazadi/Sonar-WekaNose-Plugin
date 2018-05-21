package org.sonar.stage.plugin.checks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol.MethodSymbol;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.stage.plugin.main.Utils;

@Rule(key = "FeatureEnvy", name = "Feature Envy", description = "In order to train the machine learning algorithm the following definition has been used to formalize the concept:  The Feature Envy Code Smell refers to methods that use more much more data from other classes than from their own class. A Feature Envy tends to use many attributes of other classes (considering also attributes accessed through accessor methods), to use more attributes from other classes than from its own class, and to use many attributes from few different classes.", tags = {
		"codesmells" })

public class FeatureEnvy extends IssuableSubscriptionVisitor {

	private List<String> lines;
	private boolean instantiate = true;

	public List<Kind> nodesToVisit() {

		System.out.println("[INFO] Reading Feature Envy Prediction...");
		return Utils.loadTree(true);
	}

	public void visitNode(Tree tree) {

		if (instantiate) {

			lines = new ArrayList<>();
			try {

				lines = Utils.loadPredicted("Predicted_FeatureEnvy.csv");
			} catch (IOException | InterruptedException e) {

				e.printStackTrace();
			}

			instantiate = false;

		}

		MethodTree method = (MethodTree) tree;
		MethodSymbol symbol = method.symbol();

		List<String> splittedLine = new ArrayList<>();

		for (String line : lines) {

			splittedLine = Arrays.asList(line.split(","));
			String bool = splittedLine.get(splittedLine.size() - 1);
			if (bool.equalsIgnoreCase("true")) {

				String methodClass = Utils.extractClass(splittedLine.get(3));

				if (methodClass.equalsIgnoreCase(symbol.enclosingClass().name().toString())) {

					String methodName = Utils.extractMethodName(splittedLine.get(4));

					if (methodName.equalsIgnoreCase(method.simpleName().toString())) {

						List<String> methodParameters = new ArrayList<>();
						methodParameters = Utils.extractMethodParameters(line);

						int count = 0;
						int countDeleted = 0;
						for (VariableTree sonarParameter : method.parameters()) {

							for (String csvParameter : methodParameters) {
								if (sonarParameter.symbol().type().toString().equalsIgnoreCase(csvParameter)) {

									methodParameters.remove(csvParameter);
									countDeleted++;
									break;
								}
							}
							count++;
						}

						if (count == countDeleted) {

							lines.remove(line);
							reportIssue(method.simpleName(), "Feature Envy smell detected!");
							break;
						}
					}
				}
			}
		}
	}
}