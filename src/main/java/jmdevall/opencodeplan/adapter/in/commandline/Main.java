package jmdevall.opencodeplan.adapter.in.commandline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jmdevall.opencodeplan.application.CodePlan;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.dependencygraph.LineColPos;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;
import jmdevall.opencodeplan.domain.instruction.InstructuionNatural;
import jmdevall.opencodeplan.domain.instruction.NodeSearchDescriptor;
import jmdevall.opencodeplan.domain.instruction.Seed;
import jmdevall.opencodeplan.domain.plangraph.NodeTypeTag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String args[]) throws IOException {
		Main main = new Main();
		if(args.length==0) {
			printHelp();
			return;
		}
		
		Options options=readOptions(args);
		main.opencodeplanmain(options);
	}

	private static DeltaSeeds getSeeds(Options options) {
		DeltaSeeds ds=new DeltaSeeds();
		
		Seed initialCommand=Seed.builder()
				.instruction(new InstructuionNatural(options.getInstruction()))
				.block(NodeSearchDescriptor.builder()
						.file(options.getFile())
						.nodeTypeTag(NodeTypeTag.BodyOfMethod)
						.position(LineColPos.newPosition(options.getLine(), options.getColumn()))
						.build()
				 )
				.build();
		
		ds.addSeed(initialCommand);
		return ds;
	}

	private static Options readOptions(String[] args) {
		  Options options = new Options();
		   for (int i = 0; i < args.length; i++) {
		       switch (args[i]) {
		           case "-f":
		               options.setFile(args[++i]);
		               options.setLine(Integer.parseInt(args[++i]));
		               options.setColumn(Integer.parseInt(args[++i]));
		               break;
		           case "-i":
		        	   options.setInstruction(args[++i]);
		        	   
		               break;
		           default:
		               System.out.println("Unknown option: " + args[i]);
		       }
		   }
		   return options;
	}

	private static void printHelp() {
		StringBuffer help=new StringBuffer();
		help.append("opencodeplan commandline 0.0.0\n");
		help.append("usage: opencodeplan -f file line column -i \"instruction\" \n");
		help.append("example: opencodeplan -f /mypackage/Miclass.java 14 1 -i \"rename method to foo\" \n");
		System.out.println(help);
	}

	private void opencodeplanmain(Options options) throws IOException {
		Properties opencodeplanProps = loadProperties("opencodeplan.properties");
		
		CodePlan codeplan=OpenCodeplanFactory.newFromProperties(opencodeplanProps);
		Repository r=OpenCodeplanFactory.getDefaultMavenProjectFromCurrentFolder();
		
		codeplan.codePlan(r, getSeeds(options));
	}

	public Properties loadProperties(String filename) {
		Properties properties = new Properties();

		// Intenta cargar el archivo desde la ruta actual
		File currentDirectory = new File(".");
		File propertiesFile = new File(currentDirectory, filename);
		try (InputStream inputStream = new FileInputStream(propertiesFile)) {
			properties.load(inputStream);
			log.info("using opencodeplan.properties from current folder");
			return properties;
		} catch (IOException e) {
			// Ignora la excepción y pasa a la siguiente ubicación
		}

		// Intenta cargar el archivo desde ~/.foo/
		propertiesFile = new File(System.getProperty("user.home"), ".opencodeplan/" + filename);
		try (InputStream inputStream = new FileInputStream(propertiesFile)) {
			properties.load(inputStream);
			log.info("using opencodeplan.properties from ~/.opencodeplan");
			return properties;
		} catch (IOException e) {
			// Ignora la excepción y pasa a la siguiente ubicación
		}

		// Intenta cargar el archivo desde src/main/resources/
		InputStream resourceStream = getClass().getResourceAsStream("/" + filename);
		if (resourceStream != null) {
			try {
				properties.load(resourceStream);
				log.info("using default opencodeplan.properties inside jar");
				return properties;
			} catch (IOException e) {
				// Ignora la excepción y devuelve las propiedades vacías
			}
		}

		// Si llegamos aquí, no se pudo cargar ningún archivo
		return properties;
	}
}
