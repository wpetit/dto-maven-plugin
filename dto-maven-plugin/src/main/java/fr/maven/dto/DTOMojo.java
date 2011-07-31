package fr.maven.dto;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal to generate DTO classes.
 * 
 * @goal dto
 * 
 * @phase generate-sources
 */
public class DTOMojo extends AbstractMojo {

	/**
	 * List of artifacts this plugin depends on. Used for resolving the DTO
	 * maven plugin.
	 * 
	 * @parameter expression="${plugin.artifacts}"
	 * @required
	 * @readonly
	 */
	List<Artifact> pluginArtifacts;

	/**
	 * List of artifacts the project depends on.
	 * 
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 * @readonly
	 */
	Set<Artifact> projectDependencies;

	/**
	 * Location of the project build directory.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Location of the DTO classes generation directory.
	 * 
	 * @parameter expression="${sourceRoot}"
	 *            default-value="${project.build.directory}/generated"
	 * @required
	 */
	private File generatedDirectory;

	/**
	 * List of classes we want to generate DTO for.
	 * 
	 * @parameter expression="${includeClasses}"
	 * 
	 * @required
	 */
	private List<String> includeClasses;

	// /**
	// * Pattern to match classes we want to generate a DTO for.
	// *
	// * @parameter expression="${include}"
	// * @required
	// */
	// private String[] includePatterns;
	//
	// /**
	// * Pattern to match classes we do not want to generate a DTO for.
	// *
	// * @parameter expression="${exclude}"
	// * @required
	// */
	// private String[] excludePatterns;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException {
		try {
			StringBuffer classes = new StringBuffer();
			for (String clazz : this.includeClasses) {
				classes.append(clazz + ";");
			}
			String cmd = "java -classpath "
					+ this.getClasspath(this.outputDirectory)
					+ " fr.maven.dto.DTOLauncher " + this.generatedDirectory
					+ " " + classes;

			int exitValue = this.runProcess(cmd);
			if (exitValue != 0) {
				throw new MojoExecutionException("The generation has failed.");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("The generation has failed.", e);
		}
	}

	protected String getClasspath(File directory) {
		StringBuffer classpath = new StringBuffer();
		for (File clazz : this.getFiles(directory)) {
			classpath.append(clazz.getAbsolutePath() + ";");
		}
		for (Artifact artifact : this.pluginArtifacts) {
			classpath.append(artifact.getFile().getAbsolutePath() + ";");
		}
		for (Artifact artifact : this.projectDependencies) {
			if (artifact.getFile() != null) {
				classpath.append(artifact.getFile().getAbsolutePath() + ";");
			}
		}

		classpath.append(";.");
		return classpath.toString();
	}

	protected List<File> getFiles(File file) {
		List<File> classFiles = new ArrayList<File>();
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File childFile : file.listFiles()) {
					classFiles.addAll(this.getFiles(childFile));
				}
			} else if (file.getAbsolutePath().endsWith(".class")) {
				classFiles.add(file);
			}
		}
		return classFiles;
	}

	protected int runProcess(String cmd) throws IOException,
			InterruptedException {
		Process process = Runtime.getRuntime().exec(cmd);

		// Redirect error stream to logs
		InputStream stdErr = process.getErrorStream();
		InputStreamReader isrErr = new InputStreamReader(stdErr);
		BufferedReader br = new BufferedReader(isrErr);
		String lineErr = null;
		while ((lineErr = br.readLine()) != null) {
			this.getLog().error(lineErr);
		}

		// Redirect input stream to logs
		InputStream stdIn = process.getInputStream();
		InputStreamReader isrIn = new InputStreamReader(stdIn);
		BufferedReader brIn = new BufferedReader(isrIn);
		String lineIn = null;
		while ((lineIn = brIn.readLine()) != null) {
			this.getLog().error(lineIn);
		}
		return process.waitFor();
	}
}
