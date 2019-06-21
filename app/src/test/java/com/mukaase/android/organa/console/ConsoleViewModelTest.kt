package com.mukaase.android.organa.console

//import com.mukaase.android.organa.Engine
//import com.mukaase.android.organa.Tagger
import com.mukaase.android.organa.engine.Engine
import com.mukaase.android.organa.engine.Tagger
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.instanceOf
import org.mockito.Mockito.mock

class ViewModelFactoryTest {
    @Test
    fun createCreatesAconsoleViewModelInstance(){
        // Given
        val tagger = mock(Tagger::class.java)

        // When an object is created
        val viewModel = ViewModelFactory(Engine(tagger)).create()

        // It creates a ConsoleViewModel instance
        assertThat(viewModel, instanceOf(ConsoleViewModel::class.java))

        // InjectorUtils.provideConsoleViewModel(this) == a factory
        // ViewModelProviders.of(this, vmFactory).get(ConsoleViewModel::class.java)
        // Creation of viewModel not tested!
        
    }
}