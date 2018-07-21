package unl.fct.fgodinho.dslapp;

public class HomeActivity extends BottomNavigationBaseActivity {


    @Override
    int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

}
