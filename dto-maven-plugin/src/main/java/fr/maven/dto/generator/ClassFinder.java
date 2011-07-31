/**
 * 
 */
package fr.maven.dto.generator;

import java.util.List;

/**
 * @author Wilfried Petit
 * 
 */
public interface ClassFinder {

	// /**
	// * @return
	// * @throws ClassNotFoundException
	// */
	// List<Class<?>> getClassesToGenerate(File projectBuildDirectory,
	// String[] includePatterns, String[] excludePatterns)
	// throws ClassNotFoundException;

	List<Class<?>> getClassesToGenerate(List<String> includeClasses)
			throws ClassNotFoundException;
}
