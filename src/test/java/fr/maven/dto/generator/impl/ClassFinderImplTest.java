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

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#convertClassPatternsToPathPatterns(List)
	 * .
	 */
	@Test
	public void testConvertClassPatternsToPathPatterns() {
		final List<String> classPatterns = new ArrayList<String>();
		final String firstClassPattern = "fr.maven.**.*";
		classPatterns.add(firstClassPattern);
		final String secondClassPattern = "**.*";
		classPatterns.add(secondClassPattern);
		final String[] pathPatterns = this.classFinder
				.convertClassPatternsToPathPatterns(classPatterns);

		boolean firstPatternIsValid = false;
		boolean secondPatternIsValid = false;

		final String firstPathPattern = "fr" + File.separator + "maven"
				+ File.separator + "**" + File.separator + "*.class";
		final String secondPathPattern = "**" + File.separator + "*.class";
		for (final String pathPattern : pathPatterns) {
			if (firstPathPattern.equals(pathPattern)) {
				firstPatternIsValid = true;
			} else if (secondPathPattern.equals(pathPattern)) {
				secondPatternIsValid = true;
			}
		}
		Assert.assertTrue("fr.maven.**.* conversion is wrong",
				firstPatternIsValid);
		Assert.assertTrue("**.* conversion is wrong", secondPatternIsValid);
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#convertPathToCanonicalClassName(String)
	 * .
	 */
	@Test
	public void testConvertPathToCanonicalClass() {
		final String path = "fr/maven/dto/Bean";
		Assert.assertEquals(
				"fr/maven/dto/Bean conversion to class canonical name is wrong.",
				"fr.maven.dto.Bean",
				this.classFinder.convertPathToCanonicalClassName(path));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#scanArchive(ClassLoader, File, String[], String[])
	 * .
	 */
	@Test
	public void testScanArchive() {
		try {
			final File archive = new File("target/test-classes/beans.jar");
			final List<URL> urls = new ArrayList<URL>();
			final URL beansJarURL = archive.toURI().toURL();
			urls.add(beansJarURL);
			final URLClassLoader urlClassLoader = new URLClassLoader(
					urls.toArray(new URL[0]));
			final List<String> includes = new ArrayList<String>();
			includes.add("**/BeanInAJar.class");
			final List<Class<?>> classesFound = this.classFinder.scanArchive(
					urlClassLoader, archive, includes.toArray(new String[0]),
					new String[0]);
			boolean classBeanInAJarFound = false;
			for (final Class<?> clazz : classesFound) {
				if ("fr.maven.dto.test.BeanInAJar".equals(clazz
						.getCanonicalName())) {
					classBeanInAJarFound = true;
				}
			}
			Assert.assertTrue("Class BeanInAJar has not been found.",
					classBeanInAJarFound);
		} catch (final ClassNotFoundException e) {
			Assert.fail("testScanArchive failed, classes not found with valid pattern and jar in classpath.");
		} catch (final MalformedURLException e1) {
			// Should not happened because urls are created from valid files.
			e1.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#scanDirectory(ClassLoader, File, String[], String[])
	 * .
	 */
	@Test
	public void testScanDirectory() {
		final List<String> includes = new ArrayList<String>();
		includes.add("**/Bean2.class");
		includes.add("**/Bean.class");
		try {
			final List<Class<?>> classesFound = this.classFinder.scanDirectory(
					this.getClass().getClassLoader(),
					new File("target" + File.separator + "test-classes"
							+ File.separator), includes.toArray(new String[0]),
					new String[0]);
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
}
