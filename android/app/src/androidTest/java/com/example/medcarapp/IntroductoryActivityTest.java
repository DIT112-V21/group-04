package com.example.medcarapp;

import android.content.Context;
import android.view.View;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntroductoryActivityTest {

    private IntroductoryActivity introductoryActivity;

    @Rule
    public ActivityTestRule<IntroductoryActivity> introductoryActivityActivityTestRule = new ActivityTestRule<>(IntroductoryActivity.class);

    @Before
    public void setUp() throws Exception {
        introductoryActivity = introductoryActivityActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.medcarapp", appContext.getPackageName());
    }

    @Test
    public void verifyLoadedIds(){
        View view = introductoryActivity.findViewById(R.id.logo);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        introductoryActivity = null;
        Intents.release();
    }
}