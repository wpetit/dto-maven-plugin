/**
 * 
 */
package fr.maven.dto.generator;

import java.io.File;
import java.util.List;

/**
 * This interface describes a class finder.
 * 
 * @author Wilfried Petit
 * 
 */
public interface ClassFinder {

	/**
	 * Return the classes that match the canonical name classes list given.
	 * 
	 * @param includeClasses
	 *            the list of canonical name classes list we want to get
	 *            {@link Class}.
	 * @return the classes got.
	 * @throws ClassNotFoundException
	 *             if the classes have not been found.
	 */
	List<Class<?>> getClassesToGenerate(List<String> includeClasses)
			throws ClassNotFoundException;

	/**
	 * Return the classes in base directories that match the includePatterns and
	 * do not math the excludePatterns.
	 * 
	 * @param classLoader
	 *            the ClassLoader that contains classes to generate.
	 * @param baseDirectories
	 *            the list of directories where classes are.
	 * @param includePatterns
	 *            patterns that canonical name classes must match.
	 * @param excludePatterns
	 *            patterns that canonical name classes must not match.
	 * @return the classes got.
	 * @throws ClassNotFoundException
	 *             if the classes found in directories have not been found. This
	 *             should not happened.
	 */
	List<Class<?>> getClassesToGenerate(ClassLoader classLoader,
			List<File> baseDirectories, List<String> includePatterns,
			List<String> excludePatterns) throws ClassNotFoundException;
}
