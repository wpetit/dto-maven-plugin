/**
 * 
 */
package fr.maven.dto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.maven.dto.generator.ClassFinder;
import fr.maven.dto.generator.ClassFinderImpl;
import fr.maven.dto.generator.DTOGenerator;
import fr.maven.dto.generator.DTOGeneratorImpl;

/**
 * This class launch the DTO generation.
 * 
 * @author Wilfried Petit
 * 
 */
public class DTOLauncher {

	/**
	 * The DTO generator used.
	 */
	private final DTOGenerator dtoGenerator;

	/**
	 * The class finder used.
	 */
	private final ClassFinder classFinder;

	/**
	 * Constructor.
	 */
	public DTOLauncher() {
		this.dtoGenerator = new DTOGeneratorImpl();
		this.classFinder = new ClassFinderImpl();
	}

	/**
	 * Run the generation.
	 * 
	 * @param generatedDirectory
	 *            the directory where DTOs will be generated.
	 * @param includeClasses
	 *            the list of classes to generate DTOs for.
	 * @throws IOException
	 *             if DTOs can not be generated due to an IOException.
	 * @throws ClassNotFoundException
	 *             if the classes given are not in the classpath.
	 */
	public void execute(final File generatedDirectory,
			final List<String> includeClasses) throws IOException,
			ClassNotFoundException {
		List<Class<?>> classesToGenerate = this.classFinder
				.getClassesToGenerate(includeClasses);
		this.dtoGenerator.setGeneratedDirectory(generatedDirectory);
		this.dtoGenerator.generateDTOs(classesToGenerate);
	}

	/**
	 * Run the generation from command line.
	 * 
	 * @param args
	 *            the first argument is the generated directory, the second one
	 *            is the list of classes we want to generate DTO for.
	 * @throws ClassNotFoundException
	 *             if the classes given have not been found.
	 * @throws IOException
	 *             if the generation failed due to an I/O error.
	 */
	public static void main(final String[] args) throws IOException,
			ClassNotFoundException {
		if (args.length != 2) {
			throw new IllegalArgumentException(
					"The number of argument is wrong.");
		}
		// Retrieve the directory where DTOs will be generated.
		File generatedDirectory = new File(args[0]);
		// Retrieve the classes list we will generate DTOs for.
		List<String> classesToGenerate = new ArrayList<String>();
		for (String clazz : args[1].split(";")) {
			classesToGenerate.add(clazz);
		}
		DTOLauncher dtoLauncher = new DTOLauncher();
		// Execute the generation.
		dtoLauncher.execute(generatedDirectory, classesToGenerate);
	}
}
