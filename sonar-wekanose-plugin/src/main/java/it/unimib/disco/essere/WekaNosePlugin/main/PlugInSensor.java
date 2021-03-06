package it.unimib.disco.essere.WekaNosePlugin.main;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorDescriptor;
import it.unimib.disco.essere.WekaNosePlugin.datasetgenerator.GenerateRows;
import it.unimib.disco.essere.WekaNosePlugin.jcoanalysis.DependencyRecruiter;
import it.unimib.disco.essere.WekaNosePlugin.jcoanalysis.JCodeOdorDBHandler;
import it.unimib.disco.essere.WekaNosePlugin.jcoanalysis.JCodeOdorExecutor;
import it.unimib.disco.essere.WekaNosePlugin.outlineanalysis.CorrectDatasetGenerator;
import it.unimib.disco.essere.WekaNosePlugin.outlineanalysis.OutlineExecutor;
import java.io.File;
import java.util.Arrays;
import org.sonar.api.batch.fs.FileSystem;

public class PlugInSensor implements Sensor {

	private FileSystem fs;
	private WorkSpaceHandler workspace;
	private int timeCycle = 0;
	private boolean checkTime = true;

	@Override
	public void describe(SensorDescriptor descriptor) {

	}

	@Override
	public void execute(org.sonar.api.batch.sensor.SensorContext context) {

	}

	public PlugInSensor(FileSystem fs) throws Exception {

		this.fs = fs;
		this.workspace = new WorkSpaceHandler(getBaseDir(), false);
		System.out.println("[INFO] Obtaining project dependency...");
		DependencyRecruiter dr = new DependencyRecruiter(workspace);
		String SQLiteFilePath = workspace.getJCodeOdorAnalysisPath() + "/JCOAnalysis.SQLite";
		String arg = "-source " + getBaseDir() + " -lib " + dr.getDependencyPath() + " -output " + SQLiteFilePath;
		System.out.println("[INFO] Generating JCodeOdorAnalysis...");
		new JCodeOdorExecutor(workspace.getJCodeOdorPath(), Arrays.asList(arg.split(" ")));
		while (!new File(SQLiteFilePath).exists()) {

			if (timeCycle >= 600 && checkTime) {

				checkTime = false;
				System.out.println(
						"[SEVERE]----------------------------------------------------------------------------------------------------------------------------------------------\r\n"
								+ "Be aware that if even one dependency was not specified the computation will remain stuck. \r\n"
								+ "Therefore, please check again that all dependencies, that are not included in the pom.xml, have been specified \r\n"
								+ "in the file \"sonarqube-X.X\\extensions\\plugins\\sonar-wekanose-plugin-tools\\AddExternalDependencies.properties\" \r\n"
								+ "(this is an automated printed message, if you are analysing a large project, it may not be addressed to you)\r\n"
								+ "------------------------------------------------------------------------------------------------------------------------------------------------------");
			}

			timeCycle++;
			Thread.sleep(100);
		}
		System.out.println("[INFO] Selecting the correct metrics...");
		new JCodeOdorDBHandler(SQLiteFilePath);
		System.out.println("[INFO] Generating class and method datasets...");
		new GenerateRows(workspace);
		System.out.println("[INFO] Analysing datasets with basic algoritms...");
		if (new File(workspace.getAlgorithms() + "/DataClass_class.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/ClassDataset.csv",
					workspace.getAlgorithms() + "/DataClass_class.model", "_class");
		}
		if (new File(workspace.getAlgorithms() + "/GodClass_class.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/ClassDataset.csv",
					workspace.getAlgorithms() + "/GodClass_class.model", "_class");
		}
		if (new File(workspace.getAlgorithms() + "/FeatureEnvy_method.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/MethodDataset.csv",
					workspace.getAlgorithms() + "/FeatureEnvy_method.model", "_method_1");
		}
		if (new File(workspace.getAlgorithms() + "/LongMethod_method.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/MethodDataset.csv",
					workspace.getAlgorithms() + "/LongMethod_method.model", "_method_1");
		}
		if (new File(workspace.getAlgorithms() + "/LongParameterList_method.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/MethodDataset.csv",
					workspace.getAlgorithms() + "/LongParameterList_method.model", "_method_2");
		}
		if (new File(workspace.getAlgorithms() + "/SwitchStatement_method.model").exists()) {
			new CorrectDatasetGenerator(workspace, workspace.getDatasetPath() + "/MethodDataset.csv",
					workspace.getAlgorithms() + "/SwitchStatement_method.model", "_method_2");
		}
		System.out.println("[INFO] Analyzing dataset with yours algorithms...");
		File index = new File(workspace.getAlgorithms());
		String[] entries = index.list();
		for (String algorithm : entries) {

			if (!algorithm.equalsIgnoreCase("DataClass_class.model")
					&& !algorithm.equalsIgnoreCase("GodClass_class.model")
					&& !algorithm.equalsIgnoreCase("FeatureEnvy_method.model")
					&& !algorithm.equalsIgnoreCase("LongMethod_method.model")
					&& !algorithm.equalsIgnoreCase("LongParameterList_method.model")
					&& !algorithm.equalsIgnoreCase("SwitchStatement_method.model")) {

				new OutlineExecutor(workspace, algorithm);
			}
		}

		System.out.println("[INFO] Completed!");
		System.out.println("[INFO] Deleting useless files...");
		workspace.deleteFolder(workspace.getJCodeOdorAnalysisPath());
		workspace.deleteFolder(workspace.getCorrectDatasetPath());
		workspace.deleteFolder(workspace.getDatasetPath());
		System.out.println("[INFO] Done!");
	}

	public String getBaseDir() {

		return fs.baseDir().getAbsolutePath();
	}

	public WorkSpaceHandler getWorkspace() {

		return workspace;
	}
}