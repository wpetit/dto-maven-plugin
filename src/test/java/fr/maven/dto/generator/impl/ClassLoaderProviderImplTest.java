/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.io.File;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.maven.dto.bean.AnotherBean;

/**
 * @author Wilfried Petit
 * 
 */
public class ClassLoaderProviderImplTest {

	private List<URL> classURLs;

	private URL beansJarURL;

	private ClassLoaderProviderImpl classLoaderProvider;

	/**
	 * Set up tests.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		final File anotherBeanFile = new File(
				"target/test-classes/fr/maven/dto/bean/AnotherBean.class");
		this.beansJarURL = new File("target/test-classes/beans.jar").toURI()
				.toURL();
		this.classURLs = new ArrayList<URL>();
		this.classURLs.add(anotherBeanFile.toURI().toURL());
		this.classURLs.add(this.beansJarURL);
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassLoaderProviderImpl#getClassLoader()}
	 * .
	 */
	@Test
	public void testGetClassLoader() {
		this.classLoaderProvider = new ClassLoaderProviderImpl(
				this.classURLs.toArray(new URL[0]));
		AccessController.doPrivileged(this.classLoaderProvider);
		this.classLoaderProvider.getClassLoader();
		final ClassLoader urlClassLoader = this.classLoaderProvider
				.getClassLoader();
		try {
			urlClassLoader.loadClass(AnotherBean.class.getCanonicalName());
		} catch (final ClassNotFoundException e) {

		}
		try {
			urlClassLoader.loadClass("fr.maven.dto.test.BeanInAJar");
		} catch (final ClassNotFoundException e) {
			Assert.fail("BeanInAJar has not been found but it should.");
		}
	}

}
