/**
 * 
 */
package fr.maven.dto.generator;

import java.util.ArrayList;
import java.util.List;

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

}
