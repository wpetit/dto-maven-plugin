/**
 * 
 */
package fr.maven.dto.generator.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.maven.dto.bean.Bean;
import fr.maven.dto.bean.Bean2;

/**
 * @author Wilfried Petit
 * 
 */
public class DTOGeneratorImplTest {

	private DTOGeneratorImpl dtoGeneratorImpl;
	private File generatedDirectory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.dtoGeneratorImpl = new DTOGeneratorImpl();
		this.generatedDirectory = new File("target\\generated");
		this.dtoGeneratorImpl.setGeneratedDirectory(this.generatedDirectory);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.deleteFiles(this.generatedDirectory);
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#generateDTOs(java.util.List)}
	 * .
	 */
	@Test
	public void testGenerateDTOs() {
		try {
			this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			List<Class<?>> classesToGenerate = new ArrayList<Class<?>>();
			classesToGenerate.add(Bean.class);
			classesToGenerate.add(Bean2.class);
			this.dtoGeneratorImpl.generateDTOs(classesToGenerate);
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\Bean2DTO.java");
			assertTrue("package has not been created", new File(
					this.generatedDirectory.getAbsolutePath()
							+ "\\fr\\maven\\dto\\bean\\dto").exists());
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validPackage = false;
			boolean validClass = false;
			boolean validField = false;
			boolean validFieldGetter = false;
			boolean validFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("package fr.maven.dto.bean.dto;".equals(line)) {
					validPackage = true;
				} else if ("public class Bean2DTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				} else if ("\tprivate BeanDTO bean;".equals(line)) {
					validField = true;
				} else if ("\tpublic BeanDTO getBean() {".equals(line)) {
					validFieldGetter = true;
				} else if ("\tpublic void setBean(BeanDTO bean) {".equals(line)) {
					validFieldSetter = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Package generated not valid", validPackage);
			assertTrue("Class generated not valid", validClass);
			assertTrue("Field generated not valid", validField);
			assertTrue("Field getter generated not valid", validFieldGetter);
			assertTrue("Field setter generated not valid", validFieldSetter);
		} catch (IOException e) {
			fail("The class has not been generated (could not write or read file).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#generateDTO(java.lang.Class)}
	 * .
	 */
	@Test
	public void testGenerateDTO() {
		try {
			this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			this.dtoGeneratorImpl.generateDTO(Bean2.class);
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\Bean2DTO.java");
			assertTrue("package has not been created", new File(
					this.generatedDirectory.getAbsolutePath()
							+ "\\fr\\maven\\dto\\bean\\dto").exists());
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validPackage = false;
			boolean validClass = false;
			boolean validField = false;
			boolean validFieldGetter = false;
			boolean validFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("package fr.maven.dto.bean.dto;".equals(line)) {
					validPackage = true;
				} else if ("public class Bean2DTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				} else if ("\tprivate BeanDTO bean;".equals(line)) {
					validField = true;
				} else if ("\tpublic BeanDTO getBean() {".equals(line)) {
					validFieldGetter = true;
				} else if ("\tpublic void setBean(BeanDTO bean) {".equals(line)) {
					validFieldSetter = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Package generated not valid", validPackage);
			assertTrue("Class generated not valid", validClass);
			assertTrue("Field generated not valid", validField);
			assertTrue("Field getter generated not valid", validFieldGetter);
			assertTrue("Field setter generated not valid", validFieldSetter);
		} catch (IOException e) {
			fail("The class has not been generated (could not write or read file).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOPackage(java.lang.Class)}
	 * .
	 */
	@Test
	public void testGetDTOPackage() {
		Class<?> clazz = Bean.class;
		assertEquals("getPackageDTO failed", "fr.maven.dto.bean.dto",
				this.dtoGeneratorImpl.getDTOPackage(clazz));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#isClassToGenerate(java.lang.Class)}
	 * .
	 */
	@Test
	public void testIsClassToGenerateWithValidClass() {
		this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
		assertTrue("isClassToGenerate does not find Bean class but it should.",
				this.dtoGeneratorImpl.isClassToGenerate(Bean.class));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#isClassToGenerate(java.lang.Class)}
	 * .
	 */
	@Test
	public void testIsClassToGenerateWitnValidClass() {
		assertFalse("isClassToGenerate found Bean class but it should not.",
				this.dtoGeneratorImpl.isClassToGenerate(Bean.class));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOFieldType(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testGetDTOFieldType() {
		try {
			Field field = Bean.class.getDeclaredField("attribut1");
			assertEquals(
					"getFieldType does not result String for attribut1 field",
					"String",
					this.dtoGeneratorImpl.getDTOFieldType(Bean.class, field));
		} catch (SecurityException e) {
			fail("Field attribut1 not accessible.");
		} catch (NoSuchFieldException e) {
			fail("Field attribut1 does not exists.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOFieldPackage(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testGetDTOFieldPackage() {
		try {
			Field field = Bean.class.getDeclaredField("attribut1");
			assertEquals(
					"getFieldPackage does not result \"\" for attribut1 field",
					"",
					this.dtoGeneratorImpl.getDTOFieldPackage(Bean.class, field));
		} catch (SecurityException e) {
			fail("Field attribut1 not accessible.");
		} catch (NoSuchFieldException e) {
			fail("Field attribut1 does not exists.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOClassFileWriter(java.lang.Class)}
	 * .
	 */
	@Test
	public void testGetDTOClassFileWriter() {
		try {
			FileWriter beanFileWriter = new FileWriter(new File(""));
			FileWriter bean2FileWriter = new FileWriter(new File(""));
			this.dtoGeneratorImpl.fileWriters.put(Bean.class, beanFileWriter);
			this.dtoGeneratorImpl.fileWriters.put(Bean2.class, bean2FileWriter);
			assertEquals(beanFileWriter,
					this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class));
		} catch (IOException e) {
			// Should not happened as the file is not created and we do not
			// write in.
		}

	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOPackage(java.lang.Class)}
	 * .
	 */
	@Test
	public void testMakeDTOPackage() {
		try {
			this.dtoGeneratorImpl.makeDTOPackage(Bean.class);
			this.dtoGeneratorImpl.makeDTOClass(Bean.class);
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\BeanDTO.java");
			assertTrue("package has not been created", new File(
					this.generatedDirectory.getAbsolutePath()
							+ "\\fr\\maven\\dto\\bean\\dto").exists());
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validPackage = false;
			while ((line = bf.readLine()) != null) {
				if ("package fr.maven.dto.bean.dto;".equals(line)) {
					validPackage = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Package generated not valid", validPackage);
		} catch (IOException e) {
			fail("The package has not been generated (could not write or read file).");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOClass(java.lang.Class)}
	 * .
	 */
	@Test
	public void testMakeDTOClass() {
		try {
			this.dtoGeneratorImpl.makeDTOPackage(Bean.class);
			this.dtoGeneratorImpl.makeDTOClass(Bean.class);
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\BeanDTO.java");
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validClass = false;
			while ((line = bf.readLine()) != null) {
				if ("public class BeanDTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Class generated not valid", validClass);
		} catch (IOException e) {
			fail("The class has not been generated (could not write or read file).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOField(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOField() {
		try {
			this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			this.dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			this.dtoGeneratorImpl.makeDTOField(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\Bean2DTO.java");
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validField = false;
			while ((line = bf.readLine()) != null) {
				if ("\tprivate BeanDTO bean;".equals(line)) {
					validField = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Field generated not valid", validField);
		} catch (IOException e) {
			fail("The field has not been generated (could not write or read file).");
		} catch (SecurityException e) {
			fail("The field has not been generated (the source field is not accessible).");
		} catch (NoSuchFieldException e) {
			fail("The field has not been generated (the source field is not accessible).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOFieldGetter(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOFieldGetter() {
		try {
			this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			this.dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			this.dtoGeneratorImpl.makeDTOFieldGetter(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\Bean2DTO.java");
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldGetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic BeanDTO getBean() {".equals(line)) {
					validFieldGetter = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Field getter generated not valid", validFieldGetter);
		} catch (IOException e) {
			fail("The field getter has not been generated (could not write or read file).");
		} catch (SecurityException e) {
			fail("The field getter has not been generated (the source field is not accessible).");
		} catch (NoSuchFieldException e) {
			fail("The field getter has not been generated (the source field is not accessible).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOFieldSetter(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOFieldSetter() {
		try {
			this.dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			this.dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			this.dtoGeneratorImpl.makeDTOFieldSetter(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			File file = new File(this.generatedDirectory.getAbsolutePath()
					+ "\\fr\\maven\\dto\\bean\\dto\\Bean2DTO.java");
			this.dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic void setBean(BeanDTO bean) {".equals(line)) {
					validFieldSetter = true;
				}
			}
			bf.close();
			this.deleteFiles(file);
			assertTrue("Field setter generated not valid", validFieldSetter);
		} catch (IOException e) {
			fail("The field getter has not been generated (could not write or read file).");
		} catch (SecurityException e) {
			fail("The field getter has not been generated (the source field is not accessible).");
		} catch (NoSuchFieldException e) {
			fail("The field getter has not been generated (the source field is not accessible).");
		}
	}

	private void deleteFiles(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (File fileChild : file.listFiles()) {
					if (fileChild != null && fileChild.exists()) {
						this.deleteFiles(fileChild);
					}
				}
			}
			file.delete();
		}
	}
}
