package fr.maven.dto;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import fr.maven.dto.generator.ClassLoaderProvider;
import fr.maven.dto.generator.impl.ClassLoaderProviderImpl;

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
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Location of the DTO classes generation directory.
	 * 
	 * @parameter expression="${generatedDirectory}"
	 *            default-value="${project.build.directory}/generated"
	 * @required
	 */
	private File generatedDirectory;

	/**
	 * List of pattern classes we want to generate DTO for match.
	 * 
	 * @parameter expression="${includes}"
	 */
	private List<String> includes;

	/**
	 * List of pattern classes we do not want to generate DTO for match.
	 * 
	 * @parameter expression="${excludes}"
	 */
	private List<String> excludes;

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
			this.getLog().debug("dto-maven-plugin launch the generation.");
			DTOLauncher dtoLauncher = new DTOLauncher();
			dtoLauncher.execute(this.getClassLoader(this.outputDirectory),
					this.getBaseDirectories(), this.includes, this.excludes,
					this.generatedDirectory);
			this.getLog().debug("dto-maven-plugin finished the generation.");
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
	 * @throws MalformedURLException
	 *             if the creation of url for files found failed
	 */
	protected ClassLoader getClassLoader(File directory)
			throws MalformedURLException {
		this.getLog().debug("Begin classloader creation");

		List<URL> urlList = new ArrayList<URL>();

		urlList.add(directory.toURI().toURL());
		for (Artifact artifact : this.pluginArtifacts) {
			urlList.add(artifact.getFile().toURI().toURL());
		}
		for (Artifact artifact : this.projectDependencies) {
			if (artifact.getFile() != null) {
				urlList.add(artifact.getFile().toURI().toURL());
			}
		}

		ClassLoaderProvider classLoaderProvider = new ClassLoaderProviderImpl(
				urlList.toArray(new URL[0]));
		AccessController.doPrivileged(classLoaderProvider);
		ClassLoader urlClassLoader = classLoaderProvider.getClassLoader();

		this.getLog().debug("End classloader creation");
		return urlClassLoader;
	}

	/**
	 * Return all base directories or archive where classes can been found.
	 * 
	 * @param classesDirectory
	 *            the classes directory.
	 * @return the list of directories and archive found.
	 */
	protected List<File> getBaseDirectories() {
		this.getLog().debug("Begin classes containers listing");
		List<File> directoriesOrArchive = new ArrayList<File>();
		directoriesOrArchive.add(this.outputDirectory);
		for (Artifact artifact : this.pluginArtifacts) {
			directoriesOrArchive.add(artifact.getFile());
		}
		for (Artifact artifact : this.projectDependencies) {
			directoriesOrArchive.add(artifact.getFile());
		}
		this.getLog().debug("End classes containers listing");
		return directoriesOrArchive;
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
		if (this.includes == null || this.includes.isEmpty()) {
			this.getLog()
					.warn("No classes to generate. Please check the plugin configuration.");
			argsValid = false;
		}
		// Check classes are compiled before generation.
		if (this.outputDirectory == null || !this.outputDirectory.exists()) {
			this.getLog()
					.warn("Output directory does not exists. Please check the classes you want to generate DTOs for have been compiled.");
			argsValid = false;
		}
		return argsValid;
	}
}
