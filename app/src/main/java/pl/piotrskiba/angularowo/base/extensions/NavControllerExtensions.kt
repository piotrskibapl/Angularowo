package pl.piotrskiba.angularowo.base.extensions

import androidx.navigation.NavController
import pl.piotrskiba.angularowo.R

fun NavController.changeStartDestination(startDestination: Int) {
    val navGraph = navInflater.inflate(R.navigation.main_nav_graph)
    navGraph.setStartDestination(startDestination)
    graph = navGraph
}
