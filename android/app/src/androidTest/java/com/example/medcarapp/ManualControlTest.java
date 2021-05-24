package com.example.medcarapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ManualControlTest {

    private ManualControl manualControl;

    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<ManualControl> manualControlActivityTestRule = new ActivityTestRule<ManualControl>(ManualControl.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, ManualControl.class);
            result.putExtra(targetContext.getString(R.string.switchServer),true);
            return result;
        }
    };

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class,true,false);

    @Before
    public void setUp() {
        manualControl = manualControlActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.medcarapp", appContext.getPackageName());
    }

    @Test
    public void verifyLoadedElements(){
        View view = manualControl.findViewById(R.id.connectionText);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.cameraView);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.angleHeader);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.angleIndicator);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.speedometer);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.autonomousDrivingButton);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.joystickView2);
        assertNotNull(view);
        onView(withTagValue(equalTo(R.drawable.intermission))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyLayoutPositions(){
        //Using cameraView as a reference to every other component
        onView(withId(R.id.speedometer)).check(isCompletelyBelow(withId(R.id.cameraView)));
        onView(withId(R.id.angleHeader)).check(isCompletelyBelow(withId(R.id.cameraView)));
        onView(withId(R.id.angleIndicator)).check(isCompletelyBelow(withId(R.id.cameraView)));
        onView(withId(R.id.joystickView2)).check(isCompletelyBelow(withId(R.id.cameraView)));
        onView(withId(R.id.autonomousDrivingButton)).check(isCompletelyBelow(withId(R.id.cameraView)));
        onView(withId(R.id.connectionText)).check(isCompletelyBelow(withId(R.id.cameraView)));
    }

    @Test
    public void checkAutoButton(){
        assertNotNull(manualControl);
        onView(withId(R.id.autonomousDrivingButton)).check(matches(withText(R.string.startingAutonomousButtonText)));
        onView(withId(R.id.autonomousDrivingButton)).perform(click());
        onView(withId(R.id.autonomousDrivingButton)).check(matches(withText(R.string.autonomousDrivingOnButtonText)));
        onView(withId(R.id.autonomousDrivingButton)).perform(click());
        onView(withId(R.id.autonomousDrivingButton)).check(matches(withText(R.string.autonomousDrivingOffButtonText)));
    }

    @Test
    public void checkJoystickMovement(){
        onView(withText(R.string.connectedToMQTTBrokerMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        JoystickView joystickView = manualControl.findViewById(R.id.joystickView2);
        assertNotNull(joystickView);
        onView(withId(R.id.angleIndicator)).check(matches(withText(R.string.initialAngleIndicatorValue)));
        onView(withId(R.id.joystickView2)).perform(swipeUp());
        onView(withId(R.id.joystickView2)).perform(swipeDown());
        onView(withId(R.id.joystickView2)).perform(swipeLeft());
        onView(withId(R.id.joystickView2)).perform(swipeRight());
        onView(withId(R.id.angleIndicator)).check(matches(withText(R.string.initialAngleIndicatorValue)));
    }

    @Test
    public void onBackPressed() {
        /*Espresso.pressBackUnconditionally();
        mainActivityActivityTestRule.launchActivity(null);
        intended(hasComponent(MainActivity.class.getName()));*/
    }

    @After
    public void tearDown() {
        manualControl = null;
        Intents.release();
    }
}