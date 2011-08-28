package fr.maven.dto;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import fr.maven.dto.generator.ClassLoaderProvider;
import fr.maven.dto.generator.impl.ClassLoaderProviderImpl;

/**
 * Mojo to generate DTO classes.
 * 
 * @goal dto
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * @threadSafe
 */
public class DTOMojo extends AbstractMojo {

	/**
	 * The project currently being built.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

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
	 *            default-value="${project.build.directory}/generated-sources"
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
			final DTOLauncher dtoLauncher = new DTOLauncher();
			dtoLauncher.execute(this.getClassLoader(this.outputDirectory),
					this.getBaseDirectories(), this.includes, this.excludes,
					this.generatedDirectory);
			this.getLog().debug("dto-maven-plugin finished the generation.");
			this.project.addCompileSourceRoot(this.generatedDirectory
					.getAbsolutePath());
		} catch (final Exception e) {
			this.getLog().error("The generation has failed.", e);
			throw new MojoExecutionException("The generation has failed.", e);
		}
	}

	/**
	 * Create the classloader that contains classes to generate.
	 * 
	 * @param directory
	 *            the directory that contains compiled classes.
	 * @return the classloader result.
	 * @throws MalformedURLException
	 *             if the creation of url for files found failed
	 * @throws DependencyResolutionRequiredException
	 */
	protected ClassLoader getClassLoader(final File directory)
			throws MalformedURLException, DependencyResolutionRequiredException {
		this.getLog().debug("Begin classloader creation");

		final List<URL> urlList = new ArrayList<URL>();

		for (final Object filesPath : this.project
				.getCompileClasspathElements()) {
			urlList.add(new File((String) filesPath).toURI().toURL());
		}

		final ClassLoaderProvider classLoaderProvider = new ClassLoaderProviderImpl(
				urlList.toArray(new URL[0]));
		AccessController.doPrivileged(classLoaderProvider);
		final ClassLoader urlClassLoader = classLoaderProvider.getClassLoader();

		this.getLog().debug("End classloader creation");
		return urlClassLoader;
	}

	/**
	 * Return all base directories or archive where classes can been found.
	 * 
	 * @param classesDirectory
	 *            the classes directory.
	 * @return the list of directories and archive found.
	 * @throws DependencyResolutionRequiredException
	 */
	protected List<File> getBaseDirectories()
			throws DependencyResolutionRequiredException {
		this.getLog().debug("Begin classes containers listing");
		final List<File> directoriesOrArchive = new ArrayList<File>();
		for (final Object filesPath : this.project
				.getCompileClasspathElements()) {
			directoriesOrArchive.add(new File((String) filesPath));
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
		return argsValid;
	}
}
