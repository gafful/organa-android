package com.mukaase.android.organa.console

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mukaase.android.organa.LiveDataTestUtil.getValue
import com.mukaase.android.organa.engine.Engine
import com.mukaase.android.organa.engine.FakeTagger
import com.mukaase.android.organa.util.FileUtils
//import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File


@ExperimentalCoroutinesApi
class ConsoleViewModelTest {
    //    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var sourceFolder = TemporaryFolder()

    @get:Rule
    var destFolder = TemporaryFolder()

    private lateinit var engine: Engine
    private lateinit var viewModel: ConsoleViewModel
    private lateinit var sourceDir: File
    private lateinit var destDir: File


    @Before
    fun setupViewModel() {
        engine = Engine(FakeTagger())
        viewModel = ConsoleViewModel(engine)

        // Init source directory
        sourceFolder.newFile("audio1.mp3")
        sourceFolder.newFile("audio2.mp3")

        assertEquals(2, sourceFolder.root.listFiles().size)

        sourceDir = sourceFolder.root
        destDir = destFolder.root
    }

    // Apparently I have to refactor
    // https://stackoverflow.com/questions/17681708/mocking-files-in-java-mock-contents-mockito
    @Test
    fun setsUpSource_GivenValidSourceFile() {
        viewModel.initSourceDirectory(sourceDir, Dispatchers.Unconfined)

        assertThat(getValue(viewModel.srcDirName)).contains(sourceDir.name)
        assertThat(getValue(viewModel.srcDirPath)).contains(sourceDir.path)
        assertThat(getValue(viewModel.srcStatus)).contains(ConsoleViewModel.CHECK_OK)
        assertEquals(2, getValue(viewModel.srcFiles).count())
        assertEquals(2, getValue(viewModel.srcDirAudioFileCount))
    }

    @Test
    fun setsUpDestination_GivenValidDestinationFile() {
        viewModel.initDestDirectory(destDir)

        assertThat(getValue(viewModel.destStatus)).contains(ConsoleViewModel.CHECK_OK)
        assertThat(getValue(viewModel.destDirName)).contains(destDir.name)
        assertThat(getValue(viewModel.destDirPath)).contains(destDir.path)
        assertThat(getValue(viewModel.destDirAvSpace)).contains(FileUtils.readableFileSize(destDir.freeSpace))
    }

    @Test
    fun startStartsTheEngine() = runBlockingTest {
        viewModel.start(1, sourceFolder.root)

        assertEquals(2, getValue(viewModel.engineStatsSequence).count())
    }
}