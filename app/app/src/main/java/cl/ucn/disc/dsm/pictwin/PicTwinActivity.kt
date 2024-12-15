



package cl.ucn.disc.dsm.pictwin

import android.os.Bundle;
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.ucn.disc.dsm.pictwin.ui.theme.PicTwinTheme

/**
 * Activity: PicTwin
 */
class PicTwinActivity : ComponentActivity() {

    /**
     * onCreate: build the UI
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        // call the super!
        super.onCreate(savedInstanceState)

        //setContent: build the UI!
        setContent {
            PicTwinTheme {
                PicTwinScaffold()
            }
        }
    }

}

/**
 * PicTwinScaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicTwinScaffold() {

    // the scroll behavior
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        snapAnimationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    // the scaffold
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PicTwinTopBar(
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FabActionButton()
        }
    ) { innerPadding ->
        PicTwinList(
            innerPadding = innerPadding,
        )
    }
}

/**
 * The PicTwin pair.
 */
data class PicturePair(
    val leftImage: Painter,
    val rightImage: Painter
)

/**
 * The List of PicTwins.
 */