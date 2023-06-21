package panomete.jwtauth;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import panomete.jwtauth.security.UserTest;

@Suite
@SuiteDisplayName("Spring boot 3 JWT auth test suite")
@SelectClasses({
		UserTest.class
})
class JwtauthApplicationTests { }
