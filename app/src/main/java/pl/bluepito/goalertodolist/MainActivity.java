package pl.bluepito.goalertodolist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;

import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.Fragments.AddCategoryFragmentDialog;
import pl.bluepito.goalertodolist.Fragments.AddTaskFragmentDialog;
import pl.bluepito.goalertodolist.Fragments.CategoriesFragment;
import pl.bluepito.goalertodolist.Fragments.DonateFragment;
import pl.bluepito.goalertodolist.Fragments.HomeFragment;
import pl.bluepito.goalertodolist.Fragments.SettingsFragment;
import pl.bluepito.goalertodolist.Fragments.TaskByCategoryFragment;
import pl.bluepito.goalertodolist.Models.Category;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    FloatingActionButton add_fab, add_category_fab, add_task_fab;
    Animation FabOpen, FabClose, FabRotateClockwise, FabRotateAntiClockwise;
    boolean isFabOpen = false;
    private List<Category> allCategories;
    private HashMap<String, Integer> allCategoriesForMenu = new HashMap<String, Integer>();
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Conect to db
        databaseHelper = new DatabaseHelper(this);

        //Seting fabs
        add_fab = (FloatingActionButton) findViewById(R.id.floating_button_main);
        add_category_fab = (FloatingActionButton) findViewById(R.id.floating_button_add_category);
        add_task_fab = (FloatingActionButton) findViewById(R.id.floating_button_add_task);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);


        // Seting toolbar for activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Seting drawer layout with navigation
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        addCategoriesToMenu(navigationView);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Seting fragment changing
        if (savedInstanceState == null) {
            HomeFragment f1 = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, f1);
            fragmentTransaction.commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFabOpen) {

                    add_category_fab.startAnimation(FabClose);
                    add_task_fab.startAnimation(FabClose);
                    add_fab.startAnimation(FabRotateAntiClockwise);
                    add_category_fab.setClickable(false);
                    add_task_fab.setClickable(false);
                    isFabOpen = false;


                } else {
                    add_category_fab.startAnimation(FabOpen);
                    add_task_fab.startAnimation(FabOpen);
                    add_fab.startAnimation(FabRotateClockwise);
                    add_category_fab.setClickable(true);
                    add_task_fab.setClickable(true);
                    isFabOpen = true;

                }


            }
        });

        add_category_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_category_fab.startAnimation(FabClose);
                add_task_fab.startAnimation(FabClose);
                add_fab.startAnimation(FabRotateAntiClockwise);
                add_category_fab.setClickable(false);
                add_task_fab.setClickable(false);
                isFabOpen = false;


                AddCategoryFragmentDialog dialog = new AddCategoryFragmentDialog();


                dialog.show(getSupportFragmentManager(), "AddCategoryFragmentDialog");


            }
        });

        add_task_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_category_fab.startAnimation(FabClose);
                add_task_fab.startAnimation(FabClose);
                add_fab.startAnimation(FabRotateAntiClockwise);
                add_category_fab.setClickable(false);
                add_task_fab.setClickable(false);
                isFabOpen = false;

                AddTaskFragmentDialog dialog = new AddTaskFragmentDialog();
                dialog.show(getSupportFragmentManager(), "AddTaskFragmentDialog");

            }
        });

    }

    public void addCategoriesToHashMap(HashMap<String, Integer> hashMap) {
        allCategories = databaseHelper.getAllCategories();
        for (Category category : allCategories) {
            hashMap.put(category.getName(), category.getId());
        }
    }

    public void addCategoriesToMenu(NavigationView navigationView) {
        addCategoriesToHashMap(allCategoriesForMenu);

        final Menu mainMenu = navigationView.getMenu();
        final SubMenu categoriesSubMenu = mainMenu.addSubMenu("Categories");

        for (int i = 0; i < allCategories.size(); i++) {
            categoriesSubMenu.add(allCategories.get(i).getName());
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = new HomeFragment();
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                menuItem.setChecked(true);
                break;
            case R.id.nav_categories:
                fragment = new CategoriesFragment();
                menuItem.setChecked(true);
                break;
    /*            case R.id.nav_settings:
                fragment = new SettingsFragment();
                menuItem.setChecked(true);
                break;
            case R.id.nav_donate:
                fragment = new DonateFragment();
                menuItem.setChecked(true);
                break; */
            default:
                String menuItemTitle = menuItem.getTitle().toString();
                if (isCategorySelected(menuItem.getTitle().toString()) && allCategoriesForMenu.get(menuItemTitle) != null) {
                    int id = allCategoriesForMenu.get(menuItemTitle);
                    fragment = new TaskByCategoryFragment(id);
                }
                break;

        }


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public boolean isCategorySelected(String title) {
        if (allCategoriesForMenu.get(title) != null) {
            return true;
        } else {
            return false;
        }

    }
}