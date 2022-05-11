package com.mostafan3ma.android.pcm_helper10

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.mostafan3ma.android.pcm_helper10.data.source.*
import com.mostafan3ma.android.pcm_helper10.databinding.ActivityMainBinding
import com.mostafan3ma.android.pcm_helper10.lines.MainLinesFragmentDirections
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /////new code drawer layout
        drawerLayout=binding.drawerLayout

        val navController=findNavController(R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)
        navController.addOnDestinationChangedListener{nc:NavController,nd:NavDestination,bundle:Bundle?->
            if (nd.id==nc.graph.startDestinationId){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }else{
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        binding.navView.menu.getItem(2).setOnMenuItemClickListener { item->
            Toast.makeText(this,"Contact Clicked",Toast.LENGTH_SHORT).show()
            Log.i("Mostafa Log", "onCreate: Contact Clicked")
            navController.navigate(MainLinesFragmentDirections.actionLinesMainFragmentToContactFragment())
            true
        }


        NavigationUI.setupWithNavController(binding.navView,navController)







//        val navController=findNavController(R.id.nav_host_fragment_content_main)
//        navController.let {
//            appBarConfiguration= AppBarConfiguration(navController.graph)
//            setSupportActionBar(binding.toolbar)
//            binding.toolbar.setupWithNavController(it,appBarConfiguration)
//        }


    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration)



//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
    }

}