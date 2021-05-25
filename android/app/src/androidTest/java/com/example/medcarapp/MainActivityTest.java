package com.example.medcarapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class MainActivityTest {

    private MainActivity mainActivity;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, MainActivity.class);
            result.putExtra(targetContext.getString(R.string.switchServer),true);
            return result;
        }
    };

    @Rule
    public ActivityTestRule<ServerSelection> serverSelectionActivityTestRule = new ActivityTestRule<>(ServerSelection.class,true,false);

    @Before
    public void setUp() {
        mainActivity = mainActivityActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.medcarapp", appContext.getPackageName());
    }

    @Test
    public void verifyLoadedElements(){
        View view = mainActivity.findViewById(R.id.rvAvaliableCars);
        assertNotNull(view);
        view = mainActivity.findViewById(R.id.carNames);
        assertNotNull(view);
        view = mainActivity.findViewById(R.id.description);
        assertNotNull(view);
        view = mainActivity.findViewById(R.id.btnConnect);
        assertNotNull(view);
        view = mainActivity.findViewById(R.id.Introtext);
        assertNotNull(view);
        view = mainActivity.findViewById(R.id.btnConnect);
        assertNotNull(view);
        onView(withTagValue(equalTo(R.drawable.logo7))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyLayoutPositions(){
        //Using the top logo as a reference to every other component
        onView(withId(R.id.Introtext)).check(isCompletelyBelow(withId(R.id.logo)));
        onView(withId(R.id.rvAvaliableCars)).check(isCompletelyBelow(withId(R.id.logo)));
        onView(withId(R.id.btnConnect)).check(isCompletelyBelow(withId(R.id.logo)));
    }

    @Test
    public void validateRecyclerView(){
        //Check if recyclerview is not null
        final RecyclerView rvAvailableCars = mainActivity.findViewById(R.id.rvAvaliableCars);
        assertNotNull(rvAvailableCars);

        //Check recyclerview images
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(0)).check(matches(hasDescendant(withId(R.id.carLogo))));

        //Check recyclerview text
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(0)).check(matches(hasDescendant(withText("Medcar 1"))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(0)).check(matches(hasDescendant(withText("This is the medical car 1."))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(1)).check(matches(hasDescendant(withText("Medcar 2"))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(1)).check(matches(hasDescendant(withText("This is the medical car 2."))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(2)).check(matches(hasDescendant(withText("Medcar 3"))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(2)).check(matches(hasDescendant(withText("This is the medical car 3."))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(3)).check(matches(hasDescendant(withText("Medcar 4"))));
        onView(withRecyclerView(R.id.rvAvaliableCars).atPosition(3)).check(matches(hasDescendant(withText("This is the medical car 4."))));

        //Check recyclerview click
        onView(withId(R.id.rvAvaliableCars)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.rvAvaliableCars)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.rvAvaliableCars)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.rvAvaliableCars)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
    }


    @Test
    public void checkConnectButton(){
        //Check connect button click and text
        assertNotNull(mainActivity);
        onView(withId(R.id.btnConnect)).perform(click());
        onView(withText(R.string.connectButtonDisabledMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.rvAvaliableCars)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.btnConnect)).perform(click());

        //Check intent
        Intents.intended(hasComponent(new ComponentName(getInstrumentation().getTargetContext(),ManualControl.class)));
        onView(withId(R.id.cameraView)).check(matches(isDisplayed()));
        onView(withId(R.id.autonomousDrivingButton)).check(matches(withText(R.string.startingAutonomousButtonText)));
    }

    @Test
    public void backToServerSelection(){
        Espresso.pressBackUnconditionally();
        serverSelectionActivityTestRule.launchActivity(null);
        intended(hasComponent(ServerSelection.class.getName()));
        onView(withTagValue(equalTo(R.drawable.medcar))).check(matches(isDisplayed()));
        onView(withId(R.id.Introtext)).check(matches(withText(R.string.chooseServerText)));
    }

    @After
    public void tearDown() {
        mainActivity = null;
        Intents.release();
    }
}