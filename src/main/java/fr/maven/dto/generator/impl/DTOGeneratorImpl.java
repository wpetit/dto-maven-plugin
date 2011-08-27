/**
 * 
 */
package fr.maven.dto.generator.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.maven.dto.generator.DTOGenerator;

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
	 * @see fr.maven.dto.generator.DTOGenerator#generateDTOs(java.util.List)
	 */
	@Override
	public void generateDTOs(final List<Class<?>> classes) throws IOException {
		this.classesToGenerate = classes;
		for (final Class<?> clazz : classes) {
			this.generateDTO(clazz);
		}
	}

	/**
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
		final File packageDirectory = new File(
				this.generatedDirectory.getAbsolutePath()
						+ File.separator
						+ this.getDTOPackage(clazz).replace('.',
								File.separatorChar));
		if (!packageDirectory.exists()) {
			final boolean directoryCreated = packageDirectory.mkdirs();
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
		return this.getDTOType(clazz, field.getGenericType());
	}

	/**
	 * Return the type canonical name for the field type given.
	 * 
	 * @param clazz
	 *            the class that contains the field.
	 * @param type
	 *            the field type.
	 * @return the type canonical name.
	 */
	protected String getDTOType(final Class<?> clazz, final Type type) {
		final StringBuffer typeSimpleName = new StringBuffer();
		typeSimpleName.append(this.getDTOFieldPackage(clazz, type));
		if (type instanceof ParameterizedType) {
			final ParameterizedType parameterizedType = (ParameterizedType) type;
			typeSimpleName.append(((Class<?>) parameterizedType.getRawType())
					.getSimpleName() + "<");
			final Type[] typeArguments = parameterizedType
					.getActualTypeArguments();
			for (int i = 0; i < typeArguments.length; i++) {
				typeSimpleName.append(this.getDTOType(clazz, typeArguments[i]));
				if (i != (typeArguments.length - 1)) {
					typeSimpleName.append(", ");
				}
			}
			typeSimpleName.append(">");
		} else {
			final Class<?> clazzType = ((Class<?>) type);
			if (clazzType.isArray()) {
				typeSimpleName.append(this.getArrayComponentType(clazzType)
						.getSimpleName());
				for (int i = 0; i < this.getArrayDimension(clazzType); i++) {
					typeSimpleName.append("[]");
				}
			} else {
				typeSimpleName.append(((Class<?>) type).getSimpleName());
				if (this.isClassToGenerate(clazzType)) {
					typeSimpleName.append("DTO");
				}
			}
		}
		return typeSimpleName.toString();
	}

	/**
	 * Return the real component type of an array. E.g. for Bean[], Bean[][]
	 * ,... , it returns Bean
	 * 
	 * @param clazz
	 *            the array
	 * @return the component type
	 */
	protected Class<?> getArrayComponentType(final Class<?> clazz) {
		if (clazz.isArray()) {
			return this.getArrayComponentType(clazz.getComponentType());
		} else {
			return clazz;
		}
	}

	/**
	 * Return the dimension of the array. E.g. : For Bean[] it returns 1. For
	 * Bean[][] it returns 2...
	 * 
	 * @param clazz
	 *            the array we want the dimension.
	 * @return the dimension.
	 */
	protected int getArrayDimension(final Class<?> clazz) {
		int arrayDimension = 0;
		if (clazz.isArray()) {
			arrayDimension++;
			arrayDimension += this.getArrayDimension(clazz.getComponentType());
		}
		return arrayDimension;
	}

	/**
	 * Return the DTO field package class that belongs to the class given.
	 * 
	 * @param clazz
	 *            the class that contains the field.
	 * @param fieldType
	 *            the field type we want to know its DTO package.
	 * @return the type.
	 */
	protected String getDTOFieldPackage(final Class<?> clazz,
			final Type fieldType) {
		Class<?> fieldTypeClass;
		Package fieldPackage;
		if (fieldType instanceof ParameterizedType) {
			fieldTypeClass = ((Class<?>) ((ParameterizedType) fieldType)
					.getRawType());
		} else {
			fieldTypeClass = (Class<?>) fieldType;
			if (fieldTypeClass.isArray()) {
				fieldTypeClass = this.getArrayComponentType(fieldTypeClass);
			}
		}
		fieldPackage = ((Class<?>) fieldTypeClass).getPackage();
		String result = "";
		if (fieldPackage != null && !"java.lang".equals(fieldPackage.getName())) {
			if (this.isClassToGenerate(fieldTypeClass)) {
				if (!clazz.getPackage().getName()
						.equals(fieldTypeClass.getPackage().getName())) {
					result = this.getDTOPackage(fieldTypeClass) + ".";
				}
			} else {
				result = fieldTypeClass.getPackage().getName() + ".";
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
			final String directory = this.generatedDirectory.getAbsolutePath()
					+ File.separator
					+ this.getDTOPackage(clazz).replace(".", File.separator);
			final File classFile = new File(directory.concat(File.separator)
					.concat(clazz.getSimpleName()).concat("DTO.java"));
			if (classFile.exists()) {
				final boolean fileDeleted = classFile.delete();
				if (fileDeleted) {
					final boolean fileCreated = classFile.createNewFile();
					if (!fileCreated) {
						throw new IOException("The file " + classFile
								+ " can not be created.");
					}
				}
			}
			final FileWriter fw = new FileWriter(classFile);
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
		final FileWriter fw = this.getDTOClassFileWriter(clazz);
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
		for (final Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				this.makeDTOField(clazz, field);
			}
		}
		for (final Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				this.makeDTOFieldGetter(clazz, field);
				this.makeDTOFieldSetter(clazz, field);
			}
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
		final FileWriter fw = this.getDTOClassFileWriter(clazz);
		final String fieldType = this.getDTOFieldType(clazz, field);
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ field.getName() + "\n");
		fw.write("\t */\n");
		fw.write("\tprivate " + fieldType + " " + field.getName() + ";\n\n");

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
		final FileWriter fw = this.getDTOClassFileWriter(clazz);
		String methodSignature;
		final char firstFieldNameCharacterUpper = Character.toUpperCase(field
				.getName().charAt(0));
		String fieldNameWithoutFirstCharacter = "";
		if (field.getName().length() > 1) {
			fieldNameWithoutFirstCharacter = field.getName().substring(1);
		}
		if (field.getType().equals(boolean.class)) {
			methodSignature = "is" + firstFieldNameCharacterUpper
					+ fieldNameWithoutFirstCharacter;
		} else {

			methodSignature = "get" + firstFieldNameCharacterUpper
					+ fieldNameWithoutFirstCharacter;
		}
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ methodSignature + "()\n");
		fw.write("\t */\n");
		fw.write("\tpublic " + this.getDTOFieldType(clazz, field) + " "
				+ methodSignature + "() {\n");
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
		final FileWriter fw = this.getDTOClassFileWriter(clazz);

		final char firstFieldNameCharacterUpper = Character.toUpperCase(field
				.getName().charAt(0));
		String fieldNameWithoutFirstCharacter = "";
		if (field.getName().length() > 1) {
			fieldNameWithoutFirstCharacter = field.getName().substring(1);
		}
		final String methodSignature = "set" + firstFieldNameCharacterUpper
				+ fieldNameWithoutFirstCharacter;
		fw.write("\t/**\n");
		fw.write("\t * @see " + clazz.getCanonicalName() + "#"
				+ methodSignature + "(" + field.getType().getSimpleName()
				+ ")\n");
		fw.write("\t */\n");
		fw.write("\tpublic void " + methodSignature + "("
				+ this.getDTOFieldType(clazz, field) + " " + field.getName()
				+ ") {\n");
		fw.write("\t\tthis." + field.getName() + " = " + field.getName()
				+ ";\n");
		fw.write("\t}\n\n");
	}
}
