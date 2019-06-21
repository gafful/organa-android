package com.mukaase.android.organa.console

import com.mukaase.android.organa.LiveDataTestUtil.getValue
import com.mukaase.android.organa.engine.Engine
import com.mukaase.android.organa.engine.Tagger
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.io.File

@ExperimentalCoroutinesApi
class ConsoleViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var engine: Engine
    private lateinit var viewModel: ConsoleViewModel

    @Before
    fun setupViewModel() {
        viewModel = ConsoleViewModel(engine)
    }

    @Test
    fun createCreatesAconsoleViewModelInstance(){
        mainCoroutineRule.pauseDispatcher()

        val f = mock(File::class.java)
        val ff = File("/")
        
        viewModel.setSourceDirectory(f)


        // setSourceDirectory sets srcDir nam, path, status, srcDirAudioFileCount, srcFiles, srcStatus
        assertThat(getValue(viewModel.srcDirName)).contains()
//        assertThat()
    }
}