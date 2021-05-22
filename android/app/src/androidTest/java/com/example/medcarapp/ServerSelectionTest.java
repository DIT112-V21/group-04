package com.example.medcarapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.anything;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class ServerSelectionTest {

    private ServerSelection serverSelection;
    private ServerAdapter serverAdapter;

    @Rule
    public ActivityTestRule<ServerSelection> serverSelectionActivityTestRule = new ActivityTestRule<>(ServerSelection.class);

    @Before
    public void setUp() throws Exception {
        serverSelection = serverSelectionActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.medcarapp", appContext.getPackageName());
    }

    @Test
    public void creditButtonDisplaysPopup() {
        assertNotNull(serverSelection);
        onView(withId(R.id.btnCredit)).perform(click());
        onView(withId(R.id.credits)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextSmartcar_shield)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextCopyrightDimitris)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextLicenseMIT)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextJoystick)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextCopyrightDamien)).check(matches(isDisplayed()));
        onView(withText(R.string.creditsTextLicenseApache)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyLoadedIds(){
        View view = serverSelection.findViewById(R.id.rvServerSelection);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverNames);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverDescription);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverPic);
        assertNotNull(view);
    }

    /*@Test
    public void checkServerSwitch() {
        final RecyclerView rvServerSelection = serverSelection.findViewById(R.id.rvServerSelection);
        assertNotNull(rvServerSelection);
        onData(instanceOf(ServerAdapter)).atPosition(0).perform(click());
        Intent intent = new Intent(serverSelection,MainActivity.class);
        intent.putExtra(serverAdapter.context.getString(R.string.switchServer), false);
    }*/

    @After
    public void tearDown() throws Exception {
        serverSelection = null;
        Intents.release();
    }

}