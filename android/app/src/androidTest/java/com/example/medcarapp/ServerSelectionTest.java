package com.example.medcarapp;

import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.PositionAssertions.isPartiallyBelow;
import static androidx.test.espresso.assertion.PositionAssertions.isPartiallyLeftOf;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pl.droidsonroids.gif.GifImageView;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ServerSelectionTest {

    private ServerSelection serverSelection;
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<ServerSelection> serverSelectionActivityTestRule = new ActivityTestRule<>(ServerSelection.class);

    @Before
    public void setUp() {
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
    public void verifyLoadedElements(){
        View view = serverSelection.findViewById(R.id.rvServerSelection);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverNames);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverDescription);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.serverPic);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.Introtext);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.logo);
        assertNotNull(view);
        view = serverSelection.findViewById(R.id.btnCredit);
        assertNotNull(view);
        GifImageView gifImageView = serverSelection.findViewById(R.id.medcarGif);
        assertNotNull(gifImageView);
        onView(withTagValue(equalTo(R.drawable.logo7))).check(matches(isDisplayed()));
        onView(withTagValue(equalTo(R.drawable.medcar))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyLayoutPositions(){
        //Using the top logo as a reference to every other component
        onView(withId(R.id.rvServerSelection)).check(isCompletelyBelow(withId(R.id.logo)));
        onView(withId(R.id.Introtext)).check(isPartiallyBelow(withId(R.id.logo)));
        onView(withId(R.id.btnCredit)).check(isPartiallyLeftOf(withId(R.id.logo)));
        onView(withId(R.id.medcarGif)).check(isCompletelyBelow(withId(R.id.logo)));
    }

    @Test
    public void checkOfflineSwitch() {
        //Check if recyclerview is not null
        final RecyclerView rvServerSelection = serverSelection.findViewById(R.id.rvServerSelection);
        assertNotNull(rvServerSelection);

        //Check recyclerview images
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(0)).check(matches(hasDescendant(withDrawable(R.drawable.offline))));

        //Check recyclerview text
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(0)).check(matches(hasDescendant(withText("Offline mode"))));
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(0)).check(matches(hasDescendant(withText("Use within the same department"))));

        //Check recyclerview click
        onView(withId(R.id.rvServerSelection)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //Check intents
        Intents.intended(hasComponent(new ComponentName(getInstrumentation().getTargetContext(),MainActivity.class)));
        onView(withId(R.id.logo)).check(matches(isDisplayed()));
        onView(withId(R.id.Introtext)).check(matches(withText(R.string.chooseCarFromListInstruction)));
    }

    @Test
    public void checkOnlineSwitch(){
        //Check recyclerview images
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(1)).check(matches(hasDescendant(withDrawable(R.drawable.online))));

        //Check recyclerview text
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(1)).check(matches(hasDescendant(withText("Online mode"))));
        onView(withRecyclerView(R.id.rvServerSelection).atPosition(1)).check(matches(hasDescendant(withText("Use from a different department"))));

        //Check recyclerview click
        onView(withId(R.id.rvServerSelection)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        //Check intents
        Intents.intended(hasComponent(new ComponentName(getInstrumentation().getTargetContext(),MainActivity.class)));
        onView(withId(R.id.logo)).check(matches(isDisplayed()));
        onView(withId(R.id.Introtext)).check(matches(withText(R.string.chooseCarFromListInstruction)));
    }

    @After
    public void tearDown() {
        serverSelection = null;
        Intents.release();
    }

}