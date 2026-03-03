package com.smartnotes.notepadplusplus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import com.smartnotes.notepadplusplus.ui.addedit.AddEditViewModel
import com.smartnotes.notepadplusplus.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddEditViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var repository: NoteRepository

    private lateinit var viewModel: AddEditViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddEditViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveNote with blank title returns error`() = runTest {
        viewModel.saveNote("", "Some content")
        val state = viewModel.saveState.value
        Assert.assertTrue(state is UiState.Error)
        Assert.assertEquals("Title cannot be empty", (state as UiState.Error).message)
    }

    @Test
    fun `saveNote with whitespace title returns error`() = runTest {
        viewModel.saveNote("   ", "Some content")
        val state = viewModel.saveState.value
        Assert.assertTrue(state is UiState.Error)
    }

    @Test
    fun `saveNote with valid title inserts note`() = runTest {
        val note = NoteEntity(title = "Test Title", content = "Content")
        `when`(repository.insertNote(org.mockito.kotlin.any())).thenReturn(Result.success(1L))
        viewModel.saveNote("Test Title", "Content")
        val state = viewModel.saveState.value
        Assert.assertTrue(state is UiState.Success)
    }

    @Test
    fun `hasUnsavedChanges is false for new empty note`() {
        Assert.assertFalse(viewModel.hasUnsavedChanges("", ""))
    }

    @Test
    fun `hasUnsavedChanges is true for new note with content`() {
        Assert.assertTrue(viewModel.hasUnsavedChanges("Some title", ""))
    }

    @Test
    fun `loadNote with invalid id sets success with null`() = runTest {
        viewModel.loadNote(-1L)
        val state = viewModel.noteState.value
        Assert.assertTrue(state is UiState.Success)
        Assert.assertNull((state as UiState.Success).data)
    }
}
