/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.ZipScanner;

import fr.maven.dto.generator.ClassFinder;

/**
 * {@link ClassFinder} implementation.
 * 
 * It searches classes in the classpath.
 * 
 * @author Wilfried Petit
 * 
 */
public class ClassFinderImpl implements ClassFinder {

	/**
	 * {@inheritDoc}
	 * 
	 * @see fr.maven.dto.generator.ClassFinder#getClassesToGenerate(java.lang.ClassLoader,
	 *      java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public List<Class<?>> getClassesToGenerate(final ClassLoader classLoader,
			final List<File> baseDirectories,
			final List<String> includePatterns,
			final List<String> excludePatterns) throws ClassNotFoundException {

		final List<Class<?>> classes = new ArrayList<Class<?>>();
		final String[] includesPath = this
				.convertClassPatternsToPathPatterns(includePatterns);
		final String[] excludesPath = this
				.convertClassPatternsToPathPatterns(excludePatterns);

		for (final File directoryOrArchive : baseDirectories) {
			if (directoryOrArchive != null) {
				classes.addAll(this.scanDirectoryOrArchive(classLoader,
						directoryOrArchive, includesPath, excludesPath));
			}
		}
		return classes;
	}

	/**
	 * Scan the directory or the archive given to find classes that match
	 * includes path, and do not match excludes path. Classes found are load to
	 * the class loader given and returned.
	 * 
	 * @param classLoader
	 *            the class loader to load classes found.
	 * @param directoryOrArchive
	 *            the directory or the archive to scan.
	 * @param includesPath
	 *            the list of includes path patterns that classes must match.
	 * @param excludesPath
	 *            the list of excludes path patterns that classes must not
	 *            match.
	 * @return the list of classes found.
	 * @throws ClassNotFoundException
	 *             if the class found in the package has not been found after
	 *             loading in the class loader.
	 */
	protected List<Class<?>> scanDirectoryOrArchive(
			final ClassLoader classLoader, final File directoryOrArchive,
			final String[] includesPath, final String[] excludesPath)
			throws ClassNotFoundException {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		if (directoryOrArchive.isDirectory()) {
			classes.addAll(this.scanDirectory(classLoader, directoryOrArchive,
					includesPath, excludesPath));
		} else {
			classes.addAll(this.scanArchive(classLoader, directoryOrArchive,
					includesPath, excludesPath));
		}
		return classes;
	}

	/**
	 * Scan the directory given to find classes that match includes path, and do
	 * not match excludes path. Classes found are load to the class loader given
	 * and returned.
	 * 
	 * @param classLoader
	 *            the class loader to load classes found.
	 * @param directory
	 *            the directory to scan.
	 * @param includesPath
	 *            the list of includes path patterns that classes must match.
	 * @param excludesPath
	 *            the list of excludes path patterns that classes must not
	 *            match.
	 * @return the list of classes found.
	 * @throws ClassNotFoundException
	 *             if the class found in the package has not been found after
	 *             loading in the class loader.
	 */
	protected List<Class<?>> scanDirectory(final ClassLoader classLoader,
			final File directory, final String[] includesPath,
			final String[] excludesPath) throws ClassNotFoundException {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		final DirectoryScanner ds = new DirectoryScanner();
		ds.setBasedir(directory);
		ds.setIncludes(includesPath);
		ds.setExcludes(excludesPath);
		ds.setCaseSensitive(true);
		ds.scan();
		for (final String fileFound : ds.getIncludedFiles()) {
			final String classString = this
					.convertPathToCanonicalClassName(fileFound);
			final Class<?> clazz = classLoader.loadClass(classString);
			classes.add(clazz);
		}
		return classes;
	}

	/**
	 * Scan the archive given to find classes that match includes path, and do
	 * not match excludes path. Classes found are load to the class loader given
	 * and returned.
	 * 
	 * @param classLoader
	 *            the class loader to load classes found.
	 * @param archive
	 *            the archive to scan.
	 * @param includesPath
	 *            the list of includes path patterns that classes must match.
	 * @param excludesPath
	 *            the list of excludes path patterns that classes must not
	 *            match.
	 * @return the list of classes found.
	 * @throws ClassNotFoundException
	 *             if the class found in the package has not been found after
	 *             loading in the class loader.
	 */
	protected List<Class<?>> scanArchive(final ClassLoader classLoader,
			final File archive, final String[] includesPath,
			final String[] excludesPath) throws ClassNotFoundException {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		final ZipScanner ds = new ZipScanner();
		ds.setSrc(archive);
		ds.setIncludes(includesPath);
		ds.setExcludes(excludesPath);
		ds.setCaseSensitive(true);
		for (final String fileFound : ds.getIncludedFiles()) {
			final String classString = this
					.convertPathToCanonicalClassName(fileFound);
			final Class<?> clazz = classLoader.loadClass(classString);
			classes.add(clazz);
		}
		return classes;
	}

	/**
	 * Create path patterns from class patterns. E.g : For the class pattern :
	 * **.Bean, the path pattern returned is **\Bean.class.
	 * 
	 * @param classPatterns
	 *            the list of class patterns to convert.
	 * @return the list of path patterns got.
	 */
	protected String[] convertClassPatternsToPathPatterns(
			final List<String> classPatterns) {
		final String[] pathPatterns = new String[classPatterns.size()];
		for (int i = 0; i < classPatterns.size(); i++) {
			pathPatterns[i] = classPatterns.get(i).replace(".", File.separator)
					+ ".class";
		}
		return pathPatterns;
	}

	/**
	 * Convert file path to canonical class name. E.g. : For the file
	 * fr\maven\dto\Bean.class, the canonical class name returned is
	 * fr.maven.dto.Bean.
	 * 
	 * @param path
	 *            the path to convert.
	 * @return the canonical class name got.
	 */
	protected String convertPathToCanonicalClassName(final String path) {
		final String classString = path.replace(File.separator, ".")
				.replace("/", ".").replace("\\", "").replace(".class", "");
		return classString;
	}

}
