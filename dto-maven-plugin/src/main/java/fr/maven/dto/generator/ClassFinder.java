/**
 * 
 */
package fr.maven.dto.generator;

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
}
