/**
 * 
 */
package fr.maven.dto.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wilfried Petit
 * 
 */
public class ClassFinderImpl implements ClassFinder {

	// /**
	// *
	// * {@inheritDoc}
	// *
	// * @see
	// fr.maven.dto.generator.ClassFinder#getClassesToGenerate(java.lang.String[])
	// */
	// @Override
	// public List<Class<?>> getClassesToGenerate(File buildProjectDirectory,
	// String[] includePatterns, String[] excludePatterns)
	// throws ClassNotFoundException {
	//
	// List<Class<?>> classes = new ArrayList<Class<?>>();
	// String[] includesPath = null;
	// if (includePatterns != null) {
	// includesPath = new String[includePatterns.length];
	// for (int i = 0; i < includePatterns.length; i++) {
	// includesPath[i] = includePatterns[i].replace(".",
	// File.separator);
	// }
	// }
	// String[] excludesPath = null;
	// if (excludePatterns != null) {
	// excludesPath = new String[excludePatterns.length];
	// for (int i = 0; i < excludePatterns.length; i++) {
	// excludesPath[i] = excludePatterns[i].replace(".",
	// File.separator);
	// }
	// }
	// DirectoryScanner ds = new DirectoryScanner();
	// ds.setBasedir(buildProjectDirectory);
	// ds.setIncludes(includesPath);
	// ds.setExcludes(excludesPath);
	// ds.setCaseSensitive(true);
	// ds.scan();
	// for (String file : ds.getIncludedFiles()) {
	// String classString = file.replace(File.separator, ".").replace(
	// ".class", "");
	// Class<?> clazz = this.getClass().getClassLoader()
	// .loadClass(classString);
	// classes.add(clazz);
	// }
	// return classes;
	// }

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassNotFoundException
	 * @see fr.maven.dto.generator.ClassFinder#getClassesToGenerate(java.lang.String[])
	 */
	@Override
	public List<Class<?>> getClassesToGenerate(List<String> includeClasses)
			throws ClassNotFoundException {
		List<Class<?>> classesToGenerate = new ArrayList<Class<?>>();
		for (String classString : includeClasses) {
			Class<?> clazz = this.getClass().getClassLoader()
					.loadClass(classString);
			classesToGenerate.add(clazz);
		}
		return classesToGenerate;
	}

}
