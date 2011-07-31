/**
 * 
 */
package fr.maven.dto.generator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
	 * {@link fr.maven.dto.generator.ClassFinderImpl#getClassesToGenerate(java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithValidPattern() {
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
	 * {@link fr.maven.dto.generator.ClassFinderImpl#getClassesToGenerate(java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetClassesToGenerateWithInvalidPattern() {
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
}
