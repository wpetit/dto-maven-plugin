/**
 * 
 */
package fr.maven.dto;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.maven.dto.generator.ClassFinder;
import fr.maven.dto.generator.DTOGenerator;
import fr.maven.dto.generator.impl.ClassFinderImpl;
import fr.maven.dto.generator.impl.DTOGeneratorImpl;

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
	 * @param classLoader
	 *            the ClassLoader that contains classes to generate.
	 * @param baseDirectories
	 *            the directories or archives that contains classes to generate
	 *            DTO for.
	 * @param includes
	 *            patterns that classes to generate match.
	 * @param excludes
	 *            patterns that classes to generate do not match.
	 * @param generatedDirectory
	 *            the directory where DTOs will be generated.
	 * @throws ClassNotFoundException
	 *             if the classes found in directories have not been found. This
	 *             should not happened.
	 * @throws IOException
	 *             if DTOs can not be written.
	 */
	public void execute(final ClassLoader classLoader,
			final List<File> baseDirectories, final List<String> includes,
			final List<String> excludes, final File generatedDirectory)
			throws ClassNotFoundException, IOException {
		List<Class<?>> classesToGenerate = this.classFinder
				.getClassesToGenerate(classLoader, baseDirectories, includes,
						excludes);
		this.dtoGenerator.setGeneratedDirectory(generatedDirectory);
		this.dtoGenerator.generateDTOs(classesToGenerate);
	}
}
