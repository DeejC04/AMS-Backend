package com.ams.googleOAuth;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ams.restapi.authentication.CanvasSectionRefresher;

public class GoogleOAuthTests {
    @Test 
    void TestSectionCode() throws IOException{
        CanvasSectionRefresher test = new CanvasSectionRefresher();
        test.updateUserSections();
    }
}
