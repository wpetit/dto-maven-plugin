package fr.maven.dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Mojo to generate DTO classes.
 * 
 * @goal dto
 * 
 * @phase generate-sources
 */
public class DTOMojo extends AbstractMojo {

	/**
	 * List of artifacts this plugin depends on. Used for resolving the DTO
	 * maven plugin.
	 * 
	 * @parameter expression="${plugin.artifacts}"
	 * @required
	 * @readonly
	 */
	List<Artifact> pluginArtifacts;

	/**
	 * List of artifacts the project depends on.
	 * 
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 * @readonly
	 */
	Set<Artifact> projectDependencies;

	/**
	 * Location of the project build directory.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Location of the DTO classes generation directory.
	 * 
	 * @parameter expression="${sourceRoot}"
	 *            default-value="${project.build.directory}/generated"
	 * @required
	 */
	private File generatedDirectory;

	/**
	 * List of classes we want to generate DTO for.
	 * 
	 * @parameter expression="${includeClasses}"
	 * 
	 * @required
	 */
	private List<String> includeClasses;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException {
		this.getLog().info("dto-maven-plugin execution");

		// check parameters
		if (!this.checkArgs()) {
			throw new MojoExecutionException(
					"Generation aborted due to previous errors.");
		}

		try {
			// Get classes to generate DTO for
			StringBuffer classes = new StringBuffer();
			for (String clazz : this.includeClasses) {
				classes.append(clazz + ";");
			}

			// Generate the process to launch
			String cmd = "java -classpath "
					+ this.getClasspath(this.outputDirectory) + " "
					+ DTOLauncher.class.getCanonicalName() + " "
					+ this.generatedDirectory + " " + classes;

			this.getLog().debug("dto-maven-plugin launching " + cmd);

			// Get process result
			int exitValue = this.runProcess(cmd);
			if (exitValue != 0) {
				this.getLog().error("The generation has failed.");
				throw new MojoExecutionException("The generation has failed.");
			}
		} catch (Exception e) {
			this.getLog().error("The generation has failed.", e);
			throw new MojoExecutionException("The generation has failed.", e);
		}
	}

	/**
	 * Create the classpath to set to java process to launch the generation.
	 * 
	 * @param directory
	 *            the directory that contains classes.
	 * @return the classpath result.
	 */
	protected String getClasspath(File directory) {
		this.getLog().debug("Begin classpath resolution");
		StringBuffer classpath = new StringBuffer();
		for (File clazz : this.getFiles(directory)) {
			classpath.append(clazz.getAbsolutePath() + File.pathSeparator);
		}
		for (Artifact artifact : this.pluginArtifacts) {
			classpath.append(artifact.getFile().getAbsolutePath()
					+ File.pathSeparator);
		}
		for (Artifact artifact : this.projectDependencies) {
			if (artifact.getFile() != null) {
				classpath.append(artifact.getFile().getAbsolutePath()
						+ File.pathSeparator);
			}
		}

		classpath.append(";.");
		this.getLog().debug("End classpath resolution");
		return classpath.toString();
	}

	/**
	 * Return all classes found in the directory given.
	 * 
	 * @param file
	 *            the directory where classes to found are.
	 * @return the list of classes found.
	 */
	protected List<File> getFiles(File file) {
		this.getLog().debug("Begin classes listing");
		List<File> classFiles = new ArrayList<File>();
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File childFile : file.listFiles()) {
					classFiles.addAll(this.getFiles(childFile));
				}
			} else if (file.getAbsolutePath().endsWith(".class")) {
				classFiles.add(file);
			}
		}
		this.getLog().debug("End classes listing");
		return classFiles;
	}

	/**
	 * Run the process of the given command.
	 * 
	 * @param cmd
	 *            the command to launch
	 * @return the process result.
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the current thread is interrupted by another thread while
	 *             it is waiting, then the wait is ended and an
	 *             InterruptedException is thrown.
	 */
	protected int runProcess(String cmd) throws IOException,
			InterruptedException {
		this.getLog().debug("Begin running process");
		Process process = Runtime.getRuntime().exec(cmd);

		// Redirect error stream to logs
		InputStream stdErr = process.getErrorStream();
		InputStreamReader isrErr = new InputStreamReader(stdErr);
		BufferedReader brErr = new BufferedReader(isrErr);
		String lineErr = null;
		while ((lineErr = brErr.readLine()) != null) {
			this.getLog().error(lineErr);
		}

		// Redirect input stream to logs
		InputStream stdIn = process.getInputStream();
		InputStreamReader isrIn = new InputStreamReader(stdIn);
		BufferedReader brIn = new BufferedReader(isrIn);
		String lineIn = null;
		while ((lineIn = brIn.readLine()) != null) {
			this.getLog().error(lineIn);
		}
		int exitValue = process.waitFor();

		brErr.close();
		brIn.close();

		this.getLog().debug("End running process");

		return exitValue;
	}

	/**
	 * Check configuration is valid.
	 * 
	 * @return <code>true</code> if the configuration is valid.
	 *         <code>false</code> otherwise.
	 */
	protected boolean checkArgs() {
		boolean argsValid = true;
		// Check there are classes to generate.
		if (this.includeClasses == null || this.includeClasses.isEmpty()) {
			this.getLog()
					.warn("No classes to generate. Please check the plugin configuration.");
			argsValid = false;
		}
		// Check classes are compiled before generation.
		if (this.outputDirectory == null || !this.outputDirectory.exists()) {
			this.getLog()
					.warn("Output directory does not exists. Please check the classes you want to generate DTOs for has been compiled.");
			argsValid = false;
		}
		return argsValid;
	}
}
