package com.smartnotes.notepadplusplus

import com.smartnotes.notepadplusplus.data.database.NoteDao
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NoteRepositoryTest {

    @Mock
    private lateinit var noteDao: NoteDao

    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        repository = NoteRepository(noteDao)
    }

    @Test
    fun `getAllNotes returns flow from dao`() = runTest {
        val notes = listOf(NoteEntity(1, "Note", "Content"))
        `when`(noteDao.getAllNotes()).thenReturn(flowOf(notes))
        val flow = repository.getAllNotes()
        Assert.assertNotNull(flow)
    }

    @Test
    fun `insertNote returns success result`() = runTest {
        val note = NoteEntity(title = "Test", content = "Content")
        `when`(noteDao.insertNote(note)).thenReturn(1L)
        val result = repository.insertNote(note)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(1L, result.getOrNull())
    }

    @Test
    fun `insertNote returns failure on exception`() = runTest {
        val note = NoteEntity(title = "Test", content = "Content")
        `when`(noteDao.insertNote(note)).thenThrow(RuntimeException("DB Error"))
        val result = repository.insertNote(note)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun `deleteNote calls dao deleteNote`() = runTest {
        val note = NoteEntity(1, "Test", "Content")
        val result = repository.deleteNote(note)
        verify(noteDao).deleteNote(note)
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun `getNoteById returns null when not found`() = runTest {
        `when`(noteDao.getNoteById(999L)).thenReturn(null)
        val note = repository.getNoteById(999L)
        Assert.assertNull(note)
    }

    @Test
    fun `getNoteById returns note when found`() = runTest {
        val expected = NoteEntity(1, "Title", "Content")
        `when`(noteDao.getNoteById(1L)).thenReturn(expected)
        val note = repository.getNoteById(1L)
        Assert.assertEquals(expected, note)
    }
}
