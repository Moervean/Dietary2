package Dagger;

import com.example.dietary.NavigationBar;
import com.example.dietary.ui.invites.InvitesFragment;
import com.example.dietary.ui.search.SearchFragment;
import com.example.dietary.ui.setProfile.SetProfileFragment;

import javax.inject.Singleton;

import Activites.AddDietActivity;
import Activites.AddPersonActivity;
import Activites.AnotherExerActivity;
import Activites.AnotherUserDiet;
import Activites.AnotherUserProfile;
import Activites.CoachesList;
import Activites.CreateAccountActivity;
import Activites.DietShow;
import Activites.EditProtegeDietActivity;
import Activites.EditProtegeWorkoutActivity;
import Activites.ExerShow;
import Activites.ExerciseActivity;
import Activites.ProtegesList;
import Data.AddMealDialog;
import Data.AddTrainerRecyclerAdapter;
import Data.AddWorkoutDialog;
import Data.ExerRecyclerAdapter;
import Data.ProtegeListRecyclerAdapter;
import Data.SearchAddRecyclerAdapter;
import Data.TrainerListRecyclerViewAdapter;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface DaggerConstsComponent {

    void inject(AddDietActivity addDietActivity);

    void inject(AddPersonActivity addPersonActivity);

    void inject(AnotherExerActivity anotherExerActivity);

    void inject(AnotherUserDiet anotherUserDiet);

    void inject(AnotherUserProfile anotherUserProfile);

    void inject(CoachesList coachesList);

    void inject(CreateAccountActivity createAccountActivity);

    void inject(DietShow dietShow);

    void inject(EditProtegeDietActivity editProtegeDietActivity);

    void inject(EditProtegeWorkoutActivity editProtegeWorkoutActivity);

    void inject(ExerciseActivity exerciseActivity);

    void inject(ExerShow exerShow);

    void inject(ProtegesList protegesList);

    void inject(InvitesFragment invitesFragment);

    void inject(SearchFragment searchFragment);

    void inject(SetProfileFragment setProfileFragment);

    void inject(NavigationBar navigationBar);

    void inject(AddMealDialog addMealDialog);

    void inject(AddTrainerRecyclerAdapter addTrainerRecyclerAdapter);

    void inject(AddWorkoutDialog addWorkoutDialog);

    void inject(ExerRecyclerAdapter exerRecyclerAdapter);

    void inject(ProtegeListRecyclerAdapter protegeListRecyclerAdapter);

    void inject(SearchAddRecyclerAdapter searchAddRecyclerAdapter);

    void inject(TrainerListRecyclerViewAdapter trainerListRecyclerViewAdapter);


    Consts provideConsts();


}
