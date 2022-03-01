package com.example.mareu;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;


import android.content.Context;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.meeting_list.ListMeetingActivity;
import com.example.mareu.utils.DeleteViewAction;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    //This is fixed
    private static int ITEMS_COUNT = 8;
    private static int ITEM_POSITION = 0;

    private MeetingApiService mService;
    private List<Meeting> mMeetingList;
    private ListMeetingActivity mActivity;
    private List<Meeting> meetingsFilterByRoomA;

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule =
            new ActivityTestRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());

        mService = DI.getMeetingApiService();
        mMeetingList = mService.getMeetings();
        meetingsFilterByRoomA = mService.getMeetingsFilteredByRoom("Room A");
    }

    /**
     * Au lancement de l'appli, on s'assure que le RecyclerView affiche bien l'ensemble des éléments
     * de la liste des meetings
     */
    @Test
    public void myMeetingsList_shouldNotEmpty() {

        ViewInteraction recyclerView = onView(allOf(withId(R.id.list_meetings), isDisplayingAtLeast(60)));
        recyclerView.check(matches(hasChildCount(ITEMS_COUNT)));
    }

    /**
     * Lorsqu'on supprime un élement, le recyclerview (RV)  affiche un élement de moins
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        // Le RV affiche bien 12 élements au lancement
        ViewInteraction recyclerView = onView(allOf(withId(R.id.list_meetings), isDisplayingAtLeast(60)));
        recyclerView.check(withItemCount(ITEMS_COUNT));
        // on supprime le premier élement de la liste
        recyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_POSITION,new DeleteViewAction()));
        // On vérifie bien que le RV ne compte que 11 élements au lieu de 12
        recyclerView.check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * Lorsqu'on ajoute un élement, la liste compte un élement de plus
     */
    @Test
    public void myMeetingsList_addAction_shouldIncrementByOne() throws InterruptedException {
        //On clique sur le bouton d'ajout de meeting
        onView(withId(R.id.start_add_activity)).perform(click());

        /*
            1. On clique sur le premier élement
            2. On selectionne le 1er élement = "Room A"
         */
        onView(withId(R.id.addMeeting_tf_room)).perform(click());

        /*
            1. On clique sur le champ de texte SUJET, on saiit la valeur "Test"
            2. puis on ferme le clavier, aprés la saisie
        */
        onView(withId(R.id.addMeeting_subject)).perform(click(),typeText("Test"));
        closeSoftKeyboard();

        /*
            1. On clique sur le bouton TIME
            2. on configure le TimePicker sur les valeurs H:10 & M:00
            3. On valide la saisie
         */
        onView(withId(R.id.addMeeting_button_saveTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).
                perform(PickerActions.setTime(10,00));
        onView(withText("ok")).perform(click());

         /*
            1. On clique sur le bouton DATE
            2. on configure le DatePicker sur les valeurs Y:2022 & M:10 & D:30
            3. On valide la saisie
         */
        onView(withId(R.id.addMeeting_button_saveDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).
                perform(PickerActions.setDate(2022,10,30));
        onView(withText("ok")).perform(click());


        /*
            1. on clique sur le bouton d'ajout de participants
            2. on saisie une première fois la valeur indiquée puis on valide
            3. on répète l'opération une seconde fois
         */
        onView(withId(R.id.addMeeting_button_addEmails)).perform(click());
        onView(withId(R.id.addMeeting_dialog_participant)).perform(click(),typeText("sadek@test.com"));
        onView(withText("validate")).perform(click());
        onView(withId(R.id.addMeeting_button_addEmails)).perform(click());
        onView(withId(R.id.addMeeting_dialog_participant)).perform(click(),typeText("hatem@test.com"));
        onView(withText("validate")).perform(click());

        //on clique sur le bouton save afin de créer notre meeting
        onView(withId(R.id.addMeeting_save)).perform(click());

        Thread.sleep(3000);

        //on vérifie bien que le RV compte maintenant un élement en plus
        ViewInteraction recyclerView = onView(allOf(withId(R.id.list_meetings), isDisplayingAtLeast(60)));
        recyclerView.check(withItemCount(ITEMS_COUNT + 1));

    }

    /**
     * Aprés avoir ajouter un élement, on filtre les élements de la liste suivant la salle
     * Au moins un élèment doit apparaitre, celui ajouté dont la salle correspond au critère
     */
    @Test
    public void myMeetingsList_filterByRoom_shouldContainsAtLeastOne() throws InterruptedException {
        //On ajoute un élement en spécifiant à Room la valeur "Room A"
        myMeetingsList_addAction_shouldIncrementByOne();

        /*
            1. on clique sur l'icone de filtre
            2. on sélectionne l'option Filtre par salle
            3. on clique sur le bouton ok ce qui affecte par défaut la valeur "Room A"
         */
        onView(withId(R.id.menu_activity_item_sort_by)).perform(click());
        onView(withText("Filtrer par salle")).perform(click());
        onView(withText("ok")).perform(click());

        // on vérifie que le RV compte au minimum 1 nélement celui ajouté
        ViewInteraction recyclerView = onView(allOf(withId(R.id.list_meetings), isDisplayingAtLeast(60)));
        recyclerView.check(matches(hasChildCount(meetingsFilterByRoomA.size() + 1)));
    }

    /**
     * Aprés avoir ajouter un élement, on filtre les élements de la liste suivant la date
     * Au moins un élèment doit apparaitre, celui ajouté dont la salle correspond au critère
     */
    @Test
    public void myMeetingsList_filterByDate_shouldContainsAtLeastOne() throws InterruptedException {
        //On ajoute un élement en spécifiant à Date 30/10/2022
        myMeetingsList_addAction_shouldIncrementByOne();

        /*
            1. on clique sur l'icone de filtre
            2. on sélectionne l'option Filtre par date
            3. on configure le DatePicker
         */
        onView(withId(R.id.menu_activity_item_sort_by)).perform(click());
        onView(withText("Filtrer par date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).
                perform(PickerActions.setDate(2022,10,30));
        onView(withText("ok")).perform(click());

        /* On clique sur l'icone et on selectionne l'option Réinitialiser
            afin de charger la liste principale

         */
        onView(withId(R.id.menu_activity_item_sort_by)).perform(click());
        onView(withText("Réinitialiser")).perform(click());
        Thread.sleep(3000);

        // on vérifie que le RV compte au minimum 1 élement celui ajouté
        ViewInteraction recyclerView = onView(allOf(withId(R.id.list_meetings), isDisplayingAtLeast(60)));
        recyclerView.check(matches(hasMinimumChildCount(1)));
    }



    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mareu", appContext.getPackageName());
    }


}