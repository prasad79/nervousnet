package ch.ethz.coss.nervousnet.hub.authentication;

public class AuthenticationController {

	/*
	 * Function to check if the Application requesting for data is authorized
	 * and has the right auth token.
	 */
	public boolean checkAuthToken(long authToken, String appName) {

		// return true for all applications for now.
		return true;
	}

	public boolean checkSensorPermission(String appName) {
		// return true for all sensors for now.
		return true;
	}
}
