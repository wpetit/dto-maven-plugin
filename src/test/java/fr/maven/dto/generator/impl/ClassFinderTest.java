/**
 * 
 */
package fr.maven.dto.generator.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.maven.dto.generator.ClassFinder;
import fr.maven.dto.generator.impl.ClassFinderImpl;

/**
 * @author Wilfried Petit
 * 
 */
public class ClassFinderTest {

	private ClassFinder classFinder;

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
			List<String> includeClasses = new ArrayList<String>();
			includeClasses.add("fr.maven.dto.bean.Bean");
			includeClasses.add("fr.maven.dto.bean.Bean2");
			List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(includeClasses);
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			assertTrue("Class Bean has been found.", classBeanFound);
			assertTrue("Class Bean2 has been found.", classBean2Found);
		} catch (ClassNotFoundException e) {
			fail("testGetClassesToGenerateWithValidPattern failed, classes not found with valid pattern.");
		}

	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.ClassFinderImpl#getClassesToGenerate(java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithInvalidClasses() {
		List<String> includeClasses = new ArrayList<String>();
		includeClasses.add("fr.maven.dto.invalid.InvalidBean");
		try {
			List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(includeClasses);
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			fail("Classes found with invalid inputs.");
			assertFalse("Class Bean has not been found.", classBeanFound);
			assertFalse("Class Bean2 has not been found.", classBean2Found);
		} catch (Exception e) {
			assertTrue("Exception is not a ClassNotFoundException",
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
		List<String> includes = new ArrayList<String>();
		includes.add("**.Bean2");
		includes.add("**.Bean");
		List<File> baseDirectories = new ArrayList<File>();
		baseDirectories
				.add(new File("target" + File.separator + "test-classes"));
		baseDirectories
				.add(new File(
						"C:/Users/minimoi/.m2/repository/org/apache/maven/maven-plugin-api/2.0/maven-plugin-api-2.0.jar"));
		try {
			List<Class<?>> classesFound = this.classFinder
					.getClassesToGenerate(this.getClass().getClassLoader(),
							baseDirectories, includes, new ArrayList<String>());
			boolean classBeanFound = false;
			boolean classBean2Found = false;
			for (Class<?> clazz : classesFound) {
				if ("fr.maven.dto.bean.Bean".equals(clazz.getCanonicalName())) {
					classBeanFound = true;
				}
				if ("fr.maven.dto.bean.Bean2".equals(clazz.getCanonicalName())) {
					classBean2Found = true;
				}
			}
			assertTrue("Class Bean has not been found.", classBeanFound);
			assertTrue("Class Bean2 has not been found.", classBean2Found);
		} catch (ClassNotFoundException e) {
			fail("testGetClassesToGenerateWithValidPattern failed, classes not found with valid pattern.");
		}
	}
}
