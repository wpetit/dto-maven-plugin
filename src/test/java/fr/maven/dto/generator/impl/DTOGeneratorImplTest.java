/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.maven.dto.bean.AnotherBean;
import fr.maven.dto.bean.Bean;
import fr.maven.dto.bean.Bean2;

/**
 * Tests on {@link DTOGeneratorImpl}.
 * 
 * @author Wilfried Petit
 * 
 */
public class DTOGeneratorImplTest {

	private DTOGeneratorImpl dtoGeneratorImpl;
	private File generatedDirectory;

	/**
	 * Set up configuration for generation.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dtoGeneratorImpl = new DTOGeneratorImpl();
		generatedDirectory = new File("target" + File.separator + "generated");
		dtoGeneratorImpl.setGeneratedDirectory(generatedDirectory);
	}

	/**
	 * Tear down. Delete generated files.
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		deleteFiles(generatedDirectory);
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#generateDTOs(java.util.List)}
	 * .
	 */
	@Test
	public void testGenerateDTOs() {
		try {
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			final List<Class<?>> classesToGenerate = new ArrayList<Class<?>>();
			classesToGenerate.add(Bean.class);
			classesToGenerate.add(Bean2.class);
			dtoGeneratorImpl.generateDTOs(classesToGenerate);
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			Assert.assertTrue("package has not been created", new File(
					generatedDirectory.getAbsolutePath() + File.separator
							+ "fr" + File.separator + "maven" + File.separator
							+ "dto" + File.separator + "bean" + File.separator
							+ "dto").exists());
			final BufferedReader bf = new BufferedReader(new FileReader(file));
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
			deleteFiles(file);
			Assert.assertTrue("Package generated not valid", validPackage);
			Assert.assertTrue("Class generated not valid", validClass);
			Assert.assertTrue("Field generated not valid", validField);
			Assert.assertTrue("Field getter generated not valid",
					validFieldGetter);
			Assert.assertTrue("Field setter generated not valid",
					validFieldSetter);
		} catch (final IOException e) {
			Assert.fail("The class has not been generated (could not write or read file).");
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
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			dtoGeneratorImpl.generateDTO(Bean2.class);
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			Assert.assertTrue("package has not been created", new File(
					generatedDirectory.getAbsolutePath() + File.separator
							+ "fr" + File.separator + "maven" + File.separator
							+ "dto" + File.separator + "bean" + File.separator
							+ "dto").exists());
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validPackage = false;
			boolean validClass = false;
			boolean validField = false;
			boolean validGenericField = false;
			boolean validFieldGetter = false;
			boolean validGenericFieldGetter = false;
			boolean validFieldSetter = false;
			boolean validGenericFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("package fr.maven.dto.bean.dto;".equals(line)) {
					validPackage = true;
				} else if ("public class Bean2DTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				} else if ("\tprivate BeanDTO bean;".equals(line)) {
					validField = true;
				} else if ("\tprivate java.util.List<BeanDTO> beans;"
						.equals(line)) {
					validGenericField = true;
				} else if ("\tpublic BeanDTO getBean() {".equals(line)) {
					validFieldGetter = true;
				} else if ("\tpublic java.util.List<BeanDTO> getBeans() {"
						.equals(line)) {
					validGenericFieldGetter = true;
				} else if ("\tpublic void setBean(BeanDTO bean) {".equals(line)) {
					validFieldSetter = true;
				} else if ("\tpublic void setBeans(java.util.List<BeanDTO> beans) {"
						.equals(line)) {
					validGenericFieldSetter = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Package generated not valid", validPackage);
			Assert.assertTrue("Class generated not valid", validClass);
			Assert.assertTrue("Field generated not valid", validField);
			Assert.assertTrue("Generic Field generated not valid",
					validGenericField);
			Assert.assertTrue("Field getter generated not valid",
					validFieldGetter);
			Assert.assertTrue("Generic Field getter generated not valid",
					validGenericFieldGetter);
			Assert.assertTrue("Field setter generated not valid",
					validFieldSetter);
			Assert.assertTrue("Generic Field setter generated not valid",
					validGenericFieldSetter);
		} catch (final IOException e) {
			Assert.fail("The class has not been generated (could not write or read file).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOPackage(java.lang.Class)}
	 * .
	 */
	@Test
	public void testGetDTOPackage() {
		final Class<?> clazz = Bean.class;
		Assert.assertEquals("getPackageDTO failed", "fr.maven.dto.bean.dto",
				dtoGeneratorImpl.getDTOPackage(clazz));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#isClassToGenerate(java.lang.Class)}
	 * .
	 */
	@Test
	public void testIsClassToGenerateWithValidClass() {
		dtoGeneratorImpl.classesToGenerate.add(Bean.class);
		Assert.assertTrue(
				"isClassToGenerate does not find Bean class but it should.",
				dtoGeneratorImpl.isClassToGenerate(Bean.class));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#isClassToGenerate(java.lang.Class)}
	 * .
	 */
	@Test
	public void testIsClassToGenerateWitnValidClass() {
		Assert.assertFalse(
				"isClassToGenerate found Bean class but it should not.",
				dtoGeneratorImpl.isClassToGenerate(Bean.class));
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOFieldType(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testGetDTOFieldType() {
		try {
			final Field field = Bean.class.getDeclaredField("attribut1");
			Assert.assertEquals(
					"getFieldType does not result String for attribut1 field",
					"String",
					dtoGeneratorImpl.getDTOFieldType(Bean.class, field));
		} catch (final SecurityException e) {
			Assert.fail("Field attribut1 not accessible.");
		} catch (final NoSuchFieldException e) {
			Assert.fail("Field attribut1 does not exists.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOType(java.lang.Class, java.lang.reflect.Tyoe)}
	 * .
	 */
	@Test
	public void testGetDTOTypeForClassInJavaLang() {
		try {
			final Field field = Bean.class.getDeclaredField("attribut1");
			Assert.assertEquals(
					"getFieldType does not result String for attribut1 field",
					"String",
					dtoGeneratorImpl.getDTOFieldType(Bean.class, field));
		} catch (final SecurityException e) {
			Assert.fail("Field attribut1 not accessible.");
		} catch (final NoSuchFieldException e) {
			Assert.fail("Field attribut1 does not exists.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOType(java.lang.Class, java.lang.reflect.Tyoe)}
	 * .
	 */
	@Test
	public void testGetDTOTypeForParameterizedClass() {
		try {
			final Field field = Bean2.class.getDeclaredField("beans");
			Assert.assertEquals(
					"getFieldType does not return java.util.List<fr.maven.dto.bean.Bean> for beans field",
					"java.util.List<fr.maven.dto.bean.Bean>",
					dtoGeneratorImpl.getDTOType(Bean.class,
							field.getGenericType()));
		} catch (final SecurityException e) {
			Assert.fail("Field attribut1 not accessible.");
		} catch (final NoSuchFieldException e) {
			Assert.fail("Field attribut1 does not exists.");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#getDTOType(java.lang.Class, java.lang.reflect.Tyoe)}
	 * .
	 */
	@Test
	public void testGetDTOTypeForArray() {
		try {
			final Field field = Bean2.class.getDeclaredField("beanArray");
			final String dtoType = dtoGeneratorImpl.getDTOType(Bean.class,
					field.getGenericType());
			Assert.assertEquals(
					"getFieldType does not return fr.maven.dto.bean.Bean[] for beanArray field",
					"fr.maven.dto.bean.Bean[]", dtoType);
		} catch (final SecurityException e) {
			Assert.fail("Field attribut1 not accessible.");
		} catch (final NoSuchFieldException e) {
			Assert.fail("Field attribut1 does not exists.");
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
			final Field field = Bean.class.getDeclaredField("attribut1");
			Assert.assertEquals(
					"getFieldPackage does not result \"\" for attribut1 field",
					"",
					dtoGeneratorImpl.getDTOFieldPackage(Bean.class,
							field.getGenericType()));
		} catch (final SecurityException e) {
			Assert.fail("Field attribut1 not accessible.");
		} catch (final NoSuchFieldException e) {
			Assert.fail("Field attribut1 does not exists.");
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
			final FileWriter beanFileWriter = new FileWriter(new File(""));
			final FileWriter bean2FileWriter = new FileWriter(new File(""));
			dtoGeneratorImpl.fileWriters.put(Bean.class, beanFileWriter);
			dtoGeneratorImpl.fileWriters.put(Bean2.class, bean2FileWriter);
			Assert.assertEquals(beanFileWriter,
					dtoGeneratorImpl.getDTOClassFileWriter(Bean.class));
		} catch (final IOException e) {
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
			dtoGeneratorImpl.makeDTOPackage(Bean.class);
			dtoGeneratorImpl.makeDTOClass(Bean.class);
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "BeanDTO.java");
			Assert.assertTrue("package has not been created", new File(
					generatedDirectory.getAbsolutePath() + File.separator
							+ "fr" + File.separator + "maven" + File.separator
							+ "dto" + File.separator + "bean" + File.separator
							+ "dto").exists());
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validPackage = false;
			while ((line = bf.readLine()) != null) {
				if ("package fr.maven.dto.bean.dto;".equals(line)) {
					validPackage = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Package generated not valid", validPackage);
		} catch (final IOException e) {
			Assert.fail("The package has not been generated (could not write or read file).");
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
			dtoGeneratorImpl.makeDTOPackage(Bean.class);
			dtoGeneratorImpl.makeDTOClass(Bean.class);
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "BeanDTO.java");
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validClass = false;
			while ((line = bf.readLine()) != null) {
				if ("public class BeanDTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Class generated not valid", validClass);
		} catch (final IOException e) {
			Assert.fail("The class has not been generated (could not write or read file).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOClass(java.lang.Class)}
	 * .
	 */
	@Test
	public void testMakeDTOClassWithStaticFields() {
		try {
			dtoGeneratorImpl.makeDTOPackage(Bean.class);
			dtoGeneratorImpl.makeDTOClass(Bean.class);
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "BeanDTO.java");
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validClass = false;
			while ((line = bf.readLine()) != null) {
				if ("public class BeanDTO implements Serializable {"
						.equals(line)) {
					validClass = true;
				}
				if (line.contains("String B;")) {
					Assert.fail("It should generate nothing for static fields.");
				}
				if (line.contains("getB(")) {
					Assert.fail("It should generate nothing for static fields.");
				}
				if (line.contains("setB(")) {
					Assert.fail("It should generate nothing for static fields.");
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Class generated not valid", validClass);
		} catch (final IOException e) {
			Assert.fail("The class has not been generated (could not write or read file).");
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
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			dtoGeneratorImpl.classesToGenerate.add(AnotherBean.class);
			dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			dtoGeneratorImpl.makeDTOField(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validField = false;
			while ((line = bf.readLine()) != null) {
				if ("\tprivate BeanDTO bean;".equals(line)) {
					validField = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Field generated not valid", validField);
		} catch (final IOException e) {
			Assert.fail("The field has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field has not been generated (the source field is not accessible).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOField(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOFieldWithGenericMap() {
		try {
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			dtoGeneratorImpl.classesToGenerate.add(AnotherBean.class);
			dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			dtoGeneratorImpl.makeDTOField(Bean2.class,
					Bean2.class.getDeclaredField("beansMap"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validMapField = false;
			while ((line = bf.readLine()) != null) {
				if ("\tprivate java.util.Map<BeanDTO, AnotherBeanDTO> beansMap;"
						.equals(line)) {
					validMapField = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("MapField generated not valid", validMapField);
		} catch (final IOException e) {
			Assert.fail("The field has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field has not been generated (the source field is not accessible).");
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
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			dtoGeneratorImpl.makeDTOFieldGetter(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldGetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic BeanDTO getBean() {".equals(line)) {
					validFieldGetter = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Field getter generated not valid",
					validFieldGetter);
		} catch (final IOException e) {
			Assert.fail("The field getter has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOFieldGetter(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOFieldGetterForFieldWithOnlyOneCharacter() {
		try {
			dtoGeneratorImpl.makeDTOPackage(Bean.class);
			dtoGeneratorImpl.makeDTOFieldGetter(Bean.class,
					Bean.class.getDeclaredField("a"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "BeanDTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldGetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic String getA() {".equals(line)) {
					validFieldGetter = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Field getter generated not valid",
					validFieldGetter);
		} catch (final IOException e) {
			Assert.fail("The field getter has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
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
			dtoGeneratorImpl.classesToGenerate.add(Bean.class);
			dtoGeneratorImpl.makeDTOPackage(Bean2.class);
			dtoGeneratorImpl.makeDTOFieldSetter(Bean2.class,
					Bean2.class.getDeclaredField("bean"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "Bean2DTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic void setBean(BeanDTO bean) {".equals(line)) {
					validFieldSetter = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Field setter generated not valid",
					validFieldSetter);
		} catch (final IOException e) {
			Assert.fail("The field getter has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		}
	}

	/**
	 * Test method for
	 * {@link fr.maven.dto.generator.impl.DTOGeneratorImpl#makeDTOFieldSetter(java.lang.Class, java.lang.reflect.Field)}
	 * .
	 */
	@Test
	public void testMakeDTOFieldSetterForFieldWithOnlyOneCharacter() {
		try {
			dtoGeneratorImpl.makeDTOPackage(Bean.class);
			dtoGeneratorImpl.makeDTOFieldSetter(Bean.class,
					Bean.class.getDeclaredField("a"));
			dtoGeneratorImpl.getDTOClassFileWriter(Bean.class).close();
			final File file = new File(generatedDirectory.getAbsolutePath()
					+ File.separator + "fr" + File.separator + "maven"
					+ File.separator + "dto" + File.separator + "bean"
					+ File.separator + "dto" + File.separator + "BeanDTO.java");
			dtoGeneratorImpl.getDTOClassFileWriter(Bean2.class).close();
			final BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			boolean validFieldSetter = false;
			while ((line = bf.readLine()) != null) {
				if ("\tpublic void setA(String a) {".equals(line)) {
					validFieldSetter = true;
				}
			}
			bf.close();
			deleteFiles(file);
			Assert.assertTrue("Field setter generated not valid",
					validFieldSetter);
		} catch (final IOException e) {
			Assert.fail("The field getter has not been generated (could not write or read file).");
		} catch (final SecurityException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		} catch (final NoSuchFieldException e) {
			Assert.fail("The field getter has not been generated (the source field is not accessible).");
		}
	}

	private void deleteFiles(final File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (final File fileChild : file.listFiles()) {
					if (fileChild != null && fileChild.exists()) {
						deleteFiles(fileChild);
					}
				}
			}
			file.delete();
		}
	}
}
