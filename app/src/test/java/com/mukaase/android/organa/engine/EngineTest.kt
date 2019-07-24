//package com.mukaase.android.organa.engine
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.mukaase.android.organa.console.MainCoroutineRule
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.rules.TemporaryFolder
//import java.io.File
//
//class EngineTest {
//    @get: Rule
//    var sourceFolder = TemporaryFolder()
//
////    @get:Rule
////    var mainCoroutineRule = MainCoroutineRule()
////
////    @get:Rule
////    var instantExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var sourceDir: File
//    private lateinit var engine: Engine
//    private val fakeTagger = FakeTagger()
//
//    @Before
//    fun setup(){
//        engine = Engine(fakeTagger)
//        sourceDir = sourceFolder.root
//    }
//
//    @Test
//    fun `startCreates_ASequenceOfStatsGivenAValidSourceFile`() = runBlockingTest {
//        val stats = engine.start(2, sourceDir)
//
//        assertEquals(2, 2)
//    }
//}