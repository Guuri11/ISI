package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.TaskType

@Composable
fun Sidebar(modifier: Modifier = Modifier, filterCommands: (taskType: TaskType?) -> Unit, goTo: (String) -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "All",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().clickable {
                filterCommands(null)
            })
        Spacer(modifier = Modifier.height(16.dp))
        for (taskType in TaskType.entries) {
            if (taskType.show) {
                Text(
                    text = taskType.value,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth().clickable {
                        filterCommands(taskType)
                    })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 16.dp))

        Text(
            text = "Settings",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().clickable {
                goTo("/settings")
            })
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Car coords",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().clickable {
                goTo("/car-coords")
            })
    }
}