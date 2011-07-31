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
 * @author Wilfried Petit
 * 
 */
public class DTOLauncher {

	private final DTOGenerator dtoGenerator;
	private final ClassFinder classFinder;

	public DTOLauncher() {
		this.dtoGenerator = new DTOGeneratorImpl();
		this.classFinder = new ClassFinderImpl();
	}

	public void execute(File generatedDirectory, List<String> includeClasses)
			throws IOException, ClassNotFoundException {
		List<Class<?>> classesToGenerate = this.classFinder
				.getClassesToGenerate(includeClasses);
		this.dtoGenerator.setGeneratedDirectory(generatedDirectory);
		this.dtoGenerator.generateDTOs(classesToGenerate);
	}

	public static void main(String[] args) {
		File generatedDirectory = new File(args[0]);
		List<String> classesToGenerate = new ArrayList<String>();
		for (String clazz : args[1].split(";")) {
			classesToGenerate.add(clazz);
		}
		DTOLauncher dtoLauncher = new DTOLauncher();
		try {
			dtoLauncher.execute(generatedDirectory, classesToGenerate);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
