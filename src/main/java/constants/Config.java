package constants;

import utils.FileOperations;

public interface Config {
	
	FileOperations fileOperations = new FileOperations();
	Constants constantVar = new Constants();

    /** VARIABLES **/

    // APP CONFIG DATA
    public static final String BASE_URL = fileOperations.getValueFromPropertyFile(constantVar.CONFIG_WEB_FILE_PATH, "randomUserGenerationURL");
}
