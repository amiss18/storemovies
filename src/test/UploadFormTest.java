package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.subtitlor.form.UploadForm;
import com.subtitlor.utilities.Language;

class UploadFormTest {

	@Test
	void testReplaceLatinCharsToChars() {
		
		String nameOfFile ="déjouez les pièges de jakarta ee";
		assertEquals( "dejouez les pieges de jakarta ee", UploadForm.replaceLatinCharsToChars(nameOfFile));
	}

}
