package com.developer_rahul.agrideal;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.developer_rahul.agrideal.AgriNewsFragment.AgriNewsFragment;
import com.developer_rahul.agrideal.HomeFragment.HomeFragment;
import com.developer_rahul.agrideal.Learn.LearnFragment;
import com.developer_rahul.agrideal.ProfileFragment.ProfileFragment;
import com.developer_rahul.agrideal.SaleFragment.SaleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final AgriNewsFragment agriNewsFragment = new AgriNewsFragment();
    private final LearnFragment learnFragment = new LearnFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    private final SaleFragment saleFragment = new SaleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            loadFragment(homeFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home_fragment) {
            selectedFragment = homeFragment;
        } else if (itemId == R.id.navigation_agrinews_fragment) {
            selectedFragment = agriNewsFragment;
        } else if (itemId == R.id.navigation_learn_fragment) {
            selectedFragment = learnFragment;
        } else if (itemId == R.id.navigation_profile_fragment) {
            selectedFragment = profileFragment;
        } else if (itemId == R.id.navigation_sale_fragment) {
            selectedFragment = saleFragment;
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment) // Use correct ID from XML
                .commit();
    }
}
