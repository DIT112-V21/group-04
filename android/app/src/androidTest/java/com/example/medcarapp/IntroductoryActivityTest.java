package com.example.medcarapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class IntroductoryActivityTest {

    private IntroductoryActivity introductoryActivity;

    private ServerSelection serverSelection;

    @Rule
    public ActivityTestRule<IntroductoryActivity> introductoryActivityActivityTestRule = new ActivityTestRule<>(IntroductoryActivity.class);

    @Rule
    public ActivityTestRule<ServerSelection> serverSelectionActivityTestRule = new ActivityTestRule<ServerSelection>(ServerSelection.class,true, false);

    @Before
    public void setUp() {
        introductoryActivity = introductoryActivityActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.medcarapp", appContext.getPackageName());
    }

    @Test
    public void verifyLoadedElements(){
        View view = introductoryActivity.findViewById(R.id.logo);
        assertNotNull(view);
        onView(withTagValue(equalTo(R.drawable.logo7))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyIntent(){
        Intent intent = new Intent(getInstrumentation().getTargetContext(),ServerSelection.class);
        serverSelectionActivityTestRule.launchActivity(intent);
        intended(hasComponent(ServerSelection.class.getName()));
    }

    @After
    public void tearDown() {
        introductoryActivity = null;
        Intents.release();
    }
}