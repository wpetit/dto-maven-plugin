/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.net.URL;
import java.net.URLClassLoader;

import fr.maven.dto.generator.ClassLoaderProvider;

/**
 * ClassLoaderProvider provides ClassLoader from URLs in a security way.
 * 
 * @author Wilfried Petit
 * 
 */
public class ClassLoaderProviderImpl implements ClassLoaderProvider {

	/**
	 * The URL List containing classes and archives to load in the ClassLoader.
	 */
	private final URL[] urls;

	/**
	 * The ClassLoader provided to callers.
	 */
	private ClassLoader classLoader;

	/**
	 * Constructor.
	 * 
	 * @param urls
	 *            The URL List containing classes and archives to load in the
	 *            ClassLoader.
	 */
	public ClassLoaderProviderImpl(final URL[] urls) {
		this.urls = urls.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see fr.maven.dto.generator.ClassLoaderProvider#run()
	 */
	@Override
	public ClassLoader run() {
		this.classLoader = new URLClassLoader(this.urls,
				ClassLoader.class.getClassLoader());
		return this.classLoader;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see fr.maven.dto.generator.ClassLoaderProvider#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

}
