/**
 * 
 */
package fr.maven.dto.generator;

import java.security.PrivilegedAction;

/**
 * Interface that describes a ClassLoaderProvider in a security way.
 * 
 * @author Wilfried Petit
 * 
 */
public interface ClassLoaderProvider extends PrivilegedAction<ClassLoader> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.security.PrivilegedAction#run()
	 */
	@Override
	ClassLoader run();

	/**
	 * Return the ClassLoader built.
	 * 
	 * @return the ClassLoader.
	 */
	ClassLoader getClassLoader();

}