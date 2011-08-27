/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class on {@link ClassFinderImpl}
 * 
 * @author Wilfried Petit
 * 
 */
public class ClassFinderImplTest {

	private ClassFinderImpl classFinder;

	@Before
	public void setUp() throws Exception {
		this.classFinder = new ClassFinderImpl();
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#getClassesToGenerate(java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithValidClasses() {
		try {
			final List<String> includeClasses = new ArrayList<String>();
			includeClasses.add("fr.maven.dto.bean.Bean");
			includeClasses.add("fr.maven.dto.bean.Bean2");
			final List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(includeClasses);
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (final Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			Assert.assertTrue("Class Bean has been found.", classBeanFound);
			Assert.assertTrue("Class Bean2 has been found.", classBean2Found);
		} catch (final ClassNotFoundException e) {
			Assert.fail("testGetClassesToGenerateWithValidPattern failed, classes not found with valid pattern.");
		}

	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#getClassesToGenerate(java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithInvalidClasses() {
		final List<String> includeClasses = new ArrayList<String>();
		includeClasses.add("fr.maven.dto.invalid.InvalidBean");
		try {
			final List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(includeClasses);
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (final Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			Assert.fail("Classes found with invalid inputs.");
			Assert.assertFalse("Class Bean has not been found.", classBeanFound);
			Assert.assertFalse("Class Bean2 has not been found.",
					classBean2Found);
		} catch (final Exception e) {
			Assert.assertTrue("Exception is not a ClassNotFoundException",
					e instanceof ClassNotFoundException);
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#getClassesToGenerate(List, String[], String[])
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithValidPattern() {
		final List<String> includes = new ArrayList<String>();
		includes.add("**.Bean2");
		includes.add("**.Bean");
		final List<File> baseDirectories = new ArrayList<File>();
		baseDirectories
				.add(new File("target" + File.separator + "test-classes"));
		baseDirectories.add(new File("target" + File.separator + "test-classes"
				+ File.separator + "beans.jar"));
		try {
			final List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(this.getClass().getClassLoader(),
							baseDirectories, includes, new ArrayList<String>());
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (final Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			Assert.assertTrue("Class Bean has not been found.", classBeanFound);
			Assert.assertTrue("Class Bean2 has not been found.",
					classBean2Found);
		} catch (final ClassNotFoundException e) {
			Assert.fail("testGetClassesToGenerateWithValidPattern failed, classes not found with valid pattern.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#getClassesToGenerate(List, String[], String[])
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithValidPatternAndJarInClasspath() {
		URLClassLoader urlClassLoader = null;
		try {
			final List<URL> urls = new ArrayList<URL>();
			final URL beansJarURL = new File("target/test-classes/beans.jar")
					.toURI().toURL();
			final URL beanUrl = new File(
					"target/test-classes/fr/maven/dto/Bean.class").toURI()
					.toURL();
			urls.add(beansJarURL);
			urls.add(beanUrl);
			urlClassLoader = new URLClassLoader(urls.toArray(new URL[0]));
		} catch (final MalformedURLException e1) {
			// Should not happened because urls are created from valid files.
			e1.printStackTrace();
		}

		final List<String> includes = new ArrayList<String>();
		includes.add("**.Bean");
		includes.add("**.BeanInAJar");
		final List<File> baseDirectories = new ArrayList<File>();
		baseDirectories.add(new File("target/test-classes"));
		baseDirectories.add(new File("target/test-classes/beans.jar"));
		try {
			final List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(urlClassLoader, baseDirectories,
							includes, new ArrayList<String>());
			boolean classBeanFound = false;
			boolean classBeanInAJarFound = false;
			for (final Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.test.BeanInAJar".equals(clazz
						.getCanonicalName())) {
					classBeanInAJarFound = true;
				}
			}
			Assert.assertTrue("Class Bean has not been found.", classBeanFound);
			Assert.assertTrue("Class BeanInAJar has not been found.",
					classBeanInAJarFound);
		} catch (final ClassNotFoundException e) {
			Assert.fail("testGetClassesToGenerateWithValidPatternAndJarInClasspath failed, classes not found with valid pattern and jar in classpath.");
		}
	}
}
