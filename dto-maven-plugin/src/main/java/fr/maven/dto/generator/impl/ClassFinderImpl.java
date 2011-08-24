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
	 * 
	 * {@inheritDoc}
	 * 
	 * @see fr.maven.dto.generator.ClassFinder#getClassesToGenerate(java.util.List)
	 */
	@Override
	public List<Class<?>> getClassesToGenerate(final List<String> includeClasses)
			throws ClassNotFoundException {
		List<Class<?>> classesToGenerate = new ArrayList<Class<?>>();
		for (String classString : includeClasses) {
			Class<?> clazz = this.getClass().getClassLoader()
					.loadClass(classString);
			classesToGenerate.add(clazz);
		}
		return classesToGenerate;
	}

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

		List<Class<?>> classes = new ArrayList<Class<?>>();
		String[] includesPath = this
				.convertClassPatternsToPathPatterns(includePatterns);
		String[] excludesPath = this
				.convertClassPatternsToPathPatterns(excludePatterns);

		for (File directoryOrArchive : baseDirectories) {
			if (directoryOrArchive != null) {
				if (directoryOrArchive.isDirectory()) {
					DirectoryScanner ds = new DirectoryScanner();
					ds.setBasedir(directoryOrArchive);
					ds.setIncludes(includesPath);
					ds.setExcludes(excludesPath);
					ds.setCaseSensitive(true);
					ds.scan();
					for (String fileFound : ds.getIncludedFiles()) {
						String classString = this
								.convertPathToCanonicalClassName(fileFound);
						Class<?> clazz = classLoader.loadClass(classString);
						classes.add(clazz);
					}
				} else {
					ZipScanner ds = new ZipScanner();
					ds.setSrc(directoryOrArchive);
					ds.setIncludes(includesPath);
					ds.setExcludes(excludesPath);
					ds.setCaseSensitive(true);
					for (String fileFound : ds.getIncludedFiles()) {
						String classString = this
								.convertPathToCanonicalClassName(fileFound);
						Class<?> clazz = classLoader.loadClass(classString);
						classes.add(clazz);
					}
				}
			}
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
		String[] pathPatterns = new String[0];
		if (classPatterns != null) {
			pathPatterns = new String[classPatterns.size()];
			for (int i = 0; i < classPatterns.size(); i++) {
				pathPatterns[i] = classPatterns.get(i).replace(".",
						File.separator)
						+ ".class";
			}
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
		String classString = path.replace(File.separator, ".")
				.replace("/", ".").replace("\\", "").replace(".class", "");
		return classString;
	}

}
