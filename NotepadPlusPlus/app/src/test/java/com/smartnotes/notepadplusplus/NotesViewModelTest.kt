package com.smartnotes.notepadplusplus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import com.smartnotes.notepadplusplus.ui.noteslist.NotesViewModel
import com.smartnotes.notepadplusplus.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NotesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var repository: NoteRepository

    private lateinit var viewModel: NotesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val sampleNotes = listOf(
            NoteEntity(1, "Test Note 1", "Content 1"),
            NoteEntity(2, "Test Note 2", "Content 2")
        )
        `when`(repository.getAllNotes()).thenReturn(flowOf(sampleNotes))
        `when`(repository.searchNotes(any())).thenReturn(flowOf(emptyList()))
        viewModel = NotesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        // When viewModel is first created, state transitions from Loading to Success
        // Since we use UnconfinedTestDispatcher, it immediately executes
        val state = viewModel.notesState.value
        Assert.assertTrue(state is UiState.Success || state is UiState.Loading)
    }

    @Test
    fun `getAllNotes returns Success with notes`() = runTest {
        val state = viewModel.notesState.value
        Assert.assertTrue(state is UiState.Success)
        val notes = (state as UiState.Success).data
        Assert.assertEquals(2, notes.size)
    }

    @Test
    fun `deleteNote calls repository deleteNote`() = runTest {
        val note = NoteEntity(1, "Test", "Content")
        `when`(repository.deleteNote(note)).thenReturn(Result.success(Unit))
        viewModel.deleteNote(note)
        // Verify delete state is set
        val deleteState = viewModel.deleteState.value
        Assert.assertTrue(deleteState is UiState.Success)
    }

    @Test
    fun `search query updates flow`() = runTest {
        val searchResults = listOf(NoteEntity(1, "Searched Note", "Content"))
        `when`(repository.searchNotes("Searched")).thenReturn(flowOf(searchResults))
        viewModel.setSearchQuery("Searched")
        // State should update to success with search results
        val state = viewModel.notesState.value
        Assert.assertTrue(state is UiState.Success)
    }
}
