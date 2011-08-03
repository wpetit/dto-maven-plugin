/**
 * 
 */
package fr.maven.dto.generator;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This interface describes a DTO generator from {@link Class}.
 * 
 * @author Wilfried Petit
 * 
 */
public interface DTOGenerator {

	/**
	 * Generate DTOs for the classes given in parameter.
	 * 
	 * @param classes
	 *            the classes that we want to generate DTO for.
	 * @throws IOException
	 *             if the creation of dtos failed.
	 */
	void generateDTOs(List<Class<?>> classes) throws IOException;

	/**
	 * Generate DTOs for the class given in parameter.
	 * 
	 * @param clazz
	 *            the class that we want to generate DTO for.
	 * @throws IOException
	 *             if the creation of dtos failed.
	 */
	void generateDTO(Class<?> clazz) throws IOException;

	/**
	 * The directory where we want to generate DTO classes.
	 * 
	 * @param generatedDirectory
	 *            the directory.
	 */
	void setGeneratedDirectory(File generatedDirectory);
}
