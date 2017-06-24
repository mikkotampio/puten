package fi.purkka.puten;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestLexer.class,
	TestParser.class,
})
public class PutenSuite {}