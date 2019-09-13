package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.subtitlor.utilities.*;
class SubtitlesHandlerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void given_Time_From_String() {
		String str = "00:00:10,996";// --> 00:00:13,198";
		LocalTime lt = LocalTime.parse("00:00:10.996");
		assertEquals(lt, SubtitlesHandler.parseStringToLocalTime(str) );
	}

	@Test
	void given_Millis_From_Time() {
		String[] str = "00:00:10,996".split("\\,");
		assertEquals(996, SubtitlesHandler.extractMillis(str ));
	}

}
