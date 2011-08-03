/**
 * 
 */
package fr.maven.dto.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link DTOGenerator} implementation.
 * 
 * @author Wilfried Petit
 * 
 */
public class DTOGeneratorImpl implements DTOGenerator {

	/**
	 * File writes associated to classes.
	 */
	protected final Map<Class<?>, FileWriter> fileWriters;

	/**
	 * The classes list we want to generate for.
	 */
	protected List<Class<?>> classesToGenerate;

	/**
	 * The directory where DTOs will be generated.
	 */
	protected File generatedDirectory;

	/**
	 * Constructor.
	 */
	public DTOGeneratorImpl() {
		this.classesToGenerate = new ArrayList<Class<?>>();
		this.fileWriters = new HashMap<Class<?>, FileWriter>();
	}

	/**
	 * Set the DTOs generated directory.
	 * 
	 * @param generatedDirectory
	 *            the DTOs generated directory to set
	 */
	@Override
	public void setGeneratedDirectory(final File generatedDirectory) {
		this.generatedDirectory = generatedDirectory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 * 
	 * @see fr.maven.dto.generator.DTOGenerator#generateDTOs(java.util.List)
	 */
	@Override
	public void generateDTOs(final List<Class<?>> classes) throws IOException {
		this.classesToGenerate = classes;
		for (Class<?> clazz : classes) {
			this.generateDTO(clazz);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see fr.maven.dto.generator.DTOGenerator#generateDTO(java.lang.Class)
	 */
	@Override
	public void generateDTO(final Class<?> clazz) throws IOException {
		this.makeDTOPackage(clazz);
		this.makeDTOClass(clazz);
		this.getDTOClassFileWriter(clazz).close();
	}

	/**
	 * Create the directory of the class package.
	 * 
	 * @param clazz
	 *            the class we want to create the package directory for.
	 * @throws IOException
	 *             if the directory creation failed.
	 */
	protected void makeDTOPackage(final Class<?> clazz) throws IOException {
		File packageDirectory = new File(
				this.generatedDirectory.getAbsolutePath()
						+ File.separator
						+ this.getDTOPackage(clazz).replace('.',
								File.separatorChar));
		if (!packageDirectory.exists()) {
			boolean directoryCreated = packageDirectory.mkdirs();
			if (!directoryCreated) {
				throw new IOException(
						"The generated directory can not be created.");
			}
		}
	}

	/**
	 * Return the package name of the DTO for the clazz given.
	 * 
	 * @param clazz
	 *            the class we want to generate DTO for.
	 * @return the package name got.
	 */
	protected String getDTOPackage(final Class<?> clazz) {
		return clazz.getPackage().getName() + ".dto";
	}

	/**
	 * Check if the class belongs to the classes we want to generate DTO for.
	 * 
	 * @param clazz
	 *            the class to check.
	 * @return <code>true</code> if the classes list contains the class.
	 */
	protected boolean isClassToGenerate(final Class<?> clazz) {
		return this.classesToGenerate.contains(clazz);
	}

	/**
	 * Return the type of the DTO field class that belongs to the class given.
	 * 
	 * @param clazz
	 *            the class that contains the field.
	 * @param field
	 *            the field we want to know its DTO type.
	 * @return the type.
	 */
	protected String getDTOFieldType(final Class<?> clazz, final Field field) {
		String fieldType = field.getType().getSimpleName();
		if (this.isClassToGenerate(field.getType())) {
			fieldType += "DTO";
		}
		if (field.getType().isArray()) {
			fieldType += "[]";
		}
		return fieldType;
	}

	/**
	 * Return the DTO field package class that belongs to the class given.
	 * 
	 * @param clazz
	 *            the class that contains the field.
	 * @param field
	 *            the field we want to know its DTO package.
	 * @return the type.
	 */
	protected String getDTOFieldPackage(final Class<?> clazz, final Field field) {
		Package fieldPackage = field.getType().getPackage();
		String result = "";
		if (fieldPackage != null && !"java.lang".equals(fieldPackage.getName())) {
			if (this.isClassToGenerate(field.getType())) {
				if (!clazz.getPackage().getName()
						.equals(field.getType().getPackage().getName())) {
					result = this.getDTOPackage(field.getType()) + ".";
				}
			} else {
				result = field.getType().getPackage().getName() + ".";
			}
		}
		return result;
	}

	/**
	 * Return the file writer used to write the DTO for the class given.
	 * 
	 * @param clazz
	 *            the class we want to generate a DTO for.
	 * @return the file write associated to the DTO.
	 * @throws IOException
	 *             if the file is not writable.
	 */
	protected FileWriter getDTOClassFileWriter(final Class<?> clazz)
			throws IOException {
		if (!this.fileWriters.containsKey(clazz)) {
			String directory = this.generatedDirectory.getAbsolutePath()
					+ File.separator
					+ this.getDTOPackage(clazz).replace(".", File.separator);
			File classFile = new File(directory.concat(File.separator)
					.concat(clazz.getSimpleName()).concat("DTO.java"));
			if (classFile.exists()) {
				boolean fileDeleted = classFile.delete();
				if (fileDeleted) {
					boolean fileCreated = classFile.createNewFile();
					if (!fileCreated) {
						throw new IOException("The file " + classFile
								+ " can not be created.");
					}
				}
			}
			FileWriter fw = new FileWriter(classFile);
			this.fileWriters.put(clazz, fw);
			return fw;
		} else {
			return this.fileWriters.get(clazz);
		}
	}

	/**
	 * Write class part in the DTO generation file.
	 * 
	 * @param clazz
	 *            the clazz we want a DTO for.
	 * @throws IOException
	 *             if the file is not writable.
	 */
	protected void makeDTOClass(final Class<?> clazz) throws IOException {
		FileWriter fw = this.getDTOClassFileWriter(clazz);
		fw.write("package " + this.getDTOPackage(clazz) + ";" + "\n\n");
		fw.write("import java.io.Serializable;\n\n");

		fw.write("/**\n");
		fw.write(" * This class was generated by the DTO Maven Plugin.\n");
		fw.write(" * "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())
				+ "\n");
		fw.write(" */\n");
		fw.write("public class " + clazz.getSimpleName()
				+ "DTO implements Serializable {\n\n");
		fw.write("\tprivate static final long serialVersionUID = 1L;\n\n");
		for (Field field : clazz.getDeclaredFields()) {
			this.makeDTOField(clazz, field);
		}
		for (Field field : clazz.getDeclaredFields()) {
			this.makeDTOFieldGetter(clazz, field);
			this.makeDTOFieldSetter(clazz, field);
		}
		fw.write("}");
	}

	/**
	 * Write field part in the DTO generation file.
	 * 
	 * @param clazz
	 *            the clazz we want a DTO for.
	 * @param field
	 *            the field we want to write the part for.
	 * @throws IOException
	 *             if the file is not writable.
	 */
	protected void makeDTOField(final Class<?> clazz, final Field field)
			throws IOException {
		FileWriter fw = this.getDTOClassFileWriter(clazz);
		String fieldPackage = this.getDTOFieldPackage(clazz, field);
		String fieldType = this.getDTOFieldType(clazz, field);
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ field.getName() + "\n");
		fw.write("\t */\n");
		fw.write("\tprivate " + fieldPackage + fieldType + " "
				+ field.getName() + ";\n\n");

	}

	/**
	 * Write field getter part in the DTO generation file.
	 * 
	 * @param clazz
	 *            the clazz we want a DTO for.
	 * @param field
	 *            the field we want to write the part for.
	 * @throws IOException
	 *             if the file is not writable.
	 */
	protected void makeDTOFieldGetter(final Class<?> clazz, final Field field)
			throws IOException {
		FileWriter fw = this.getDTOClassFileWriter(clazz);
		String methodSignature;
		char firstCharacter = field.getName().charAt(0);
		if (field.getType().equals(boolean.class)) {
			methodSignature = "is"
					+ field.getName().replace(field.getName().charAt(0),
							Character.toUpperCase(firstCharacter));
		} else {
			methodSignature = "get"
					+ field.getName().replace(field.getName().charAt(0),
							Character.toUpperCase(firstCharacter));
		}
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ methodSignature + "()\n");
		fw.write("\t */\n");
		fw.write("\tpublic " + this.getDTOFieldPackage(clazz, field)
				+ this.getDTOFieldType(clazz, field) + " " + methodSignature
				+ "() {\n");
		fw.write("\t\treturn this." + field.getName() + ";\n");
		fw.write("\t}\n\n");
	}

	/**
	 * Write field setter part in the DTO generation file.
	 * 
	 * @param clazz
	 *            the clazz we want a DTO for.
	 * @param field
	 *            the field we want to write the part for.
	 * @throws IOException
	 *             if the file is not writable.
	 */
	protected void makeDTOFieldSetter(final Class<?> clazz, final Field field)
			throws IOException {
		FileWriter fw = this.getDTOClassFileWriter(clazz);
		char firstCharacter = field.getName().charAt(0);
		String methodSignature = "set"
				+ field.getName().replace(field.getName().charAt(0),
						Character.toUpperCase(firstCharacter));
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ methodSignature + "(" + field.getType().getSimpleName()
				+ ")\n");
		fw.write("\t */\n");
		fw.write("\tpublic void " + methodSignature + "("
				+ this.getDTOFieldPackage(clazz, field)
				+ this.getDTOFieldType(clazz, field) + " " + field.getName()
				+ ") {\n");
		fw.write("\t\tthis." + field.getName() + " = " + field.getName()
				+ ";\n");
		fw.write("\t}\n\n");
	}
}
