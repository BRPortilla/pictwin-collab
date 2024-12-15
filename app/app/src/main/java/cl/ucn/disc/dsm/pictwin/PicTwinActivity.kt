



package cl.ucn.disc.dsm.pictwin

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ucn.disc.dsm.pictwin.data.model.Persona
import cl.ucn.disc.dsm.pictwin.data.services.Service
import cl.ucn.disc.dsm.pictwin.ui.theme.PicTwinTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import javax.inject.Inject

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicTwinList(
    innerPaddingValues: PaddingValues,
) {

    //TODO: retrieve this list from the server
    val pictures = listOf(
        PicturePair(
            leftImage = painterResource(id = R.drawable.image_ucn),
            rightImage = painterResource(id = R.drawable.image_portada),
        ),
        PicturePair(
            leftImage = painterResource(id = R.drawable.image_ucn),
            rightImage = painterResource(id = R.drawable.image_portada),
        ),
    )

    // Values list.
    LazyColumn (
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = innerPadding.calculateTopPadding(),
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        items(pictures) { picturePair ->
            PicTwinRow(twin = picturePair)
        }
    }
}


/**
 * PicTwinRow.
 */
@Composable
fun PicTwinRow(twin: PicturePair) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = twin.leftImage,
            //FIXME: it does not have to be null the description.
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = twin.rightImage,
            //FIXME: it does not have to be null the description.
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * PicTwinTopBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicTwinTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = { Text(text = "PicTwin Application") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors()
    )
}

/**
 * FabActionButton.
 */
@Composable
fun FabActionButton(
    onAction: () -> Unit = { },
) {
    FloatingActionButton(
        onClick = {
        },
        containerColor = MaterialTheme.colorScheme.tertiary,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}

/**
 * The ViewModel.
 */
@HiltViewModel
class PicTwinListModel @Inject constructor(
    private val service: Service,
) : ViewModel() {

    // the logger
    private val _log = LoggerFactory.getLogger(PicTwinListViewModel::class.java)

    // internal state
    private val _state = MutableStateFlow<State>(State.Initial)

    // public state (as flow)
    val state = _state.asStateFlow()

    // init
    init {
        _log.debug("Fetching Persona ..")
        viewModelScope.launch {
            //FIXME: refresh Persona is in the Service class, needs to be implemented with the fetch method.
            refreshPersona()
        }
    }

    /**
     * Refresh the Persona in the background.
     */
    fun refresh() {
        viewModelScope.launch {
            refreshPersona()
        }
    }

    /**
     * Retrieve the Persona.
     */
    private suspend fun refreshPersona() {
        _state.value = State.Loading

        service.retrieve()
            .onSuccess { persona ->
                _state.value = State.Success(persona)
                _log.debug("Persona fetched: {}",persona)
            }
            .onFailure { error ->
                _state.value = State.Error(error.message ?: "Unknown error.")
                _log.error("Error fetching Persona: {}", error.message)
            }
    }

    /**
     * The state of the view.
     */
    sealed class State {
        object Initial : State()
        object Loading : State()
        data class Success(val persona: Persona) : State()
        data class Error(val message: String) : State()
    }

}

/**
 * Preview.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PicTwinScaffoldPreview() {
    PicTwinTheme {
        PicTwinScaffold()
    }
}